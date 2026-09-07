package tp.models.ranking;

import tp.models.entidad.Entidad;
import java.util.List;

public interface Criterio {
  public void generarReporte(List<Entidad> entidades);
}

