package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.evaluation.EvaluationView;

import java.sql.RowId;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface EvaluationViewRepository extends CrudRepository<EvaluationView, RowId> {

    List<EvaluationView> findByTeamIdInAndQuarterId(List<Long> teamIds, Long quarterId);
}
