package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.keyresult.KeyResult;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface KeyResultRepository extends DeleteRepository<KeyResult, Long> {
    List<KeyResult> findByObjectiveId(Long objectiveId);
}
