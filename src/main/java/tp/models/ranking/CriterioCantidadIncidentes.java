package tp.models.ranking;

import lombok.Getter;
import tp.models.entidad.Entidad;
import tp.models.ranking.exportador.Exportador;
import tp.models.ranking.exportador.ListaEntidadesCriterio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CriterioCantidadIncidentes implements Criterio {

  @Getter
  List<Entidad> ultimaListaCriterioCantidad = new ArrayList<>();
  @Override
  public void generarReporte(List<Entidad> entidades) {
    Collections.sort(entidades, Comparator.comparingInt(Entidad::getCantReportesParaRanking).reversed());
    ListaEntidadesCriterio listaEntidadesCriterio = new ListaEntidadesCriterio("Criterio Cantidad de Incidentes");
    listaEntidadesCriterio.setListaEntidades(entidades);
    listaEntidadesCriterio.setPrefijoArchivo("ListadoCantidadIncidentes");
    Exportador.getInstancia().agregarListaEntidadesCriterio(listaEntidadesCriterio);
    ultimaListaCriterioCantidad = entidades;
  }
}