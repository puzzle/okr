package ch.puzzle.okr.service;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static ch.puzzle.okr.SpringCachingConfig.USER_CACHE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringIntegrationTest
class CacheServiceIT {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private UserPersistenceService userPersistenceService;

    private User createdUser;
    private Cache cache;

    private static final String USER_EMAIL = "email";

    @BeforeEach
    void beforeEach() {
        cache = cacheManager.getCache(USER_CACHE);
    }

    @AfterEach
    void tearDown() {
        if (createdUser != null) {
            userPersistenceService.deleteById(createdUser.getId());
            createdUser = null;
        }
        cache.clear();
    }

    private static User createUser() {
        return User.Builder.builder().withFirstname("firstname").withLastname("lastname")
                .withEmail("username@puzzle.ch").build();
    }

    @Test
    void emptyUsersCacheShouldClearUserCache() {
        createdUser = userPersistenceService.getOrCreateUser(createUser());
        User userBeforeClearCache = cache.get(USER_EMAIL, User.class);

        cacheService.emptyUsersCache();

        User userAfterClearCache = cache.get(USER_EMAIL, User.class);
        assertNotNull(userBeforeClearCache);
        assertNull(userAfterClearCache);
    }

    @Test
    void emptyAllCachesShouldClearAllCaches() {
        createdUser = userPersistenceService.getOrCreateUser(createUser());
        User userBeforeClearCache = cache.get(USER_EMAIL, User.class);

        cacheService.emptyAllCaches();

        User userAfterClearCache = cache.get(USER_EMAIL, User.class);
        assertNotNull(userBeforeClearCache);
        assertNull(userAfterClearCache);
    }
}
