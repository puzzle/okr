package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import org.springframework.stereotype.Service;

@Service
public class CheckInAuthorizationService extends AuthorizationServiceBase<Long, CheckIn, CheckInBusinessService> {

    public CheckInAuthorizationService(CheckInBusinessService checkInBusinessService,
            AuthorizationService authorizationService) {
        super(checkInBusinessService, authorizationService);
    }

    @Override
    protected void hasRoleReadById(Long id, AuthorizationUser authorizationUser) {
        getAuthorizationService().hasRoleReadByCheckInId(id, authorizationUser);
    }

    @Override
    protected void hasRoleCreateOrUpdate(CheckIn entity, AuthorizationUser authorizationUser) {
        getAuthorizationService().hasRoleCreateOrUpdate(entity, authorizationUser);
    }

    @Override
    protected void hasRoleDeleteById(Long id, AuthorizationUser authorizationUser) {
        getAuthorizationService().hasRoleDeleteByCheckInId(id, authorizationUser);
    }
}
