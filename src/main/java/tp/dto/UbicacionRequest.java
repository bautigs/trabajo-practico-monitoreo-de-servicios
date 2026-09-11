package tp.dto;

import jakarta.validation.constraints.NotNull;

public record UbicacionRequest(
                @NotNull(message = "provinciaId es obligatorio") Long provinciaId,

                Long municipioId,

                Long localidadId) {
}
