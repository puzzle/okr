package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.ActionBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionAuthorizationService {

    private final ActionBusinessService actionBusinessService;
    private final AuthorizationService authorizationService;

    public ActionAuthorizationService(ActionBusinessService actionBusinessService,
            AuthorizationService authorizationService) {
        this.actionBusinessService = actionBusinessService;
        this.authorizationService = authorizationService;
    }

    public List<Action> getActionsByKeyResult(KeyResult keyResult) {
        List<Action> actionList = actionBusinessService.getActionsByKeyResultId(keyResult.getId());
        actionList.forEach(action -> action.setWriteable(keyResult.isWriteable()));
        return actionList;
    }

    public List<Action> createEntities(List<Action> actionList) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        actionList.forEach(action -> hasRoleCreateOrUpdate(action, authorizationUser));
        List<Action> savedActions = actionBusinessService.createEntities(actionList);
        savedActions.forEach(action -> action.setWriteable(true));
        return savedActions;
    }

    public List<Action> updateEntities(List<Action> actionList) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        actionList.forEach(action -> hasRoleCreateOrUpdate(action, authorizationUser));
        List<Action> updatedActions = actionBusinessService.updateEntities(actionList);
        updatedActions.forEach(action -> action.setWriteable(true));
        return updatedActions;
    }

    public void deleteActionByActionId(Long actionId) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        authorizationService.hasRoleDeleteByActionId(actionId, authorizationUser);
        actionBusinessService.deleteEntityById(actionId);
    }

    private void hasRoleCreateOrUpdate(Action entity, AuthorizationUser authorizationUser) {
        authorizationService.hasRoleCreateOrUpdate(entity.getKeyResult(), authorizationUser);
    }
}
