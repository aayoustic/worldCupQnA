package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.FieldingStats;
import worldcup.model.Match;
import worldcup.model.Player;

import java.util.Optional;

public interface FieldingStatsRepository extends CrudRepository<FieldingStats, Long> {

    Optional<FieldingStats> findByMatchAndPlayer(Match match, Player player);
}
