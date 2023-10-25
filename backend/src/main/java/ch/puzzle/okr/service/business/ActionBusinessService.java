package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Action;
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
    public void createActions(List<Action> actionList) {
        actionList.forEach(this::createAction);
    }

    @Transactional
    public void createAction(Action action) {
        validator.validateOnCreate(action);
        actionPersistenceService.save(action);
    }

    @Transactional
    public Action updateAction(Long id, Action action) {
        validator.validateOnUpdate(id, action);
        return actionPersistenceService.save(action);
    }

    @Transactional
    public void deleteActionById(Long id) {
        validator.validateOnDelete(id);
        actionPersistenceService.deleteById(id);
    }
}
