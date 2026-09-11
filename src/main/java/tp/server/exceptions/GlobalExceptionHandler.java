package tp.server.exceptions;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacion(MethodArgumentNotValidException e) {
        Map<String, String> errores = new LinkedHashMap<>();

        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            errores.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }

        String traceId = UUID.randomUUID().toString();
        log.warn("[{}] 400 -> validacion {}", traceId, errores);
        return ResponseEntity.badRequest()
                .body(new ApiError("La solicitud tiene campos inválidos", traceId, Instant.now(), errores));
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    public ResponseEntity<ApiError> solicitudInvalida(SolicitudInvalidaException e) {
        return procesarExcepcion(HttpStatus.BAD_REQUEST, e.getMessage(), e, false);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> noEncontrado(RecursoNoEncontradoException e) {
        return procesarExcepcion(HttpStatus.NOT_FOUND, e.getMessage(), e, false);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> accesoDenegado(AccessDeniedException e) {
        return procesarExcepcion(HttpStatus.FORBIDDEN, "No tenés permisos para realizar esta acción", e, false);
    }

    @ExceptionHandler(KeycloakNoDisponibleException.class)
    public ResponseEntity<ApiError> dependenciaCaida(KeycloakNoDisponibleException e) {
        return procesarExcepcion(HttpStatus.SERVICE_UNAVAILABLE,
                "No pudimos procesar la solicitud en este momento. Reintentá en unos minutos", e, true);

    }

    private ResponseEntity<ApiError> procesarExcepcion(HttpStatus status, String mensaje, Exception e,
            boolean printStackTrace) {
        String traceId = UUID.randomUUID().toString();

        if (printStackTrace) {
            log.error("[{}] {} -> {}", traceId, status.value(), e.toString(), e);
        } else {
            log.warn("[{}] {} -> {}", traceId, status.value(), e.toString());
        }

        return ResponseEntity.status(status)
                .body(new ApiError(mensaje, traceId, Instant.now()));
    }
}