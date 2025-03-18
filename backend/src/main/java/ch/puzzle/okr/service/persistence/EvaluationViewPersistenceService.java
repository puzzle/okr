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

    public List<EvaluationView> findByTeamIdsAndQuarterId(List<Long> teamIds, Long quarterId)
            throws OkrResponseStatusException {
        List<EvaluationViewId> list = teamIds.stream().map(teamId -> new EvaluationViewId(teamId, quarterId)).toList();
        return iteratorToList(getRepository().findAllById(list));
    }

    @Override
    public String getModelName() {
        return "EvaluationView";
    }
}
