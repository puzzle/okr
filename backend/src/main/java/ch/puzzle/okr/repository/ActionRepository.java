package ch.puzzle.okr.repository;

import java.util.List;

import ch.puzzle.okr.models.Action;

import org.springframework.data.repository.CrudRepository;

public interface ActionRepository extends CrudRepository<Action, Long> {
    List<Action> getActionsByKeyResultIdOrderByPriorityAsc(Long keyResultId);
}
