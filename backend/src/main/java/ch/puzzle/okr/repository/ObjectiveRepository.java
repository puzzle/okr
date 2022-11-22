package ch.puzzle.okr.repository;

import java.util.List;
import ch.puzzle.okr.models.Objective;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveRepository extends CrudRepository<Objective, Long> {
    List<Objective> findByTeamId(long id);
}
