package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.BattingStats;
import worldcup.model.BowlingStats;
import worldcup.model.Match;
import worldcup.model.Player;

import java.util.List;
import java.util.Optional;

public interface BowlingStatsRepository extends CrudRepository<BowlingStats, Long> {

    List<BowlingStats> findByMatch(Match match);

    Optional<BowlingStats> findByMatchAndPlayer(Match match, Player player);
}
