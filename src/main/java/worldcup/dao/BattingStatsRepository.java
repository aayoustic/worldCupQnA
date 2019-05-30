package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.BattingStats;
import worldcup.model.Match;
import worldcup.model.Player;

import java.util.List;
import java.util.Optional;

public interface BattingStatsRepository extends CrudRepository<BattingStats, Long> {

    List<BattingStats> findByMatch(Match match);

    Optional<BattingStats> findByMatchAndPlayer(Match match, Player player);
}
