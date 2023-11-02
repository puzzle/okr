package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
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

    public void createEntities(List<Action> actionList) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        actionList.forEach(action -> hasRoleCreateOrUpdate(action, authorizationUser));
        actionBusinessService.createEntities(actionList);
    }

    public void updateEntities(List<Action> actionList) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        actionList.forEach(action -> {
            hasRoleCreateOrUpdate(action, authorizationUser);
            setRoleCreateOrUpdate(action, authorizationUser);
        });
        actionBusinessService.updateEntities(actionList);
    }

    protected void hasRoleCreateOrUpdate(Action entity, AuthorizationUser authorizationUser) {
        authorizationService.hasRoleCreateOrUpdate(entity.getKeyResult(), authorizationUser);
    }

    public void deleteActionByActionId(Long actionId) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        authorizationService.hasRoleDeleteByActionId(actionId, authorizationUser);
        actionBusinessService.deleteEntityById(actionId);
    }

    private void setRoleCreateOrUpdate(Action entity, AuthorizationUser authorizationUser) {
        entity.setWriteable(isWriteable(entity, authorizationUser));
    }

    protected boolean isWriteable(Action entity, AuthorizationUser authorizationUser) {
        return authorizationService.isWriteable(entity.getKeyResult(), authorizationUser);
    }
}
