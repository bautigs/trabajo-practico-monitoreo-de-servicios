package tp.server.exceptions;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class); 

    @ExceptionHandler(KeycloakNoDisponibleException.class)
    public ResponseEntity<ApiError> dependenciaCaida(KeycloakNoDisponibleException e){
        return procesarExcepcion(HttpStatus.SERVICE_UNAVAILABLE,"No pudimos procesar la solicitud en este momento. Reintentá en unos minutos",e,true);
    
    }

    private ResponseEntity<ApiError> procesarExcepcion(HttpStatus status, String mensaje, Exception e, boolean printStackTrace){
        String traceId = UUID.randomUUID().toString();

        if(printStackTrace){
            log.error("[{}] {} -> {}",traceId,status.value(),e.toString(),e);
        } else {
            log.warn("[{}] {} -> {}", traceId, status.value(),e.toString());
        }

        return ResponseEntity.status(status)
                            .body(new ApiError(mensaje,traceId,Instant.now()));
    }
}