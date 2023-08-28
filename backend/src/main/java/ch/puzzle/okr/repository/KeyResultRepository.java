package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.keyResult.KeyResult;
import ch.puzzle.okr.models.Objective;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeyResultRepository extends CrudRepository<KeyResult, Long> {
    List<KeyResult> findByObjectiveId(@Param("objective_id") Long objectiveId);

    List<KeyResult> findByObjectiveOrderByModifiedOnDesc(@Param("objective") Objective objective);
}
