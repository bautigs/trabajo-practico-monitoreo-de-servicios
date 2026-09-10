package tp.services.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;

import tp.server.exceptions.KeycloakNoDisponibleException;

import java.time.Instant;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KeycloakTokenProvider {

  private final RestClient restClient;
  private final KeycloakAdminProperties properties;

  private String cachedToken;
  private Instant expiresAt = Instant.EPOCH;

  public KeycloakTokenProvider(RestClient keycloakRestClient, KeycloakAdminProperties properties) {
    this.restClient = keycloakRestClient;
    this.properties = properties;
  }

  public synchronized String accessToken() {
    if (Instant.now().isBefore(expiresAt)) {
      return cachedToken;
    }

    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

    form.add("grant_type", "password");
    form.add("client_id", properties.clientId());
    form.add("client_secret", properties.clientSecret());
    form.add("username", properties.username());
    form.add("password", properties.password());

    TokenResponse response = restClient.post()
        .uri("/realms/{realm}/protocol/openid-connect/token", properties.realm())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(form)
        .retrieve()
        .body(TokenResponse.class);

    if (response == null) {
      throw new KeycloakNoDisponibleException("Respuesta de token inválido de keycloak");
    }

    this.cachedToken = response.accessToken();
    this.expiresAt = Instant.now().plusSeconds(response.expiresIn() - 30);

    return cachedToken;

  }
}
