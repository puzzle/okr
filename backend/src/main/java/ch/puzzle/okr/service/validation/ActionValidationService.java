package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class ActionValidationService extends ValidationBase<Action, Long> {

    public ActionValidationService(ActionPersistenceService actionPersistenceService) {
        super(actionPersistenceService);
    }

    @Override
    public void validateOnCreate(Action model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());

        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Action model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenIdHasChanged(id, model.getId());

        doesEntityExist(id);
        validate(model);
    }
}
