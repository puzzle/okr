package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Action;
import java.util.List;

public interface ActionRepository extends DeleteRepository<Action, Long> {
    List<Action> getActionsByKeyResultIdAndIsDeletedFalseOrderByPriorityAsc(Long keyResultId);
}
