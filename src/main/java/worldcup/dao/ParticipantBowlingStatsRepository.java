package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.ParticipantBowlingStats;

public interface ParticipantBowlingStatsRepository extends CrudRepository<ParticipantBowlingStats, Long> {
}
