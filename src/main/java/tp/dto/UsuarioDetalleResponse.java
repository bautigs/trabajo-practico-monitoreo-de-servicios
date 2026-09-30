package tp.dto;

import java.time.LocalDate;
import java.util.List;

public record UsuarioDetalleResponse(
        String keycloakId,
        String username,
        String email,
        boolean habilitado,
        List<String> roles,
        String nombreApellido,
        String mailDeContacto,
        String numeroDeContacto,
        String estrategiaNotificacion,
        String configuracionRecepcion,
        PerfilResponse.Ubicacion ubicacion,
        List<Long> tiposDeServicioDeInteres,
        List<Long> entidadesDeInteres,
        LocalDate fechaDeAlta,
        List<ComunidadResumen> comunidades
) {
    public record ComunidadResumen(Long id, String nombre) {
    }
}
