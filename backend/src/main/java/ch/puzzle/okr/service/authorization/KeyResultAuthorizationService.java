package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultWithActionList;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class KeyResultAuthorizationService extends AuthorizationServiceBase<Long, KeyResult, KeyResultBusinessService> {
    public KeyResultAuthorizationService(KeyResultBusinessService keyResultBusinessService,
            AuthorizationService authorizationService) {
        super(keyResultBusinessService, authorizationService);
    }

    @Override
    protected void hasRoleReadById(Long id, AuthorizationUser authorizationUser) {
        getAuthorizationService().hasRoleReadByKeyResultId(id, authorizationUser);
    }

    @Override
    protected void hasRoleCreateOrUpdate(KeyResult entity, AuthorizationUser authorizationUser) {
        getAuthorizationService().hasRoleCreateOrUpdate(entity, authorizationUser);
    }

    @Override
    protected void hasRoleDeleteById(Long id, AuthorizationUser authorizationUser) {
        getAuthorizationService().hasRoleDeleteByKeyResultId(id, authorizationUser);
    }

    @Override
    protected boolean isWriteable(KeyResult entity, AuthorizationUser authorizationUser) {
        return getAuthorizationService().isWriteable(entity, authorizationUser);
    }

    public List<CheckIn> getAllCheckInsByKeyResult(Long keyResultId) {
        AuthorizationUser authorizationUser = getAuthorizationService().getAuthorizationUser();
        getAuthorizationService().hasRoleReadByKeyResultId(keyResultId, authorizationUser);
        List<CheckIn> checkIns = getBusinessService().getAllCheckInsByKeyResult(keyResultId);
        setRoleCreateOrUpdateCheckIn(checkIns, authorizationUser);
        return checkIns;
    }

    @Override
    public KeyResult updateEntity(Long id, KeyResult keyResult) {
        throw new ResponseStatusException(BAD_REQUEST,
                "unsupported method in class " + getClass().getSimpleName() + ", use updateEntities() instead");
    }

    public KeyResultWithActionList updateEntities(Long id, KeyResult entity, List<Action> actionList) {
        AuthorizationUser authorizationUser = getAuthorizationService().getAuthorizationUser();
        hasRoleCreateOrUpdate(entity, authorizationUser);
        KeyResultWithActionList updatedEntities = getBusinessService().updateEntities(id, entity, actionList);
        updatedEntities.keyResult().setWriteable(true);
        updatedEntities.actionList().forEach(action -> action.setWriteable(true));
        return updatedEntities;
    }

    public boolean isImUsed(Long id, KeyResult keyResult) {
        return getBusinessService().isImUsed(id, keyResult);
    }

    private void setRoleCreateOrUpdateCheckIn(List<CheckIn> checkIns, AuthorizationUser authorizationUser) {
        if (!CollectionUtils.isEmpty(checkIns)) {
            boolean isWriteable = getAuthorizationService().isWriteable(checkIns.get(0), authorizationUser);
            checkIns.forEach(c -> c.setWriteable(isWriteable));
        }
    }
}
