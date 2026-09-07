package tp.models.services.gradoDeConfianza;

import lombok.Builder;

@Builder
public class IncidenteModelConfianza {
    private Long id;
    private Long idUsuAp;
    private Long idUsuCi;
    private String  horaApertura;
    private String horaCierre;
    private String lugar;
    private Boolean activo;
}
