package tests.usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import tp.dto.PerfilResponse;
import tp.dto.UsuarioDetalleResponse;
import tp.dto.UsuarioResumenResponse;
import tp.dto.UsuariosPageResponse;
import tp.models.builders.PersonaBuilder;
import tp.models.comunidad.Comunidad;
import tp.models.comunidad.Miembro;
import tp.models.comunidad.Persona;
import tp.repositories.RepositorioPersonas;
import tp.server.exceptions.RecursoNoEncontradoException;
import tp.server.exceptions.SolicitudInvalidaException;
import tp.services.keycloak.KeycloakAdminService;
import tp.services.keycloak.KeycloakRole;
import tp.services.keycloak.KeycloakUser;
import tp.services.usuarios.AdministracionUsuariosMapper;
import tp.services.usuarios.AdministracionUsuariosService;
import tp.services.usuarios.PerfilMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdministracionUsuariosServiceTest {

    @Mock
    KeycloakAdminService keycloakAdminService;

    @Mock
    RepositorioPersonas repositorioPersonas;

    @Spy
    AdministracionUsuariosMapper mapper = new AdministracionUsuariosMapper(new PerfilMapper());

    @InjectMocks
    AdministracionUsuariosService service;

    private KeycloakUser usuario(String id, String username, String email){
        return new KeycloakUser(id, username, email, "Nombre", "Apellido", true);
    }

    private Miembro miembroDe(String nombreComunidad){
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre(nombreComunidad);
        Miembro miembro = new Miembro();
        miembro.setComunidad(comunidad);
        return miembro;
    }

    @Test
    void listarSinRol_usaLaPaginacionYElConteoDeKeycloak(){
        when(keycloakAdminService.listarUsuarios("ana", 40, 20))
                .thenReturn(List.of(usuario("kc-1","ana","ana@x.com")));
        when(keycloakAdminService.contarUsuarios("ana")).thenReturn(45);
        when(keycloakAdminService.rolesDeRealm("kc-1")).thenReturn(List.of(new KeycloakRole("r1","BASICO")));
        when(repositorioPersonas.findByKeycloakId("kc-1")).thenReturn(Optional.empty());

        UsuariosPageResponse page = service.listarUsuarios("ana",null,2,20);

        assertThat(page.total()).isEqualTo(45);
        assertThat(page.pagina()).isEqualTo(2);
        assertThat(page.usuarios()).extracting(UsuarioResumenResponse::username).containsExactly("ana");
        verify(keycloakAdminService, never()).listarUsuarioPorRol(any());
    }

    @Test
    void listarCombinaLosDatosDeKeycloakConLaPersonaLocal(){
        Persona persona = new PersonaBuilder()
                .keycloakId("kc-1")
                .nombre("Ana Perez")
                .fechaDeAlta(LocalDate.of(2026,1,10))
                .membresias(Set.of(miembroDe("Vecinos Linea A"), miembroDe("Usuarios FC Mitre")))
                .build();
        when(keycloakAdminService.listarUsuarios(null,0,20))
                .thenReturn(List.of(usuario("kc-1", "ana", "ana@x.com")));
        when(keycloakAdminService.contarUsuarios(null)).thenReturn(1);
        when(keycloakAdminService.rolesDeRealm("kc-1"))
                .thenReturn(List.of(new KeycloakRole("r1", "ADMIN"), new KeycloakRole("r1", "BASICO")));
        when(repositorioPersonas.findByKeycloakId("kc-1")).thenReturn(Optional.of(persona));

        UsuarioResumenResponse resumen = service.listarUsuarios(null, null, 0, 20).usuarios().get(0);

        assertThat(resumen.email()).isEqualTo("ana@x.com");
        assertThat(resumen.roles()).containsExactly("ADMIN", "BASICO");
        assertThat(resumen.nombreApellido()).isEqualTo("Ana Perez");
        assertThat(resumen.cantidadComunidades()).isEqualTo(2);
    }

    @Test
    void listar_usuarioQueTodaviaNoAccedio_devuelveLosCamposDeDominioVacios(){
        when(keycloakAdminService.listarUsuarios(null, 0, 20))
                .thenReturn(List.of(usuario("kc-9", "nuevo", "nuevo@x.com")));
        when(keycloakAdminService.contarUsuarios(null)).thenReturn(1);
        when(repositorioPersonas.findByKeycloakId("kc-9")).thenReturn(Optional.empty());

        UsuarioResumenResponse resumen = service.listarUsuarios(null, null, 0, 20).usuarios().get(0);

        assertThat(resumen.username()).isEqualTo("nuevo");
        assertThat(resumen.email()).isEqualTo("nuevo@x.com");
        assertThat(resumen.nombreApellido()).isNull();
        assertThat(resumen.fechaDeAlta()).isNull();
        assertThat(resumen.cantidadComunidades()).isZero();
    }

    @Test
    void listarConRol_filtradoPorTextoSinDistinguirMayusculaYPaginaEnMemoria(){
        when(keycloakAdminService.listarUsuarioPorRol("BASICO")).thenReturn(List.of(
                usuario("kc-1", "ana", "ana@x.com"),
                usuario("kc-2", "juan", "juan@x.com"),
                usuario("kc-3","mariana", "mari@x.com")));
        when(keycloakAdminService.rolesDeRealm("kc-3")).thenReturn(List.of(new KeycloakRole("r1", "BASICO")));
        when(repositorioPersonas.findByKeycloakId("kc-3")).thenReturn(Optional.empty());

        UsuariosPageResponse page = service.listarUsuarios("ANA", "BASICO", 1,1 );

        assertThat(page.total()).isEqualTo(2);
        assertThat(page.usuarios()).extracting(UsuarioResumenResponse::username).containsExactly("mariana");
    }

    @Test
    void listarConRol_conPaginaFueraDeRango_DevuelveUsuariosVacios(){
        when(keycloakAdminService.listarUsuarioPorRol("BASICO")).thenReturn(List.of(
                usuario("kc-1", "ana", "ana@x.com"),
                usuario("kc-2", "juan", "juan@x.com"),
                usuario("kc-3","mariana", "mari@x.com")));

        UsuariosPageResponse page = service.listarUsuarios("ANA", "BASICO", 5,5 );

        assertThat(page.total()).isEqualTo(2);
        assertThat(page.usuarios()).isEqualTo(List.of());

    }

    @Test
    void detalle_usuarioInexistenteEnKeycloak_lanza404() throws Exception{
        when(keycloakAdminService.obtenerUsuario("no-existe")).thenThrow(
                HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", HttpHeaders.EMPTY, null, null)
        );

        assertThatThrownBy(() -> service.detalleUsuario("no-existe"))
                .isInstanceOf(RecursoNoEncontradoException.class);
        verifyNoInteractions(repositorioPersonas);
    }

    @Test
    void detalle_usuarioExistente_DevuelveUsarioDetalleResponse() {
        Persona persona = new PersonaBuilder()
                .keycloakId("kc-1")
                .nombre("Ana Perez")
                .fechaDeAlta(LocalDate.of(2026,1,10))
                .membresias(Set.of(miembroDe("Vecinos Linea A"), miembroDe("Usuarios FC Mitre")))
                .build();

        when(keycloakAdminService.obtenerUsuario("kc-1"))
                .thenReturn(new KeycloakUser("kc-1", "ana", "ana@x.com", "Ana", "Perez", true));
        when(repositorioPersonas.findByKeycloakId("kc-1"))
                .thenReturn(Optional.of(persona));
        UsuarioDetalleResponse usuarioDetalleResponse = service.detalleUsuario("kc-1");
        assertThat(usuarioDetalleResponse.comunidades()).extracting(UsuarioDetalleResponse.ComunidadResumen::nombre).containsExactlyInAnyOrder("Vecinos Linea A", "Usuarios FC Mitre");
    }

    @Test
    void detalle_sinPersonaLocal_devuelveSoloLosDatosDeKeycloak(){
        when(keycloakAdminService.obtenerUsuario("kc-9"))
                .thenReturn(usuario("kc-9", "nuevo", "nuevo@x.com"));
        when(repositorioPersonas.findByKeycloakId("kc-9")).thenReturn(Optional.empty());

        UsuarioDetalleResponse detalle = service.detalleUsuario("kc-9");

        assertThat(detalle.username()).isEqualTo("nuevo");
        assertThat(detalle.nombreApellido()).isNull();
        assertThat(detalle.mailDeContacto()).isNull();
        assertThat(detalle.estrategiaNotificacion()).isNull();
        assertThat(detalle.ubicacion()).isNull();
        assertThat(detalle.tiposDeServicioDeInteres()).isEmpty();
        assertThat(detalle.entidadesDeInteres()).isEmpty();
        assertThat(detalle.comunidades()).isEmpty();
    }

    @Test
    void listarConRolInexistente_lanzaSolicitudInvalida(){
        assertThatThrownBy(() -> service.listarUsuarios(null, "FOO", 0, 20))
                .isInstanceOf(SolicitudInvalidaException.class);
        verifyNoInteractions(keycloakAdminService);
    }
}
