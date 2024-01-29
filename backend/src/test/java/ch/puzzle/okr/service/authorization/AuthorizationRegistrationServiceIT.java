package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;
import static ch.puzzle.okr.TestHelper.defaultUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SpringIntegrationTest
class AuthorizationRegistrationServiceIT {
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private AuthorizationRegistrationService authorizationRegistrationService;
    @Autowired
    private UserPersistenceService userPersistenceService;

    private final User user = defaultUser(null);

    @AfterEach
    void tearDown() {
        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);
        assertNotNull(cache);
        cache.clear();
    }

    @Test
    void registerAuthorizationUserShouldAddAuthorizationUserToCache() {
        authorizationRegistrationService.updateOrAddAuthorizationUser(user);

        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);
        assertNotNull(cache);
        assertNotNull(cache.get(user.getEmail(), AuthorizationUser.class));
    }

    @Test
    void registerAuthorizationUser_shouldSetOkrChampionsCorrectly() {
        var userRichard = User.Builder.builder().withFirstname("Richard").withLastname("Eberhard")
                .withEmail("richard.eberhard@puzzle.ch").build();
        var userMaria = User.Builder.builder().withFirstname("Maria").withLastname("Gerber")
                .withEmail("maria.gerber@puzzle.ch").build();
        var userAndrea = User.Builder.builder().withFirstname("Andrea").withLastname("Nydegger")
                .withEmail("andrea.nydegger@puzzle.ch").build();

        userPersistenceService.getOrCreateUser(userRichard);
        userPersistenceService.getOrCreateUser(userMaria);
        userPersistenceService.getOrCreateUser(userAndrea);

        setField(authorizationRegistrationService, "okrChampionEmails",
                "maria.gerber@puzzle.ch, richard.eberhard@puzzle.ch");

        authorizationRegistrationService.updateOrAddAuthorizationUser(userRichard);
        authorizationRegistrationService.updateOrAddAuthorizationUser(userMaria);
        authorizationRegistrationService.updateOrAddAuthorizationUser(userAndrea);

        var userRichardSaved = userPersistenceService.findByEmail(userRichard.getEmail());
        var userMariaSaved = userPersistenceService.findByEmail(userMaria.getEmail());
        var userAndreaSaved = userPersistenceService.findByEmail(userAndrea.getEmail());

        assertTrue(userRichardSaved.get().isOkrChampion());
        assertTrue(userMariaSaved.get().isOkrChampion());
        assertFalse(userAndreaSaved.get().isOkrChampion());

        userPersistenceService.deleteById(userRichardSaved.get().getId());
        userPersistenceService.deleteById(userMariaSaved.get().getId());
        userPersistenceService.deleteById(userAndreaSaved.get().getId());
    }
}
