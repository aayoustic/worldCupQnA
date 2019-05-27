package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.Player;

public interface PlayerRepository extends CrudRepository<Player, Long> {
}
