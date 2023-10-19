package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class CheckInAuthorizationService {
    private final CheckInBusinessService checkInBusinessService;
    private final AuthorizationService authorizationService;

    public CheckInAuthorizationService(CheckInBusinessService checkInBusinessService,
            AuthorizationService authorizationService) {
        this.checkInBusinessService = checkInBusinessService;
        this.authorizationService = authorizationService;
    }

    public CheckIn getCheckInById(Long id, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleReadByCheckInId(id, authorizationUser);
        return checkInBusinessService.getCheckInById(id);
    }

    public CheckIn createCheckIn(CheckIn checkIn, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleCreateOrUpdate(checkIn, authorizationUser);
        return checkInBusinessService.createCheckIn(checkIn, authorizationUser);
    }

    public CheckIn updateCheckIn(Long id, CheckIn checkIn, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleCreateOrUpdate(checkIn, authorizationUser);
        return checkInBusinessService.updateCheckIn(id, checkIn);
    }

    public void deleteCheckInById(Long id, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleDeleteByCheckInId(id, authorizationUser);
        checkInBusinessService.deleteCheckInById(id);
    }
}
