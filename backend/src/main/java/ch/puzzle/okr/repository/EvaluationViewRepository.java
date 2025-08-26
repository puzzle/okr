package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EvaluationViewRepository extends CrudRepository<EvaluationView, EvaluationViewId> {

    List<EvaluationView> findByTeamIdAndQuarterId(Long teamId, Long quarterId);
}
