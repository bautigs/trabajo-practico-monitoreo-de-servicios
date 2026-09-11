package tp.services.usuarios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tp.dto.PerfilResponse;
import tp.models.comunidad.Persona;
import tp.models.builders.PersonaBuilder;

import tp.repositories.RepositorioPersonas;
import tp.repositories.RepositorioTipoDeServicio;
import tp.server.exceptions.RecursoNoEncontradoException;
import tp.repositories.RepositorioEntidades;
import tp.repositories.RepositorioProvincias;
import tp.repositories.RepositorioMunicipios;
import tp.repositories.RepositorioLocalidades;
import tp.services.usuarios.PerfilMapper;
import tp.dto.ActualizarPerfilRequest;
import tp.models.comunidad.Sincronico;
import tp.models.comunidad.Asincronico;
import tp.models.notificador.NoExisteFormatoException;
import tp.server.exceptions.SolicitudInvalidaException;
import tp.models.servicios.TipoServicio;
import tp.models.entidad.Entidad;
import tp.dto.UbicacionRequest;
import tp.services.georef.Provincia;
import tp.services.georef.Municipio;
import tp.services.georef.Localidad;

@Service
public class PerfilService {

    private final RepositorioPersonas repositorioPersonas;
    private final RepositorioTipoDeServicio repositorioTipoDeServicio;
    private final RepositorioEntidades repositorioEntidades;
    private final RepositorioProvincias repositorioProvincias;
    private final RepositorioMunicipios repositorioMunicipios;
    private final RepositorioLocalidades repositorioLocalidades;
    private final PerfilMapper mapper;

    public PerfilService(RepositorioPersonas repositorioPersonas, RepositorioTipoDeServicio repositorioTipoDeServicio,
            RepositorioEntidades repositorioEntidades, RepositorioProvincias repositorioProvincias,
            RepositorioMunicipios repositorioMunicipios, RepositorioLocalidades repositorioLocalidades,
            PerfilMapper mapper) {
        this.repositorioPersonas = repositorioPersonas;
        this.repositorioTipoDeServicio = repositorioTipoDeServicio;
        this.repositorioEntidades = repositorioEntidades;
        this.repositorioProvincias = repositorioProvincias;
        this.repositorioMunicipios = repositorioMunicipios;
        this.repositorioLocalidades = repositorioLocalidades;
        this.mapper = mapper;
    }

    @Transactional
    public PerfilResponse perfilActual(Jwt jwt, List<String> roles) {
        Persona persona = repositorioPersonas.findByKeycloakId(jwt.getSubject()).orElseGet(() -> provisionar(jwt));

        return mapper.toResponse(persona, jwt.getClaimAsString("preferred_username"), roles);
    }

    // Por qué transactional en vez de repositorio.save? La transaccion hace que
    // persona sea una entidad managed que hbernate mantiene en su persistence
    // context. Por ende, al hacer commit, compara
    // el estado de la entidad en dicho contexto con el de la db y genera el UPDATE
    // automáticamente (dirty checking). Con el save sería este mismo mecanismo
    // el que logre la actualizacion en db pues save hace internamente un
    // entityManager.merge. Como la entidad ya esta en el persistence context, merge
    // detecta
    // que es la misma instancia y no hace nada extra. Entonces save solo es
    // obligatorio si la entidad esta detached (traida de otra transaccion o
    // construida a mano)

    // 1. Atomicidad: la actualizacion de campos como servicios y entidades de
    // intereses que tienen sus propias operaciones para con la db. Si algo falla
    // (se lanza excepcion)
    // se deshace cualquier cambio parcial con el rollback

    // 2. mapper.toResponse(persona, ...) lee las colecciones de persona, lazy por
    // defecto. Esto requiere que la sesion de Hibernate este abierta.

    @Transactional
    public PerfilResponse actualizarPerfil(Jwt jwt, List<String> roles, ActualizarPerfilRequest request) {

        Persona persona = personaDe(jwt);

        if (request.numeroDeContacto() != null) {
            persona.setNumeroDeContacto(request.numeroDeContacto());
        }
        if (request.estrategiaNotificacion() != null) {
            asignarEstrategia(persona, request.estrategiaNotificacion());
        }
        if (request.configuracionRecepcion() != null) {
            persona.setConfiguracion("SINCRONICO".equals(request.configuracionRecepcion())
                    ? new Sincronico()
                    : new Asincronico());
        }
        if (request.tiposDeServicioDeInteres() != null) {
            persona.setTiposDeServiciosDeInteres(resolverTiposDeServicio(request.tiposDeServicioDeInteres()));
        }
        if (request.entidadesInteres() != null) {
            persona.setEntidadesDeInteres(resolverEntidades(request.entidadesInteres()));
        }

        return respuesta(persona, jwt, roles);
    }

    @Transactional
    public PerfilResponse actualizarUbicacion(Jwt jwt, List<String> roles, UbicacionRequest request) {
        Persona persona = personaDe(jwt);

        Provincia provincia = repositorioProvincias.findById(request.provinciaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la provincia " + request.provinciaId()));

        Municipio municipio = null;

        if (request.municipioId() != null) {
            municipio = repositorioMunicipios.findById(request.municipioId()).orElseThrow(
                    () -> new RecursoNoEncontradoException("No existe el municipio" + request.municipioId()));
            ;

            if (municipio.getProvincia() == null || !municipio.getProvincia().getId().equals(provincia.getId())) {
                throw new SolicitudInvalidaException("El municipio " + request.municipioId()
                        + " no pertenece a la provincia " + request.provinciaId());
            }
        }

        Localidad localidad = null;
        if (request.localidadId() != null) {
            if (municipio == null) {
                throw new SolicitudInvalidaException("No se puede indicar una localidad sin un municipio");
            }
            localidad = repositorioLocalidades.findById(request.localidadId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe la localidad " + request.localidadId()));
            if (localidad.getMunicipio() == null
                    || !localidad.getMunicipio().getId().equals(municipio.getId())) {
                throw new SolicitudInvalidaException("La localidad " + request.localidadId()
                        + " no pertenece al municipio " + request.municipioId());
            }
        }

        persona.setProvincia(provincia);
        persona.setMunicipio(municipio);
        persona.setLocalidad(localidad);

        return respuesta(persona, jwt, roles);
    }

    private List<TipoServicio> resolverTiposDeServicio(List<Long> ids) {
        List<TipoServicio> tipos = new ArrayList<>();

        for (Long id : ids) {
            TipoServicio tipo = repositorioTipoDeServicio.findById(id)
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe el tipo de servicio " + id));
            tipos.add(tipo);
        }

        return tipos;
    }

    private List<Entidad> resolverEntidades(List<Long> ids) {
        List<Entidad> entidades = new ArrayList<>();

        for (Long id : ids) {
            Entidad entidad = repositorioEntidades.findById(id)
                    .orElseThrow(() -> new RecursoNoEncontradoException("No exsite la entidad" + id));
            entidades.add(entidad);
        }

        return entidades;
    }

    private void asignarEstrategia(Persona persona, String estrategia) {
        try {
            persona.setEstrategia(estrategia); // Factory.crear espera "MAIL" | "WHATSAPP"
        } catch (NoExisteFormatoException e) {
            // inalcanzable: el @Pattern del DTO ya restringió el valor
            throw new SolicitudInvalidaException("Estrategia de notificación inválida: " + estrategia);
        }
    }

    private Persona personaDe(Jwt jwt) {
        return repositorioPersonas.findByKeycloakId(jwt.getSubject()).orElseGet(() -> provisionar(jwt));
    }

    private PerfilResponse respuesta(Persona persona, Jwt jwt, List<String> roles) {
        return mapper.toResponse(persona, jwt.getClaimAsString("preferred_username"), roles);
    }

    private Persona provisionar(Jwt jwt) {
        Persona nueva = new PersonaBuilder()
                .keycloakId(jwt.getSubject())
                .mail(jwt.getClaimAsString("email"))
                .nombre(nombreApellido(jwt))
                .fechaDeAlta(LocalDate.now())
                .build();

        try {
            return repositorioPersonas.saveAndFlush(nueva);
        } catch (DataIntegrityViolationException e) {
            return repositorioPersonas.findByKeycloakId(jwt.getSubject()).orElseThrow(() -> e);
        }
    }

    private String nombreApellido(Jwt jwt) {
        String name = jwt.getClaimAsString("name");
        if (name != null && !name.isBlank()) {
            return name;
        }

        String given = jwt.getClaimAsString("given_name");
        String family = jwt.getClaimAsString("family_name");
        return ((given == null ? "" : given) + " " + (family == null ? "" : family)).trim();
    }

}
