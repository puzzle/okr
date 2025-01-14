package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Completed;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface CompletedRepository extends CrudRepository<Completed, Long> {
    Optional<Completed> findByObjectiveId(Long objectiveId);
}
