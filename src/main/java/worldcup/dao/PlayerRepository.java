package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.Player;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Long> {

    Optional<Player> findByName(String name);
}
