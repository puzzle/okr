package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeyResultRepository extends CrudRepository<KeyResult, Long> {
    List<KeyResult> findByObjective(@Param("objective") Objective objective);
}
