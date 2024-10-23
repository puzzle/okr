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

    @DisplayName("before calling updateOrAddAuthorizationUser the User is not in the cache")
    @Test
    void testUserIsNotCached() {
        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);

        assertNotNull(cache);
        assertNull(cache.get(key, AuthorizationUser.class));
    }

    @DisplayName("updateOrAddAuthorizationUser puts the User in the cache with key composed by Tenant and User Email")
    @Test
    void testUserIsCached() {
        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);

        service.updateOrAddAuthorizationUser(user);

        assertNotNull(cache);
        AuthorizationUser expectedUser = new AuthorizationUser(user);
        AuthorizationUser cachedUser = cache.get(key, AuthorizationUser.class);
        assertEqualUsers(expectedUser, cachedUser);
    }

    private void assertEqualUsers(AuthorizationUser expectedAuthorizationUser,
            AuthorizationUser actualAuthorizationUser) {

        User expcetedUser = expectedAuthorizationUser.user();
        User actualUser = actualAuthorizationUser.user();
        assertTrue(expcetedUser.getFirstname().equals(actualUser.getFirstname())
                && expcetedUser.getLastname().equals(actualUser.getLastname())
                && expcetedUser.getEmail().equals(actualUser.getEmail()));
    }
}