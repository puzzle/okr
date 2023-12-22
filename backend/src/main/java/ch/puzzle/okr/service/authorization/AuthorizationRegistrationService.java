package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;

@Service
public class AuthorizationRegistrationService {

    private final UserBusinessService userBusinessService;

    public AuthorizationRegistrationService(UserBusinessService userBusinessService) {
        this.userBusinessService = userBusinessService;
    }

    @Cacheable(value = AUTHORIZATION_USER_CACHE, key = "#user.email")
    public AuthorizationUser registerAuthorizationUser(User user) {
        return new AuthorizationUser(userBusinessService.getOrCreateUser(user));
    }
}
