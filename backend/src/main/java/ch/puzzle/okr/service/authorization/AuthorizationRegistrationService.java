package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.business.UserBusinessService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;

@Service
public class AuthorizationRegistrationService {

    private final UserBusinessService userBusinessService;
    private final TenantConfigProvider tenantConfigProvider;

    private final UserUpdateHelper helper = new UserUpdateHelper();

    public AuthorizationRegistrationService(UserBusinessService userBusinessService,
            TenantConfigProvider tenantConfigProvider) {
        this.userBusinessService = userBusinessService;
        this.tenantConfigProvider = tenantConfigProvider;
    }

    @Cacheable(value = AUTHORIZATION_USER_CACHE, key = "T(ch.puzzle.okr.SpringCachingConfig).cacheKey(#userFromToken)")
    public AuthorizationUser updateOrAddAuthorizationUser(User userFromToken) {
        User userFromDb = userBusinessService.getOrCreateUser(userFromToken);
        User userFromDbWithTokenData = setFirstLastNameFromToken(userFromDb, userFromToken);
        User userFromDbWithTokenAndPropertiesData = setOkrChampionFromProperties(userFromDbWithTokenData);
        userBusinessService.saveUser(userFromDbWithTokenAndPropertiesData);
        return new AuthorizationUser(userFromDbWithTokenAndPropertiesData);
    }

    // firstname and lastname comes from JWT token
    private User setFirstLastNameFromToken(User userFromDb, User userFromToken) {
        return helper.setFirstLastNameFromToken(userFromDb, userFromToken);
    }

    // okr champion is set in application properties
    private User setOkrChampionFromProperties(User user) {
        TenantConfigProvider.TenantConfig tenantConfig = this.tenantConfigProvider
                .getTenantConfigById(TenantContext.getCurrentTenant())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find tenant"));

        return helper.setOkrChampionFromProperties(user, tenantConfig);
    }

    public static class UserUpdateHelper {

        public User setOkrChampionFromProperties(User user, TenantConfigProvider.TenantConfig tenantConfig) {
            for (var mail : tenantConfig.okrChampionEmails()) {
                if (mail.trim().equals(user.getEmail())) {
                    user.setOkrChampion(true);
                }
            }
            return user;
        }

        public User setFirstLastNameFromToken(User userFromDb, User userFromToken) {
            userFromDb.setFirstName(userFromToken.getFirstName());
            userFromDb.setLastName(userFromToken.getLastName());
            return userFromDb;
        }
    }

}
