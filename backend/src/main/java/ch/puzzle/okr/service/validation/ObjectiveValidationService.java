package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.State;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static ch.puzzle.okr.Constants.*;
import static ch.puzzle.okr.service.validation.QuarterValidationService.throwExceptionWhenStartEndDateQuarterIsNull;

@Service
public class ObjectiveValidationService
        extends ValidationBase<Objective, Long, ObjectiveRepository, ObjectivePersistenceService> {

    public ObjectiveValidationService(ObjectivePersistenceService objectivePersistenceService) {
        super(objectivePersistenceService);
    }

    @Override
    public void validateOnCreate(Objective model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        throwExceptionWhenModifiedByIsSet(model);
        throwExceptionWhenStartEndDateQuarterIsNull(model.getQuarter());
        throwExceptionWhenNotDraftInBacklogQuarter(model);
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Objective model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenIdHasChanged(id, model.getId());
        throwExceptionWhenModifiedByIsNull(model);
        throwExceptionWhenStartEndDateQuarterIsNull(model.getQuarter());
        throwExceptionWhenNotDraftInBacklogQuarter(model);
        Objective savedObjective = doesEntityExist(id);
        throwExceptionWhenTeamHasChanged(model.getTeam(), savedObjective.getTeam());
        validate(model);
    }

    private void throwExceptionWhenModifiedByIsSet(Objective model) {
        if (model.getModifiedBy() != null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_SET_FORBIDDEN,
                    List.of("ModifiedBy", model.getModifiedBy()));
        }
    }

    private void throwExceptionWhenModifiedByIsNull(Objective model) {
        if (model.getModifiedBy() == null) {
            throw new OkrResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorKey.ATTRIBUTE_NOT_SET,
                    "modifiedBy");
        }
    }

    private void throwExceptionWhenTeamHasChanged(Team team, Team savedTeam) {
        if (!Objects.equals(team, savedTeam)) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_CANNOT_CHANGE,
                    List.of(TEAM, OBJECTIVE));
        }
    }

    private void throwExceptionWhenNotDraftInBacklogQuarter(Objective model) {
        if (isInvalidBacklogObjective(model)) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_MUST_BE_DRAFT,
                    List.of(OBJECTIVE, STATE_DRAFT, model.getState()));
        }
    }

    private boolean isInvalidBacklogObjective(Objective model) {
        return model.getQuarter().getLabel().equals(BACK_LOG_QUARTER_LABEL) //
                && model.getQuarter().getStartDate() == null //
                && model.getQuarter().getEndDate() == null //
                && (model.getState() != State.DRAFT);
    }
}
