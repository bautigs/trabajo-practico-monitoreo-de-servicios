package tp.server.exceptions;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        String mensaje,
        String traceId,
        Instant timestamp,
        Map<String, String> errores) {

    public ApiError(String mensaje, String traceId, Instant timestamp) {
        this(mensaje, traceId, timestamp, null);
    }

}
