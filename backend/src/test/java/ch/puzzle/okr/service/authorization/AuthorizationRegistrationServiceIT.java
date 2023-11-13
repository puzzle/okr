package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationRole;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;
import static ch.puzzle.okr.SpringCachingConfig.USER_CACHE;
import static ch.puzzle.okr.TestConstants.*;
import static ch.puzzle.okr.TestHelper.defaultUser;
import static ch.puzzle.okr.TestHelper.mockJwtToken;
import static ch.puzzle.okr.models.authorization.AuthorizationRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
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
        Cache cache = cacheManager.getCache(USER_CACHE);
        assertNotNull(cache);

        User cachedUser = cache.get(user.getUsername(), User.class);
        if (cachedUser != null) {
            userPersistenceService.deleteById(cachedUser.getId());
        }

        cache.clear();

        cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);
        assertNotNull(cache);
        cache.clear();
    }

    @Test
    void registerAuthorizationUserShouldAddAuthorizationUserToCache() {
        Jwt token = mockJwtToken(user, List.of(ORGANISATION_FIRST_LEVEL));
        authorizationRegistrationService.registerAuthorizationUser(user, token);

        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);
        assertNotNull(cache);
        assertNotNull(cache.get(user.getUsername(), AuthorizationUser.class));

        cache = cacheManager.getCache(USER_CACHE);
        assertNotNull(cache);
        assertNotNull(cache.get(user.getUsername(), User.class));
    }

    @Test
    void registerAuthorizationUserShouldSetFirstLeveOrganisations() {
        Jwt token = mockJwtToken(user, List.of(ORGANISATION_FIRST_LEVEL, ORGANISATION_SECOND_LEVEL));
        AuthorizationUser authorizationUser = authorizationRegistrationService.registerAuthorizationUser(user, token);

        assertRoles(List.of(READ_ALL_DRAFT, READ_ALL_PUBLISHED, WRITE_ALL), authorizationUser);
    }

    @Test
    void registerAuthorizationUserShouldSetSecondLevelOrganisations() {
        Jwt token = mockJwtToken(user, List.of(ORGANISATION_SECOND_LEVEL, ORGANISATION_TEAM));
        AuthorizationUser authorizationUser = authorizationRegistrationService.registerAuthorizationUser(user, token);

        assertRoles(List.of(READ_TEAMS_DRAFT, READ_ALL_PUBLISHED, WRITE_ALL_TEAMS), authorizationUser);
    }

    @Test
    void registerAuthorizationUserShouldSetTeamOrganisations() {
        Jwt token = mockJwtToken(user, List.of(ORGANISATION_TEAM));
        AuthorizationUser authorizationUser = authorizationRegistrationService.registerAuthorizationUser(user, token);

        assertRoles(List.of(READ_TEAM_DRAFT, READ_ALL_PUBLISHED, WRITE_TEAM), authorizationUser);
    }

    private static void assertRoles(List<AuthorizationRole> roles, AuthorizationUser authorizationUser) {
        assertThat(roles).hasSameElementsAs(authorizationUser.roles());
    }

    private void setFirstLevelOrganisation(String firstLevelOrganisation) {
        setField(authorizationRegistrationService, "firstLevelOrganisationName", firstLevelOrganisation);
    }
}
