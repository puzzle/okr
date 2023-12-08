package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import ch.puzzle.okr.service.validation.ActionValidationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        validator.validateOnGetByKeyResultId(keyResultId);
        return actionPersistenceService.getActionsByKeyResultIdOrderByPriorityAsc(keyResultId);
    }

    public Action getEntityById(Long id) {
        validator.validateOnGet(id);
        return actionPersistenceService.findById(id);
    }

    @Transactional
    public List<Action> createEntities(List<Action> actionList) {
        return actionList.stream().map(this::createEntity).toList();
    }

    @Transactional
    public Action createEntity(Action action) {
        validator.validateOnCreate(action);
        return actionPersistenceService.save(action);
    }

    @Transactional
    public List<Action> updateEntities(List<Action> actionList) {
        List<Action> savedActions = new ArrayList<>();
        actionList.forEach(action -> {
            if (action.getKeyResult() == null) {
                action.setKeyResult(actionPersistenceService.findById(action.getId()).getKeyResult());
            }
            if (action.getId() == null) {
                savedActions.add(createEntity(action));
            } else {
                savedActions.add(updateEntity(action.getId(), action));
            }
        });
        return savedActions;
    }

    @Transactional
    public Action updateEntity(Long id, Action action) {
        validator.validateOnUpdate(id, action);
        return actionPersistenceService.save(action);
    }

    @Transactional
    public void deleteEntityById(Long id) {
        validator.validateOnDelete(id);
        actionPersistenceService.deleteById(id);
    }

    @Transactional
    public void deleteEntitiesByKeyResultId(Long keyResultId) {
        getActionsByKeyResultId(keyResultId).forEach(action -> deleteEntityById(action.getId()));
    }
}
