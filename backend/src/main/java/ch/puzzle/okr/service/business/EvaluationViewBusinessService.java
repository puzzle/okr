package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.service.persistence.EvaluationViewPersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationViewBusinessService {
    private final EvaluationViewPersistenceService evaluationViewPersistenceService;

    public EvaluationViewBusinessService(EvaluationViewPersistenceService evaluationViewPersistenceService) {
        this.evaluationViewPersistenceService = evaluationViewPersistenceService;
    }

    public List<EvaluationView> findByTeamIdsAndQuarterId(List<Long> teamIds, Long quarterId) {
        return evaluationViewPersistenceService.findByTeamIdsAndQuarterId(teamIds, quarterId);
    }
}
