package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.ParticipantBattingStats;

public interface ParticipantBattingStatsRepository extends CrudRepository<ParticipantBattingStats, Long> {
}
