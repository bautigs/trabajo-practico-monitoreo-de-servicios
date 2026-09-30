package tp.services.usuarios;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import tp.dto.UsuarioDetalleResponse;
import tp.dto.UsuarioResumenResponse;
import tp.dto.UsuariosPageResponse;
import tp.models.comunidad.Persona;
import tp.models.comunidad.RolPersona;
import tp.repositories.RepositorioPersonas;
import tp.server.exceptions.RecursoNoEncontradoException;
import tp.server.exceptions.SolicitudInvalidaException;
import tp.services.keycloak.KeycloakAdminService;
import tp.services.keycloak.KeycloakRole;
import tp.services.keycloak.KeycloakUser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AdministracionUsuariosService {
    private final KeycloakAdminService keycloakAdminService;
    private final RepositorioPersonas repositorioPersonas;
    private final AdministracionUsuariosMapper mapper;

    private static final Set<String> ROLES_VALIDOS = Arrays.stream(RolPersona.values()).map(Enum::name).collect(Collectors.toUnmodifiableSet());

    public AdministracionUsuariosService(KeycloakAdminService keycloakAdminService, RepositorioPersonas repositorioPersonas, AdministracionUsuariosMapper mapper) {
        this.keycloakAdminService = keycloakAdminService;
        this.repositorioPersonas = repositorioPersonas;
        this.mapper = mapper;
    }

    public UsuariosPageResponse listarUsuarios(String texto, String rol, int pagina, int tamano){
        List<KeycloakUser> usuariosDeLaPagina;
        long total;

        if(rol != null && !rol.isBlank()){
            if(ROLES_VALIDOS.contains(rol)){
                List<KeycloakUser> usuariosDelRol =
                        keycloakAdminService.listarUsuarioPorRol(rol).stream()
                                .filter(u -> coincideTexto(u,texto))
                                .toList();
                total = usuariosDelRol.size();
                usuariosDeLaPagina = paginar(usuariosDelRol, pagina, tamano);
            } else {
                throw new SolicitudInvalidaException("Rol inválido " + rol + ". Valores posibles: " + ROLES_VALIDOS);
            }

        } else {
            usuariosDeLaPagina = keycloakAdminService.listarUsuarios(texto, pagina*tamano, tamano);
            total = keycloakAdminService.contarUsuarios(texto);
        }

        List<UsuarioResumenResponse> usuarios = usuariosDeLaPagina.stream().map(this::aResumen).toList();
        return new UsuariosPageResponse(usuarios,pagina,tamano,total);
    }

    public UsuarioDetalleResponse detalleUsuario(String keycloakId) {
        KeycloakUser keycloakUser;
        try {
            keycloakUser = keycloakAdminService.obtenerUsuario(keycloakId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNoEncontradoException("No existe un usuario con id " + keycloakId);
        }

        Persona persona = repositorioPersonas.findByKeycloakId(keycloakId).orElse(null);
        return mapper.aDetalle(keycloakUser, rolesDe(keycloakId), persona);
    }


    private UsuarioResumenResponse aResumen(KeycloakUser user){
        Persona persona = repositorioPersonas.findByKeycloakId(user.id()).orElse(null);
        return this.mapper.aResumen(user, rolesDe(user.id()), persona);
    }

    private List<String> rolesDe(String id){
        return keycloakAdminService.rolesDeRealm(id).stream().map(KeycloakRole::name).toList();
    }

    private boolean coincideTexto(KeycloakUser user, String texto){
        if(texto == null || texto.isBlank()){
            return true;
        }

        String needle = texto.toLowerCase();
        return contiene(user.username(), needle) || contiene(user.email(), needle) || contiene(user.firstName(), needle) || contiene(user.lastName(), needle);
    }

    private boolean contiene(String valor, String needle){
        return valor != null && valor.toLowerCase().contains(needle);
    }

    private <T> List<T> paginar(List<T> lista, int pagina, int tamano){
        int desde = Math.min(pagina * tamano, lista.size());
        int hasta = Math.min(desde + tamano, lista.size());
        return lista.subList(desde, hasta);
    }
}
