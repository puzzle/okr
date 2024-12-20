package ch.puzzle.okr;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.authorization.AuthorizationRegistrationService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;
import static ch.puzzle.okr.test.TestHelper.defaultUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
class SpringCachingConfigTest {

    @Autowired
    AuthorizationRegistrationService service;
    @Autowired
    CacheManager cacheManager;

    private final User user = defaultUser(null);
    private final String tenant = TestHelper.SCHEMA_PITC;
    private final String key = tenant + "_" + user.getEmail();

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @AfterEach
    void tearDown() {
        TenantContext.setCurrentTenant(null);
        cacheManager.getCache(AUTHORIZATION_USER_CACHE).clear();
    }

    @DisplayName("Should confirm user is not in the cache before calling updateOrAddAuthorizationUser")
    @Test
    void shouldNotHaveCachedUserBeforeUpdateOrAddAuthorizationUser() {
        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);

        assertNotNull(cache);
        assertNull(cache.get(key, AuthorizationUser.class));
    }

    @DisplayName("Should put user in the cache with key composed of tenant and user email when calling updateOrAddAuthorizationUser")
    @Test
    void shouldCacheUserGivenToUpdateOrAddAuthorizationUser() {
        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);

        service.updateOrAddAuthorizationUser(user);

        assertNotNull(cache);
        AuthorizationUser expectedUser = new AuthorizationUser(user);
        AuthorizationUser cachedUser = cache.get(key, AuthorizationUser.class);
        assertEqualUsers(expectedUser, cachedUser);
    }

    private void assertEqualUsers(AuthorizationUser expectedAuthorizationUser,
            AuthorizationUser actualAuthorizationUser) {

        User expectedUser = expectedAuthorizationUser.user();
        User actualUser = actualAuthorizationUser.user();
        assertTrue(expectedUser.getFirstName().equals(actualUser.getFirstName())
                && expectedUser.getLastName().equals(actualUser.getLastName())
                && expectedUser.getEmail().equals(actualUser.getEmail()));
    }
}