package tp.services.usuarios;

import org.springframework.stereotype.Component;
import tp.dto.PerfilResponse;
import tp.dto.UsuarioDetalleResponse;
import tp.dto.UsuarioResumenResponse;
import tp.models.comunidad.Persona;
import tp.models.entidad.Entidad;
import tp.models.servicios.TipoServicio;
import tp.services.keycloak.KeycloakUser;

import java.util.List;

@Component
public class AdministracionUsuariosMapper {
    private final PerfilMapper perfilMapper;

    public AdministracionUsuariosMapper(PerfilMapper perfilMapper) {
        this.perfilMapper = perfilMapper;
    }

    public UsuarioResumenResponse aResumen(KeycloakUser user, List<String> roles, Persona persona){
        return new UsuarioResumenResponse(
                user.id(),
                user.username(),
                user.email(),
                user.enabled(),
                roles,
                persona == null ? null : persona.getNombreApellido(),
                persona == null ? null : persona.getFechaDeAlta(),
                persona == null ? 0 : persona.getComunidades().size()
        );
    }

    public UsuarioDetalleResponse aDetalle(KeycloakUser u, List<String> roles, Persona persona) {
        return new UsuarioDetalleResponse(
                u.id(),
                u.username(),
                u.email(),
                u.enabled(),
                roles,
                persona == null ? null : persona.getNombreApellido(),
                persona == null ? null : persona.getMailDeContacto(),
                persona == null ? null : persona.getNumeroDeContacto(),
                persona == null ? null : perfilMapper.estrategia(persona.getEstrategia()),
                persona == null ? null : perfilMapper.configuracion(persona.getConfiguracion()),
                persona == null ? null : new PerfilResponse.Ubicacion(
                        persona.getProvincia() == null ? null : persona.getProvincia().getId(),
                        persona.getMunicipio() == null ? null : persona.getMunicipio().getId(),
                        persona.getLocalidad() == null ? null : persona.getLocalidad().getId()),
                persona == null ? List.of()
                        : perfilMapper.ids(persona.getTiposDeServiciosDeInteres(), TipoServicio::getId),
                persona == null ? List.of() : perfilMapper.ids(persona.getEntidadesDeInteres(), Entidad::getId),
                persona == null ? null : persona.getFechaDeAlta(),
                persona == null ? List.of()
                        : persona.getComunidades().stream()
                        .map(c -> new UsuarioDetalleResponse.ComunidadResumen(c.getId(), c.getNombre()))
                        .toList());
    }
}

