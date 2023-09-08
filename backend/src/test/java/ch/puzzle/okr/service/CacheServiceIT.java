package ch.puzzle.okr.service;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

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

    @BeforeEach
    void beforeEach() {
        cache = cacheManager.getCache(Constants.USER_CACHE);
    }

    @AfterEach
    void tearDown() {
        if (createdUser != null) {
            userPersistenceService.deleteById(createdUser.getId());
            createdUser = null;
        }
    }

    private static User createUser() {
        return User.Builder.builder().withUsername("username").withFirstname("firstname").withLastname("lastname")
                .withEmail("username@puzzle.ch").build();
    }

    @Test
    void emptyUsersCache_ShouldClearUserCache() {
        createdUser = userPersistenceService.getOrCreateUser(createUser());
        User userBeforeClearCache = cache.get("username", User.class);

        cacheService.emptyUsersCache();

        User userAfterClearCache = cache.get("username", User.class);
        assertNotNull(userBeforeClearCache);
        assertNull(userAfterClearCache);
    }

    @Test
    void emptyAllCaches_ShouldClearAllCaches() {
        createdUser = userPersistenceService.getOrCreateUser(createUser());
        User userBeforeClearCache = cache.get("username", User.class);

        cacheService.emptyAllCaches();

        User userAfterClearCache = cache.get("username", User.class);
        assertNotNull(userBeforeClearCache);
        assertNull(userAfterClearCache);
    }
}
