package tp.models.services.gradoDeConfianza;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RequestComunidad {
    private List<ComunidadModelConfianza> comunidades;
}
