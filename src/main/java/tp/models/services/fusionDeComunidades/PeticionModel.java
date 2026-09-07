package tp.models.services.fusionDeComunidades;

import lombok.Builder;

import java.util.List;
@Builder
public class PeticionModel {

    private List<PropuestaDeFusionModel> propuestas;
    private List<ComunidadModel> comunidades;

}
