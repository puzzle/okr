package ch.puzzle.okr.service;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.business.UserBusinessService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import static ch.puzzle.okr.SpringCachingConfig.USER_CACHE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringIntegrationTest
public class CacheServiceIT {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private UserBusinessService userBusinessService;
    @Autowired
    private UserPersistenceService userPersistenceService;

    private User createdUser;

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
        createdUser = userBusinessService.getOrCreateUser(createUser());
        User userBeforeClearCache = cacheManager.getCache(USER_CACHE).get("username", User.class);

        cacheService.emptyUsersCache();

        User userAfterClearCache = cacheManager.getCache(USER_CACHE).get("username", User.class);
        assertNotNull(userBeforeClearCache);
        assertNull(userAfterClearCache);
    }

    @Test
    void emptyAllCaches_ShouldClearAllCaches() {
        createdUser = userBusinessService.getOrCreateUser(createUser());
        User userBeforeClearCache = cacheManager.getCache(USER_CACHE).get("username", User.class);

        cacheService.emptyAllCaches();

        User userAfterClearCache = cacheManager.getCache(USER_CACHE).get("username", User.class);
        assertNotNull(userBeforeClearCache);
        assertNull(userAfterClearCache);
    }
}
