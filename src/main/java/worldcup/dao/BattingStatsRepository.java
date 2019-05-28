package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.BattingStats;

public interface BattingStatsRepository extends CrudRepository<BattingStats, Long> {
}
