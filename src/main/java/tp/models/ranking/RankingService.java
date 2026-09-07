package tp.models.ranking;

  import lombok.Getter;
  import tp.repositories.RepositorioEntidades;
  import java.util.List;

public class RankingService {

  @Getter
  private List<Criterio> criteriosParaRanking;

  public void rankear(){
    //this.getCriteriosParaRanking().forEach(unCriterio->unCriterio.generarReporte(RepositorioEntidades.getInstancia().all()));
  }
}
