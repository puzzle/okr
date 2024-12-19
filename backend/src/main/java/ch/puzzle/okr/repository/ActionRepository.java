package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Action;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ActionRepository extends CrudRepository<Action, Long> {
    List<Action> getActionsByKeyResultIdOrderByPriorityAsc(Long keyResultId);
}
