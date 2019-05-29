package worldcup.dao;

import org.springframework.data.repository.CrudRepository;
import worldcup.model.PredictionPoints;

public interface PredictionPointsRepository extends CrudRepository<PredictionPoints, Long> {
}
