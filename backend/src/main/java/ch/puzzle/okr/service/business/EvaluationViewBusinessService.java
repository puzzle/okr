package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import ch.puzzle.okr.service.persistence.EvaluationViewPersistenceService;
import ch.puzzle.okr.service.validation.EvaluationViewValidationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationViewBusinessService {
    private final EvaluationViewPersistenceService evaluationViewPersistenceService;
    private final EvaluationViewValidationService evaluationViewValidationService;

    public EvaluationViewBusinessService(EvaluationViewPersistenceService evaluationViewPersistenceService,
                                         EvaluationViewValidationService evaluationViewValidationService) {
        this.evaluationViewPersistenceService = evaluationViewPersistenceService;
        this.evaluationViewValidationService = evaluationViewValidationService;
    }

    public List<EvaluationView> findByIds(List<EvaluationViewId> ids) {
        evaluationViewValidationService.validateOnGet(ids);
        return evaluationViewPersistenceService.findByTeamIdsAndQuarterId(ids);
    }
}
