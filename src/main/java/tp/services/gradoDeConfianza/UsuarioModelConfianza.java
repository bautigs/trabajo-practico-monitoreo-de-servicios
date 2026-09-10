package tp.services.gradoDeConfianza;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsuarioModelConfianza {
    private Long id;
    private Double puntosDeConfianza;
    private String gradoDeConfianza;
    private Boolean activo;
}
