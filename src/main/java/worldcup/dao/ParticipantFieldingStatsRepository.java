package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.ParticipantFieldingStats;

public interface ParticipantFieldingStatsRepository extends CrudRepository<ParticipantFieldingStats, Long> {
}
