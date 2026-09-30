package tp.dto;

import java.time.LocalDate;
import java.util.List;

public record UsuarioResumenResponse(
        String keycloakId,
        String username,
        String email,
        boolean habilitado,
        List<String> roles,
        String nombreApellido,
        LocalDate fechaDeAlta,
        int cantidadComunidades
) {
}
