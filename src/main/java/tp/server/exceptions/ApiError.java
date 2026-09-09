package tp.server.exceptions;

import java.time.Instant;

public record ApiError(
    String mensaje,
    String traceId,
    Instant timestamp
) {

}
