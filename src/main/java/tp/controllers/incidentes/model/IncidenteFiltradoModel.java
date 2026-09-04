package tp.controllers.incidentes.model;

import lombok.Builder;
import lombok.Getter;
import tp.models.entities.servicios.Estado;

@Builder
@Getter
public class IncidenteFiltradoModel {
    private String descripcion;
    private String fechaApertura;
    private Estado estado;

    public Boolean isAbierto(){
        return this.getEstado().toString().equals("ABIERTO");
    }

}
