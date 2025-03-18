package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import org.springframework.data.repository.CrudRepository;


public interface EvaluationViewRepository extends CrudRepository<EvaluationView, EvaluationViewId> {}
