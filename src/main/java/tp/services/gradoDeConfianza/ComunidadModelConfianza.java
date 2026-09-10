package tp.services.gradoDeConfianza;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ComunidadModelConfianza {
    private Long id;
    private String gradoDeConfianza;
    private Double puntosDeConfianza;
    private List<UsuarioModelConfianza> usuarios;
    private Boolean activo;
}
