package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;
import static ch.puzzle.okr.TestHelper.defaultUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
class AuthorizationRegistrationServiceIT {
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private AuthorizationRegistrationService authorizationRegistrationService;
    @Autowired
    private UserPersistenceService userPersistenceService;

    private final User user = defaultUser(null);

    private final String tenant = TestHelper.SCHEMA_PITC;
    private final String key = tenant + "_" + user.getEmail();

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(tenant);
    }

    @AfterEach
    void tearDown() {
        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);
        assertNotNull(cache);
        cache.clear();
        TenantContext.setCurrentTenant(null);
    }

    @Test
    void registerAuthorizationUserShouldAddAuthorizationUserToCache() {
        authorizationRegistrationService.updateOrAddAuthorizationUser(user);

        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);
        assertNotNull(cache);
        assertNotNull(cache.get(key, AuthorizationUser.class));
    }

    @Disabled
    @DisplayName("registerAuthorizationUser for a user with an email not defined in the application-integration-test.properties should set OkrChampions to false")
    @Test
    void registerAuthorizationUser_shouldSetOkrChampionsToFalse() {
        // arrange
        User user = User.Builder.builder().withFirstname("Richard") //
                .withLastname("Eberhard") //
                .withEmail("richard.eberhard@puzzle.ch") // email not found in application-integration-test.properties
                .build();

        userPersistenceService.getOrCreateUser(user); // updates input user with id from DB !!!

        // act
        AuthorizationUser processedUser = authorizationRegistrationService.updateOrAddAuthorizationUser(user);

        // assert
        assertFalse(processedUser.user().isOkrChampion());
        Optional<User> userFromDB = userPersistenceService.findByEmail(user.getEmail());
        assertFalse(userFromDB.get().isOkrChampion());

        // cleanup
        userPersistenceService.deleteById(userFromDB.get().getId());
    }

    @Disabled
    @Test
    @DisplayName("registerAuthorizationUser for a user with an email defined in the application-integration-test.properties should set OkrChampions to true")
    void registerAuthorizationUserShouldSetOkrChampionsToTrue() {
        // arrange
        User user = User.Builder.builder().withFirstname("Gerrit") //
                .withLastname("Braun,") //
                .withEmail("wunderland@puzzle.ch") // user.champion.emails from application-integration-test.properties
                .build();

        userPersistenceService.getOrCreateUser(user); // updates input user with id from DB !!!

        // act
        AuthorizationUser processedUser = authorizationRegistrationService.updateOrAddAuthorizationUser(user);

        // assert
        assertTrue(processedUser.user().isOkrChampion());
        Optional<User> userFromDB = userPersistenceService.findByEmail(user.getEmail());
        assertTrue(userFromDB.get().isOkrChampion());

        // cleanup
        userPersistenceService.deleteById(userFromDB.get().getId());
    }

    @Test
    void registerAuthorizationUser_shouldSetFirstnameAndLastnameFromToken() {
        // arrange
        User user = User.Builder.builder() //
                .withFirstname("Richard") //
                .withLastname("Eberhard") //
                .withEmail("richard.eberhard@puzzle.ch") //
                .build();
        userPersistenceService.save(user);

        String firstNameFromToken = "Richu";
        String lastNameFromToken = "von Gunten";
        User userFromToken = User.Builder.builder() //
                .withFirstname(firstNameFromToken) //
                .withLastname(lastNameFromToken) //
                .withEmail("richard.eberhard@puzzle.ch") //
                .build();

        // act
        authorizationRegistrationService.updateOrAddAuthorizationUser(userFromToken);

        // assert
        Optional<User> userFromDB = userPersistenceService.findByEmail(user.getEmail());
        assertEquals(userFromDB.get().getFirstname(), firstNameFromToken);
        assertEquals(userFromDB.get().getLastname(), lastNameFromToken);

        // cleanup
        userPersistenceService.deleteById(userFromDB.get().getId());
    }
}
