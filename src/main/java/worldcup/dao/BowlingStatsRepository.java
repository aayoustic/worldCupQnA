package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.BowlingStats;

public interface BowlingStatsRepository extends CrudRepository<BowlingStats, Long> {
}
