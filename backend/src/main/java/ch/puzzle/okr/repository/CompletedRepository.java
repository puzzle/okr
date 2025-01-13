package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Completed;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompletedRepository extends CrudRepository<Completed, Long> {
    Optional<Completed> findByObjectiveId(Long objectiveId);
}
