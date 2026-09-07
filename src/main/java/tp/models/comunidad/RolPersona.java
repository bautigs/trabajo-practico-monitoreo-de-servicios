package tp.models.comunidad;

import io.javalin.security.RouteRole;

public enum RolPersona implements RouteRole {
    ADMIN,
    BASICO,
    RESPONSABLE
}
