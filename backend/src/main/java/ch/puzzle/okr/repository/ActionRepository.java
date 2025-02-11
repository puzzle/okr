package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Action;
import java.util.List;

import ch.puzzle.okr.models.Objective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ActionRepository extends DeleteRepository<Action, Long> {
//    @Query("select Action from Action a where ")
//    TODO rename this
    List<Action> getActionsByKeyResultIdAndIsDeletedFalseOrderByPriorityAsc(Long keyResultId);
}
