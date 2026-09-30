package tp.controllers.usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import tp.dto.PerfilResponse;
import tp.dto.UsuarioDetalleResponse;
import tp.dto.UsuarioResumenResponse;
import tp.dto.UsuariosPageResponse;
import tp.server.exceptions.RecursoNoEncontradoException;
import tp.server.exceptions.SolicitudInvalidaException;
import tp.server.security.SecurityConfig;
import tp.services.usuarios.AdministracionUsuariosService;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

@WebMvcTest(AdministracionUsuariosController.class)
@Import(SecurityConfig.class)
public class AdministracionUsuariosControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdministracionUsuariosService administracionUsuariosService;

    @MockBean
    JwtDecoder jwtDecoder;

    private static SimpleGrantedAuthority rol(String rol){
        return new SimpleGrantedAuthority("ROLE_" + rol);
    }

    @Test
    void listar_comoAdmin_devuelveLaPaginaDeUsuarios() throws Exception{
        UsuariosPageResponse pagina = new UsuariosPageResponse(
                List.of(new UsuarioResumenResponse(
                        "kc-1", "ana", "ana@x.com", true, List.of("BASICO"),
                        "Ana Perez", LocalDate.of(2026, 1, 10),2)),
                0, 20, 1);

        when(administracionUsuariosService.listarUsuarios(any(), any(), anyInt(), anyInt()))
                .thenReturn(pagina);

        mockMvc.perform(get("/usuarios").with(jwt().authorities(rol("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.usuarios[0].username").value("ana"))
                .andExpect(jsonPath("$.usuarios[0].roles[0]").value("BASICO"))
                .andExpect(jsonPath("$.usuarios[0].cantidadComunidades").value(2));
    }

    @Test
    void listar_sinParametros_usaPagina0YTamano20() throws Exception {
        when(administracionUsuariosService.listarUsuarios(any(), any(), anyInt(), anyInt()))
                .thenReturn(new UsuariosPageResponse(List.of(), 0, 20, 0));

        mockMvc.perform(get("/usuarios").with(jwt().authorities(rol("ADMIN"))))
                .andExpect(status().isOk());

        verify(administracionUsuariosService).listarUsuarios(null, null, 0, 20);
    }

    @Test
    void listar_pasaLosFiltrosYLaPaginacionAlService() throws Exception{
        when(administracionUsuariosService.listarUsuarios(any(), any(), anyInt(), anyInt()))
                .thenReturn(new UsuariosPageResponse(List.of(), 2, 5, 0));

        mockMvc.perform(get("/usuarios")
                        .param("texto","ana")
                        .param("rol","BASICO")
                        .param("pagina", "2")
                        .param("tamano", "5")
                        .with(jwt().authorities(rol("ADMIN"))))
                .andExpect(status().isOk());

        verify(administracionUsuariosService).listarUsuarios("ana","BASICO",2,5);
    }

    @Test
    void listarconRolInexistente_devuelve404() throws Exception {
        when(administracionUsuariosService.listarUsuarios(any(), eq("FOO"), anyInt(), anyInt()))
                .thenThrow(SolicitudInvalidaException.class);

        mockMvc.perform(get("/usuarios")
                        .param("rol","FOO")
                        .param("pagina", "0")
                        .param("tamano", "20")
                        .with(jwt().authorities(rol("ADMIN"))))
                .andExpect(status().isBadRequest());

        verify(administracionUsuariosService).listarUsuarios(null, "FOO", 0, 20);
    }

    @Test
    void listar_conPaginaNegativa_devuelve400() throws Exception {
        mockMvc.perform(get("/usuarios")
                        .param("pagina", "-1")
                        .with(jwt().authorities(rol("ADMIN"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.pagina").exists());

        verifyNoInteractions(administracionUsuariosService);
    }

    @Test
    void listar_conTamanoFueraDeRango_devuelve400() throws Exception {
        mockMvc.perform(get("/usuarios")
                        .param("tamano", "0")
                        .with(jwt().authorities(rol("ADMIN"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.tamano").exists());

        mockMvc.perform(get("/usuarios")
                        .param("tamano", "101")
                        .with(jwt().authorities(rol("ADMIN"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.tamano").exists());

        verifyNoInteractions(administracionUsuariosService);
    }

    @Test
    void listar_comoBasico_devuelve403() throws Exception{
        mockMvc.perform(get("/usuarios").with(jwt().authorities(rol("BASICO"))))
                .andExpect(status().isForbidden());
        verifyNoInteractions(administracionUsuariosService);
    }

    @Test
    void listar_comoResponsable_devuelve403() throws Exception{
        mockMvc.perform(get("/usuarios").with(jwt().authorities(rol("REPSONSABLE"))))
                .andExpect(status().isForbidden());
        verifyNoInteractions(administracionUsuariosService);
    }

    @Test
    void listar_sinToken_devuelve401() throws Exception{
        mockMvc.perform(get("/usuarios")).andExpect(status().isUnauthorized());
    }

    @Test
    void detalle_comoAdmin_devuelveUsuario() throws Exception{
        when(administracionUsuariosService.detalleUsuario("kc-1"))
                .thenReturn(new UsuarioDetalleResponse(
                        "kc-1",
                        "ana",
                        "ana@x.com",
                        true,
                        List.of("BASICO"),
                        "Ana Perez",
                        "ana@x.com",
                        "1234",
                        "MAIL",
                        "ASINCRONICO",
                        new PerfilResponse.Ubicacion(1L,1L,1L),
                        List.of(1L),
                        List.of(1L),
                        LocalDate.of(2026,1,10),
                        List.of(new UsuarioDetalleResponse.ComunidadResumen(1L, "Comunidad"))
                ));

        mockMvc.perform(get("/usuarios/kc-1").with(jwt().authorities(rol("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ana"));
    }


    @Test
    void detalle_usuarioInexistente_devuelve400() throws Exception{
        when(administracionUsuariosService.detalleUsuario("no-existe"))
                .thenThrow(new RecursoNoEncontradoException("No existe un usuario con id no-existe"));

        mockMvc.perform(get("/usuarios/no-existe").with(jwt().authorities(rol("ADMIN"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void detalle_comoBasico_devuelve403() throws Exception{
        mockMvc.perform(get("/usuarios/kc-1").with(jwt().authorities(rol("BASICO"))))
                .andExpect(status().isForbidden());

        verifyNoInteractions(administracionUsuariosService);
    }



}
