package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Action;
import java.util.List;

public interface ActionRepository extends DeleteRepository<Action, Long> {
    // @Query("select Action from Action a where ")
    // TODO rename this
    List<Action> getActionsByKeyResultIdAndIsDeletedFalseOrderByPriorityAsc(Long keyResultId);
}
