package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyResultAuthorizationService {
    private final KeyResultBusinessService keyResultBusinessService;
    private final AuthorizationService authorizationService;

    public KeyResultAuthorizationService(KeyResultBusinessService keyResultBusinessService,
            AuthorizationService authorizationService) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.authorizationService = authorizationService;
    }

    public KeyResult createKeyResult(KeyResult keyResult, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleCreateOrUpdate(keyResult, authorizationUser);
        return keyResultBusinessService.createKeyResult(keyResult, authorizationUser);
    }

    public KeyResult getKeyResultById(Long id, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleReadByKeyResultId(id, authorizationUser);
        return keyResultBusinessService.getKeyResultById(id);
    }

    public KeyResult updateKeyResult(Long id, KeyResult keyResult, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleCreateOrUpdate(keyResult, authorizationUser);
        return keyResultBusinessService.updateKeyResult(id, keyResult);
    }

    public void deleteKeyResultById(Long id, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleDeleteByKeyResultId(id, authorizationUser);
        keyResultBusinessService.deleteKeyResultById(id);
    }

    public List<CheckIn> getAllCheckInsByKeyResult(long keyResultId, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleReadByKeyResultId(keyResultId, authorizationUser);
        return keyResultBusinessService.getAllCheckInsByKeyResult(keyResultId);
    }

    public boolean isImUsed(Long id, KeyResult keyResult) {
        return keyResultBusinessService.isImUsed(id, keyResult);
    }
}
