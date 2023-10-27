package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
