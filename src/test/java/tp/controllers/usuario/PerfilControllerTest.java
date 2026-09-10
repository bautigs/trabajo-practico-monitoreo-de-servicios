package tp.controllers.usuario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import tp.dto.PerfilResponse;
import tp.server.security.SecurityConfig;
import tp.services.usuarios.PerfilService;

// @WebMvcTest carga solo la capa web (controllers, filtros, MockMvc, Jackson,
// @ControllerAdvice) y NO services/repos/JPA. Con la clase se restringe a este
// controller. Necesita descubrir un @SpringBootConfiguration subiendo por el
// arbol de paquetes -> lo encuentra en tp.TestApplicationConfig.
@WebMvcTest(PerfilController.class)
// @WebMvcTest autoconfigura Security con la config por defecto; importamos la nuestra.
@Import(SecurityConfig.class)
class PerfilControllerTest {

  // Simula requests HTTP sin abrir socket ni Tomcat. Lo autoconfigura @WebMvcTest.
  @Autowired
  MockMvc mockMvc;

  // Mock de Mockito puesto en el contexto de Spring; el controller lo recibe por inyeccion.
  @MockBean
  PerfilService perfilService;

  // SecurityConfig arma un JwtDecoder desde issuer-uri (llamada de red a Keycloak
  // al arrancar el contexto). Con este mock la config se wirea sin salir a la red;
  // el post-processor jwt() de abajo saltea la validacion real.
  @MockBean
  JwtDecoder jwtDecoder;

  @Test
  void perfil_devuelveElDtoDelUsuarioAutenticado() throws Exception {
    when(perfilService.perfilActual(any(), anyList()))
        .thenReturn(new PerfilResponse(
            "kc-1", "ana", "Ana Perez", "ana@x.com", null, "MAIL", "SINCRONICO",
            new PerfilResponse.Ubicacion(null, null, null),
            List.of(), List.of(), List.of("BASICO"), LocalDate.now()));

    mockMvc.perform(get("/perfil")
            .with(jwt()
                .jwt(j -> j.claim("preferred_username", "ana"))
                .authorities(new SimpleGrantedAuthority("ROLE_BASICO"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("ana"))
        .andExpect(jsonPath("$.roles[0]").value("BASICO"));
  }

  @Test
  void sinToken_devuelve401() throws Exception {
    mockMvc.perform(get("/perfil")).andExpect(status().isUnauthorized());
  }
}
