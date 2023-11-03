package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Action;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActionRepository extends CrudRepository<Action, Long> {
    List<Action> getActionsByKeyResultIdOrderByPriorityAsc(Long keyResultId);
}
