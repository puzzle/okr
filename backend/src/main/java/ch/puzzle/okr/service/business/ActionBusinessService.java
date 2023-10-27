package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import ch.puzzle.okr.service.validation.ActionValidationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ActionBusinessService {
    private final ActionPersistenceService actionPersistenceService;
    private final ActionValidationService validator;

    public ActionBusinessService(ActionPersistenceService actionPersistenceService, ActionValidationService validator) {
        this.actionPersistenceService = actionPersistenceService;
        this.validator = validator;
    }

    public List<Action> getActionsByKeyResultId(Long keyResultId) {
        return actionPersistenceService.getActionsByKeyResultIdOrderByPriorityAsc(keyResultId);
    }

    @Transactional
    public void createActions(KeyResult keyResult, List<Action> actionList) {
        actionList.forEach(action -> {
            action.setKeyResult(keyResult);
            this.createAction(action);
        });
    }

    @Transactional
    public void createAction(Action action) {
        validator.validateOnCreate(action);
        actionPersistenceService.save(action);
    }

    @Transactional
    public void updateActions(KeyResult keyResult, List<Action> actionList) {
        actionList.forEach(action -> {
            if (keyResult == null) {
                action.setKeyResult(actionPersistenceService.findById(action.getId()).getKeyResult());
            } else {
                action.setKeyResult(keyResult);
            }
            if (action.getId() == null) {
                this.createAction(action);
            } else {
                this.updateAction(action);
            }
        });
    }

    @Transactional
    public void updateAction(Action action) {
        validator.validateOnUpdate(action.getId(), action);
        actionPersistenceService.save(action);
    }

    @Transactional
    public void deleteActionById(Long id) {
        validator.validateOnDelete(id);
        actionPersistenceService.deleteById(id);
    }
}
