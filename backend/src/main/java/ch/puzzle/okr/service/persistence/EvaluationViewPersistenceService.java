package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import ch.puzzle.okr.repository.EvaluationViewRepository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class EvaluationViewPersistenceService
        extends
            PersistenceBase<EvaluationView, EvaluationViewId, EvaluationViewRepository> {
    protected EvaluationViewPersistenceService(CrudRepository<EvaluationView, EvaluationViewId> repository) {
        super(repository);
    }

    public List<EvaluationView> findByIds(List<EvaluationViewId> ids) {
        return iteratorToList(getRepository().findAllById(ids));
    }

    @Override
    public String getModelName() {
        return "EvaluationView";
    }
}
