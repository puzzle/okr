package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.repository.ActionRepository;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import org.springframework.stereotype.Service;

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

        doesEntityExist(id);
        validate(model);
    }
}
