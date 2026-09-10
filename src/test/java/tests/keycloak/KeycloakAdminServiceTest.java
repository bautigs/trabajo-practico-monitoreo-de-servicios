package tests.keycloak;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import tp.services.keycloak.KeycloakAdminProperties;
import tp.services.keycloak.KeycloakAdminService;
import tp.services.keycloak.KeycloakTokenProvider;
import tp.services.keycloak.KeycloakUser;

class KeycloakAdminServiceTest {

  private MockRestServiceServer server;
  private KeycloakAdminService service;

  @BeforeEach
  void setUp() {
    RestClient.Builder builder = RestClient.builder().baseUrl("http://kc");
    server = MockRestServiceServer.bindTo(builder).build();
    RestClient client = builder.build();

    KeycloakAdminProperties props = new KeycloakAdminProperties(
        "http://kc", "monitoreo-servicios", "monitoreo-backend", "s3cr3t", "svc-backend", "svc-pass");

    KeycloakTokenProvider tokenProvider = Mockito.mock(KeycloakTokenProvider.class);
    Mockito.when(tokenProvider.accessToken()).thenReturn("fake-token");

    service = new KeycloakAdminService(client, tokenProvider, props);

  }

  @Test
  void listarUsuarios_devuelveLaNominaConElBearer() {
    server.expect(requestTo("http://kc/admin/realms/monitoreo-servicios/users?first=0&max=20"))
        .andExpect(header("Authorization", "Bearer fake-token"))
        .andRespond(withSuccess("""
            [{"id":"1","username":"admin","email":"a@x","firstName":"Admin","lastName":"D","enabled":true}]
            """, MediaType.APPLICATION_JSON));

    List<KeycloakUser> usuarios = service.listarUsuarios(null, 0, 20);

    assertThat(usuarios).extracting(KeycloakUser::username).containsExactly("admin");
    server.verify();
  }
}
