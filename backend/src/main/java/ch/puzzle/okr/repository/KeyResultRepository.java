package ch.puzzle.okr.repository;

import java.util.List;

import ch.puzzle.okr.models.keyresult.KeyResult;

import org.springframework.data.repository.CrudRepository;

public interface KeyResultRepository extends CrudRepository<KeyResult, Long> {
    List<KeyResult> findByObjectiveId(Long objectiveId);
}
