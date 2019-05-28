package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.Match;

public interface MatchRepository extends CrudRepository<Match, Long> {
}
