package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.repository.ActionRepository;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ActionValidationService extends ValidationBase<Action, Long, ActionRepository, ActionPersistenceService> {

    private final KeyResultValidationService keyResultValidationService;

    public ActionValidationService(ActionPersistenceService actionPersistenceService,
            KeyResultValidationService keyResultValidationService) {
        super(actionPersistenceService);
        this.keyResultValidationService = keyResultValidationService;
    }

    public void validateOnGetByKeyResultId(Long keyResultId) {
        keyResultValidationService.validateOnGet(keyResultId);
    }

    @Override
    public void validateOnCreate(Action model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());

        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Action model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenIdHasChanged(id, model.getId());
        Action savedAction = doesEntityExist(id);
        throwExceptionWhenKeyResultHasChanged(model, savedAction);
        validate(model);
    }

    void throwExceptionWhenKeyResultHasChanged(Action action, Action savedAction) {
        if (action.getKeyResult() == null || savedAction.getKeyResult() == null) {
            throw new ResponseStatusException(BAD_REQUEST,
                    format("KeyResult must not be null. action=%s, savedAction=%s", action, savedAction));
        }
        if (!Objects.equals(action.getKeyResult().getId(), savedAction.getKeyResult().getId())) {
            throw new ResponseStatusException(BAD_REQUEST,
                    format("Not allowed change the association to the key result (id = %s)", savedAction.getId()));
        }
    }
}
