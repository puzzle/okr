package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthorizationService {
    private final UserBusinessService userBusinessService;
    private final AuthorizationService authorizationService;

    public UserAuthorizationService(UserBusinessService userBusinessService,
            AuthorizationService authorizationService) {
        this.userBusinessService = userBusinessService;
        this.authorizationService = authorizationService;
    }

    public List<User> getAllUsers() {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        return userBusinessService.getAllUsers();
    }
}
