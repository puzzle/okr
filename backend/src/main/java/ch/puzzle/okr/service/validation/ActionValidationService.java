package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.MessageKey;
import ch.puzzle.okr.repository.ActionRepository;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, MessageKey.ATTRIBUTE_NOT_NULL,
                    List.of(Constants.KEY_RESULT, Constants.ACTION));
        }

        if (!Objects.equals(action.getKeyResult().getId(), savedAction.getKeyResult().getId())) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_CANNOT_CHANGE,
                    List.of(Constants.KEY_RESULT, Constants.ACTION));
        }
    }
}
