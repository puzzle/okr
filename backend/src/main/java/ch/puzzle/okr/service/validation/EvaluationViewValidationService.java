package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import ch.puzzle.okr.repository.EvaluationViewRepository;
import ch.puzzle.okr.service.persistence.EvaluationViewPersistenceService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EvaluationViewValidationService
        extends
            ValidationBase<EvaluationView, EvaluationViewId, EvaluationViewRepository, EvaluationViewPersistenceService> {

    private final QuarterValidationService quarterValidationService;
    private final TeamValidationService teamValidationService;

    EvaluationViewValidationService(EvaluationViewPersistenceService persistenceService,
                                    QuarterValidationService quarterValidationService,
                                    TeamValidationService teamValidationService) {
        super(persistenceService);
        this.quarterValidationService = quarterValidationService;
        this.teamValidationService = teamValidationService;
    }

    public void validateOnGet(List<EvaluationViewId> ids) {
        if (ids.isEmpty()) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, "Es muss mindestens 1 Team angewählt werden");
        }
        ids.forEach(id -> teamValidationService.validateOnGet(id.getTeamId()));
        quarterValidationService.validateOnGet(ids.getLast().getQuarterId());
    }

    @Override
    public void validateOnCreate(EvaluationView model) {
        throw new UnsupportedOperationException("EvaluationView is for get Operations only.");
    }

    @Override
    public void validateOnUpdate(EvaluationViewId evaluationViewId, EvaluationView model) {
        throw new UnsupportedOperationException("EvaluationView is for get Operations only.");
    }
}
