package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.Participant;

import java.util.Optional;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {

    Optional<Participant> findByEmail(String email);
}
