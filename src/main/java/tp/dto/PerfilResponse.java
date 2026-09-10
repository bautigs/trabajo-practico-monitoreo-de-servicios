package tp.dto;

import java.time.LocalDate;
import java.util.List;

public record PerfilResponse(
                String keycloakId,
                String username,
                String nombreApellido,
                String mailDeContacto,
                String numeroDeContacto,
                String estrategiaNotificacion,
                String configuracionRecepcion,
                Ubicacion ubicacion,
                List<Long> tiposDeServicioDeInteres,
                List<Long> entidadesDeInteres,
                List<String> roles,
                LocalDate fechaDeAlta) {

        public record Ubicacion(
                        Long provinciaId,
                        Long municipioId,
                        Long localidadId) {
        }
}
