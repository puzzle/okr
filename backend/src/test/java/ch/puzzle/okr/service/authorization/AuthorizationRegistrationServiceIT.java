package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.test.TestHelper;
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
import static ch.puzzle.okr.test.TestHelper.defaultUser;
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

    private static final String EMAIL_WUNDERLAND = "wunderland@puzzle.ch";

    private final User userWithNoOkrChampionInfo = User.Builder.builder() //
            .withFirstname("Alice") //
            .withLastname("Wunderland") //
            .withEmail(EMAIL_WUNDERLAND) // user.champion.emails from application-integration-test.properties
            .build();

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(tenant);
    }

    @AfterEach
    void tearDown() {
        resetOkrChampionStatus(userWithNoOkrChampionInfo);
        clearCache();
        TenantContext.setCurrentTenant(null);
    }

    private void resetOkrChampionStatus(User user) {
        // var userFromDb = userPersistenceService.getOrCreateUser(user);
        Optional<User> userFromDb = userPersistenceService.findByEmail(user.getEmail());
        assertTrue(userFromDb.isPresent());

        userFromDb.get().setOkrChampion(false);
        userPersistenceService.save(userFromDb.get());
        assertOkrChampionStatusInDb(user.getEmail(), false);
    }

    private void clearCache() {
        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);
        assertNotNull(cache);
        cache.clear();
    }

    private void assertOkrChampionStatusInDb(String email, boolean expectedOkrChampionStatus) {
        var userInDb = userPersistenceService.findByEmail(email);
        assertTrue(userInDb.isPresent());
        assertEquals(expectedOkrChampionStatus, userInDb.get().isOkrChampion());
    }

    @Test
    void registerAuthorizationUserShouldAddAuthorizationUserToCache() {
        // arrange
        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);

        // act
        authorizationRegistrationService.updateOrAddAuthorizationUser(user);

        // assert
        assertNotNull(cache);
        assertNotNull(cache.get(key, AuthorizationUser.class));

        // cleanup
        userPersistenceService.deleteById(user.getId());
    }

    @DisplayName("registerAuthorizationUser for a user with an email not defined in the application-integration-test.properties should set OkrChampions to false")
    @Test
    void registerAuthorizationUser_shouldSetOkrChampionsToFalse() {
        // arrange
        User user = User.Builder.builder() //
                .withFirstname("Richard") //
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

    /*
     * Special test setup. <pre> - the user wunderland@puzzle.ch is an existing user in the H2 db (created via
     * X_TestData.sql) - the user wunderland@puzzle.ch is also defined in application-integration-test.properties as
     * user champion - with this combination we can test, that the user in the db (which has initial isOkrChampion ==
     * false) is after calling updateOrAddAuthorizationUser() a user champion. - because the user wunderland@puzzle.ch
     * exists before the test, we make no clean in db (we don't remove it) </pre>
     */
    // @Disabled
    @Test
    @DisplayName("registerAuthorizationUser for a user with an email defined in the application-integration-test.properties should set OkrChampions to true")
    void registerAuthorizationUserShouldSetOkrChampionsToTrue() {
        // arrange
        var userFromDb = userPersistenceService.getOrCreateUser(userWithNoOkrChampionInfo); // ensure user exists
        assertFalse(userFromDb.isOkrChampion()); // pre-condition

        // act
        // load user from db and set OkrChampion status based on property "okr.tenants.pitc.user.champion.emails"
        // from application-integration-test.properties file
        AuthorizationUser processedUser = authorizationRegistrationService
                .updateOrAddAuthorizationUser(userWithNoOkrChampionInfo);

        // assert
        assertTrue(processedUser.user().isOkrChampion());
        assertOkrChampionStatusInDb(processedUser.user().getEmail(), true);

        /*
         * Optional<User> updatedUserFromDB = userPersistenceService.findByEmail(userWithNoOkrChampionInfo.getEmail());
         * assertTrue(updatedUserFromDB.isPresent()); assertTrue(updatedUserFromDB.get().isOkrChampion());
         */
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
