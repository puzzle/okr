package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface EvaluationViewRepository extends CrudRepository<EvaluationView, EvaluationViewId> {

    List<EvaluationView> findByTeamIdInAndQuarterId(List<Long> teamIds, Long quarterId);
}
