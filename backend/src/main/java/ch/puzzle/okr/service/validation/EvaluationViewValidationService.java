package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.repository.EvaluationViewRepository;
import ch.puzzle.okr.service.persistence.EvaluationViewPersistenceService;
import ch.puzzle.okr.util.TeamQuarterFilter;
import java.sql.RowId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EvaluationViewValidationService
        extends
            ValidationBase<EvaluationView, RowId, EvaluationViewRepository, EvaluationViewPersistenceService> {

    private final QuarterValidationService quarterValidationService;

    EvaluationViewValidationService(EvaluationViewPersistenceService persistenceService,
                                    QuarterValidationService quarterValidationService) {
        super(persistenceService);
        this.quarterValidationService = quarterValidationService;
    }

    public void validateOnGet(TeamQuarterFilter filter) {
        if (filter.quarterId() == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                                                 "Es muss mindestens 1 Quartal ausgewählt sein");
        }
        quarterValidationService.validateOnGet(filter.quarterId());
    }

    @Override
    public void validateOnCreate(EvaluationView model) {
        throw new UnsupportedOperationException("EvaluationView is for get Operations only.");
    }

    @Override
    public void validateOnUpdate(RowId id, EvaluationView model) {
        throw new UnsupportedOperationException("EvaluationView is for get Operations only.");
    }
}
