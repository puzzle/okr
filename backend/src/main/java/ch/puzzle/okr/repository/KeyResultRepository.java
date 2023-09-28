package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.keyresult.KeyResult;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KeyResultRepository extends CrudRepository<KeyResult, Long> {
    List<KeyResult> findByObjectiveId(Long objectiveId);
}
