package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.Participant;
import worldcup.model.TeamStatsPoints;

import java.util.Optional;

public interface TeamStatsPointRepository extends CrudRepository<TeamStatsPoints, Long> {

    Optional<TeamStatsPoints> findByParticipant(Participant participant);
}
