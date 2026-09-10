package tp.services.keycloak;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class KeycloakAdminService {

    private final RestClient restClient;
    private final KeycloakTokenProvider tokenProvider;
    private final KeycloakAdminProperties properties;

    public KeycloakAdminService(RestClient restClient, KeycloakTokenProvider tokenProvider,
            KeycloakAdminProperties properties) {
        this.restClient = restClient;
        this.tokenProvider = tokenProvider;
        this.properties = properties;
    }

    public List<KeycloakUser> listarUsuarios(String search, int first, int max) {
        KeycloakUser[] users = restClient.get()
                .uri(uri -> uri.path("/admin/realms/{realm}/users")
                        .queryParam("first", first)
                        .queryParam("max", max)
                        .queryParamIfPresent("search", Optional.ofNullable(search))
                        .build(properties.realm()))
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .retrieve()
                .body(KeycloakUser[].class);

        return users == null ? List.of() : Arrays.asList(users);

    }

    public int contarUsuarios(String search) {
        Integer total = restClient.get()
                .uri(uri -> uri.path("/admin/realms/{realm}/users/count")
                        .queryParamIfPresent("search", Optional.ofNullable(search))
                        .build(properties.realm()))
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .retrieve()
                .body(Integer.class);

        return total == null ? 0 : total;
    }

    public KeycloakUser obtenerUsuario(String keycloakId) {
        return restClient.get()
                .uri("/admin/realms/{realm}/users/{id}", properties.realm(), keycloakId)
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .retrieve()
                .body(KeycloakUser.class);

    }

    public List<KeycloakRole> rolesDeRealm(String keycloakId) {
        KeycloakRole[] roles = restClient.get()
                .uri("/admin/realms/{realm}/users/{id}/role-mappings/realm", properties.realm(), keycloakId)
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .retrieve()
                .body(KeycloakRole[].class);

        return roles == null ? List.of() : Arrays.asList(roles);
    }

    private String bearer() {
        return "Bearer " + tokenProvider.accessToken();
    }

}
