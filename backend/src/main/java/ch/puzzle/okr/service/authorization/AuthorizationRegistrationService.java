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

    public AuthorizationRegistrationService(UserBusinessService userBusinessService,
            TenantConfigProvider tenantConfigProvider) {
        this.userBusinessService = userBusinessService;
        this.tenantConfigProvider = tenantConfigProvider;
    }

    @Cacheable(value = AUTHORIZATION_USER_CACHE, key = "T(ch.puzzle.okr.SpringCachingConfig).cacheKey(#userFromToken)")
    public AuthorizationUser updateOrAddAuthorizationUser(User userFromToken) {
        var userFromDB = userBusinessService.getOrCreateUser(userFromToken);
        updateChangeableFields(userFromToken, userFromDB);
        return new AuthorizationUser(userFromDB);
    }

    // firstname and lastname comes from JWT token and could be updated in keycloak.
    // okr champion is set in application properties and should be updated as well
    private void updateChangeableFields(User userFromToken, User userFromDB) {
        userFromDB.setFirstname(userFromToken.getFirstname());
        userFromDB.setLastname(userFromToken.getLastname());
        setOkrChampionFromProperties(userFromDB);
        userBusinessService.saveUser(userFromDB);
    }

    public void setOkrChampionFromProperties(User user) {
        TenantConfigProvider.TenantConfig tenantConfig = this.tenantConfigProvider
                .getTenantConfigById(TenantContext.getCurrentTenant())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find tenant"));
        for (var mail : tenantConfig.okrChampionEmails()) {
            if (mail.trim().equals(user.getEmail())) {
                user.setOkrChampion(true);
                return;
            }
        }
    }
}
