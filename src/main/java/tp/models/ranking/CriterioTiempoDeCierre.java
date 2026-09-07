package tp.models.ranking;

import lombok.Getter;
import tp.models.entidad.Entidad;
import tp.models.ranking.exportador.Exportador;
import tp.models.ranking.exportador.ListaEntidadesCriterio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CriterioTiempoDeCierre implements Criterio {

  @Getter
  List<Entidad> ultimaListaCriterioTiempo = new ArrayList<>();
  public void generarReporte(List<Entidad> entidades) {
    Collections.sort(entidades, Comparator.comparingInt(Entidad::promedioDeCierreDeIncidentes).reversed());
    ListaEntidadesCriterio listaEntidadesCriterio = new ListaEntidadesCriterio("Criterio Tiempo de Cierre");
    listaEntidadesCriterio.setListaEntidades(entidades);
    listaEntidadesCriterio.setPrefijoArchivo("ListadoTiempoDeCierre_");
    Exportador.getInstancia().agregarListaEntidadesCriterio(listaEntidadesCriterio);
    ultimaListaCriterioTiempo = entidades;
  }
}