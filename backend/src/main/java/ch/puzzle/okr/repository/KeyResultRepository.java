package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.keyresult.KeyResult;
import java.util.List;

public interface KeyResultRepository extends DeleteRepository<KeyResult, Long> {
    List<KeyResult> findByObjectiveIdAndIsDeletedFalse(Long objectiveId);
}
