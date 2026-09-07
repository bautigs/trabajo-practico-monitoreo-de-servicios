package tp.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import tp.models.ranking.RankingService;

public class ProgramadorRanking {

  private final RankingService rankingService;

  public ProgramadorRanking(RankingService rankingService){
    this.rankingService = rankingService;
  }

  @Scheduled(cron = "0 14 19 ? * MON")
  public void ejecutar(){
    rankingService.rankear();
  }
}
