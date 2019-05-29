package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.BattingStats;
import worldcup.model.BowlingStats;
import worldcup.model.Match;
import worldcup.model.Player;

import java.util.Optional;

public interface BowlingStatsRepository extends CrudRepository<BowlingStats, Long> {

    boolean existsByMatchAndRunsGreaterThanEqualAndRunsLessThanEqual(
            Match match, int lowerBound, int upperBound);

    Optional<BowlingStats> findByMatchAndPlayer(Match match, Player player);
}
