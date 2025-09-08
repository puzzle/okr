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
    private final TeamValidationService teamValidationService;

    EvaluationViewValidationService(EvaluationViewPersistenceService persistenceService,
                                    QuarterValidationService quarterValidationService,
                                    TeamValidationService teamValidationService) {
        super(persistenceService);
        this.quarterValidationService = quarterValidationService;
        this.teamValidationService = teamValidationService;
    }

    public void validateOnGet(TeamQuarterFilter filter) {
        if (filter.quarterId() == null || filter.teamIds().isEmpty()) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, "Es muss mindestens 1 Team und 1 Quartal ausgewählt sein");
        }
        filter.teamIds().forEach(teamValidationService::validateOnGet);
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
