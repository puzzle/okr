package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import ch.puzzle.okr.repository.EvaluationViewRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationViewPersistenceService
        extends PersistenceBase<EvaluationView, EvaluationViewId, EvaluationViewRepository> {
    protected EvaluationViewPersistenceService(CrudRepository<EvaluationView, EvaluationViewId> repository) {
        super(repository);
    }

    public List<EvaluationView> findByTeamIdsAndQuarterId(List<EvaluationViewId> ids) {
        return iteratorToList(getRepository().findAllById(ids));
    }

    @Override
    public String getModelName() {
        return "EvaluationView";
    }
}
