package tp.services.usuarios;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import tp.dto.PerfilResponse;
import tp.models.comunidad.Asincronico;
import tp.models.comunidad.ConfiguracionRecepcion;
import tp.models.comunidad.Persona;
import tp.models.comunidad.Sincronico;
import tp.models.entidad.Entidad;
import tp.models.notificador.EstrategiaDeNotificacion;
import tp.models.notificador.mail.EstrategiaDeMail;
import tp.models.notificador.wpp.EstrategiaDeWPP;
import tp.models.persistencia.Persistente;
import tp.models.servicios.TipoServicio;

@Component
public class PerfilMapper {
    public PerfilResponse toResponse(Persona p, String username, List<String> roles) {
        return new PerfilResponse(
                p.getKeycloakId(),
                username,
                p.getNombreApellido(),
                p.getMailDeContacto(),
                p.getNumeroDeContacto(),
                estrategia(p.getEstrategia()),
                configuracion(p.getConfiguracion()),
                new PerfilResponse.Ubicacion(
                        p.getProvincia() == null ? null : p.getProvincia().getId(),
                        p.getMunicipio() == null ? null : p.getMunicipio().getId(),
                        p.getLocalidad() == null ? null : p.getLocalidad().getId()),
                ids(p.getTiposDeServiciosDeInteres(), TipoServicio::getId),
                ids(p.getEntidadesDeInteres(), Entidad::getId),
                roles,
                p.getFechaDeAlta());
    }

    public <T extends Persistente> List<Long> ids(List<T> lista, Function<T, Long> extractor) {
        return lista == null ? List.of() : lista.stream().map(extractor).toList();
    }

    public String estrategia(EstrategiaDeNotificacion e) {
        if (e instanceof EstrategiaDeWPP) {
            return "WHATSAPP";
        }
        if (e instanceof EstrategiaDeMail) {
            return "MAIL";
        }
        return null;
    }

    public String configuracion(ConfiguracionRecepcion c) {
        if (c instanceof Asincronico) {
            return "ASINCRONICO";
        }
        if (c instanceof Sincronico) {
            return "SINCRONICO";
        }
        return null;
    }

}
