package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.FieldingStats;

public interface FieldingStatsRepository extends CrudRepository<FieldingStats, Long> {
}
