package tp.services.keycloak;

public record KeycloakUser(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled) {

}
