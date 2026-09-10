package tp.services.fusionDeComunidades;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@Builder
public class PropuestaDeFusionModel {
    private ArrayList<ComunidadModel> comunidadesAFusionar;

    public String fechaPropuesta;
}
