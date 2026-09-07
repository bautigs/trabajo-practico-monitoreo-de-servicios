package tp.models.services.fusionDeComunidades;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class ComunidadModel {
    private Long id;
    private String nombre;
    private List<MiembroModel> miembros;
    private List<EstablecimientoModel> establecimientosObservados;
    private List<ServicioModel> serviciosObservados;
    private String gradoDeConfianza;

}
