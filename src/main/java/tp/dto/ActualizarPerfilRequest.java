package tp.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Edición parcial del perfil: un campo en null significa "no lo toques".
 * Para vaciar una lista de intereses hay que mandarla explícitamente como [].
 * nombreApellido y mailDeContacto NO se editan acá: son identidad de Keycloak.
 */

public record ActualizarPerfilRequest(
        @Size(max = 30, message = "El número de contacto no puede superar los 30 caracteres") String numeroDeContacto,

        @Pattern(regexp = "MAIL|WHATSAPP", message = "estrategiaNotificacion debe ser MAIL o WHATSAPP") String estrategiaNotificacion,

        @Pattern(regexp = "SINCRONICO|ASINCRONICO", message = "configuracionRecepcion debe ser SINCRONICO o ASINCRONICO") String configuracionRecepcion,

        List<@NotNull(message = "Los ids de tipo de servicio no pueden ser null") Long> tiposDeServicioDeInteres,

        List<@NotNull(message = "Los ids de tipo de servicio no pueden ser null") Long> entidadesInteres) {

}
