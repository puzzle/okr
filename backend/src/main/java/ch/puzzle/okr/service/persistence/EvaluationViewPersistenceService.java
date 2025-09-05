package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import ch.puzzle.okr.repository.EvaluationViewRepository;
import java.util.List;

import ch.puzzle.okr.util.TeamQuarterFilter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class EvaluationViewPersistenceService
        extends
            PersistenceBase<EvaluationView, EvaluationViewId, EvaluationViewRepository> {
    protected EvaluationViewPersistenceService(CrudRepository<EvaluationView, EvaluationViewId> repository) {
        super(repository);
    }

    public List<EvaluationView> findByIds(TeamQuarterFilter filter) {
        return getRepository().findByTeamIdInAndQuarterId(filter.teamIds(), filter.quarterId());
    }

    @Override
    public String getModelName() {
        return "EvaluationView";
    }
}
