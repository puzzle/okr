package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Action;
import java.util.List;

import ch.puzzle.okr.models.Objective;
import org.springframework.data.repository.CrudRepository;

public interface ActionRepository extends DeleteRepository<Action, Long> {
    List<Action> getActionsByKeyResultIdOrderByPriorityAsc(Long keyResultId);
}
