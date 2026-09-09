package tp.models.services.keycloak;

public record KeycloakUser(
    String id,
    String username,
    String email,
    String firstName,
    String lastName,
    boolean enabled
) {

}
