package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

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

    public List<CheckIn> getAllCheckInsByKeyResult(long keyResultId, Jwt token) {
        AuthorizationUser authorizationUser = getAuthorizationService().getAuthorizationUser(token);
        getAuthorizationService().hasRoleReadByKeyResultId(keyResultId, authorizationUser);
        return getBusinessService().getAllCheckInsByKeyResult(keyResultId);
    }

    public boolean isImUsed(Long id, KeyResult keyResult) {
        return getBusinessService().isImUsed(id, keyResult);
    }
}
