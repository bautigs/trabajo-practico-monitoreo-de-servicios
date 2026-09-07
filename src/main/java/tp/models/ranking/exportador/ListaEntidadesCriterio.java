package tp.models.ranking.exportador;

import lombok.Setter;
import tp.models.entidad.Entidad;

import java.util.List;

public class ListaEntidadesCriterio {


    String nombreDeCriterio;

    @Setter
    String prefijoArchivo;

    @Setter
    List<Entidad> listaEntidades;

    public ListaEntidadesCriterio(String unNombreCriterio){
        this.nombreDeCriterio = unNombreCriterio;
    }

}
