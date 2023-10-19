package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationReadRole;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.authorization.AuthorizationWriteRole;
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
import static ch.puzzle.okr.models.authorization.AuthorizationReadRole.*;
import static ch.puzzle.okr.models.authorization.AuthorizationWriteRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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
    void registerAuthorizationUser_ShouldAddAuthorizationUserToCache() {
        Jwt token = mockJwtToken(user, List.of(ROLE_ORG_GL));
        authorizationRegistrationService.registerAuthorizationUser(user, token);

        Cache cache = cacheManager.getCache(AUTHORIZATION_USER_CACHE);
        assertNotNull(cache);
        assertNotNull(cache.get(user.getUsername(), AuthorizationUser.class));

        cache = cacheManager.getCache(USER_CACHE);
        assertNotNull(cache);
        assertNotNull(cache.get(user.getUsername(), User.class));
    }

    @Test
    void registerAuthorizationUser_ShouldSetFirstLevelRoles() {
        Jwt token = mockJwtToken(user, List.of(ROLE_ORG_GL, ROLE_ORG_BL));
        AuthorizationUser authorizationUser = authorizationRegistrationService.registerAuthorizationUser(user, token);

        assertRoles(List.of(READ_ALL_DRAFT, READ_ALL_PUBLISHED), List.of(WRITE_ALL), authorizationUser);
    }

    @Test
    void registerAuthorizationUser_ShouldSetSecondLevelRoles() {
        Jwt token = mockJwtToken(user, List.of(ROLE_ORG_BL, ROLE_ORG_TEAM));
        AuthorizationUser authorizationUser = authorizationRegistrationService.registerAuthorizationUser(user, token);

        assertRoles(List.of(READ_TEAMS_DRAFT, READ_ALL_PUBLISHED), List.of(WRITE_ALL_TEAMS), authorizationUser);
    }

    @Test
    void registerAuthorizationUser_ShouldSetTeamRoles() {
        Jwt token = mockJwtToken(user, List.of(ROLE_ORG_TEAM));
        AuthorizationUser authorizationUser = authorizationRegistrationService.registerAuthorizationUser(user, token);

        assertRoles(List.of(READ_TEAM_DRAFT, READ_ALL_PUBLISHED), List.of(WRITE_TEAM), authorizationUser);
    }

    @Test
    void registerAuthorizationUser_ShouldThrowException_WhenTeamNotFound() {
        Jwt token = mockJwtToken(user, List.of("org_foo", "xxx_bar"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authorizationRegistrationService.registerAuthorizationUser(user, token));

        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals("no team found for given roles [org_foo]", exception.getReason());
    }

    @Test
    void registerAuthorizationUser_ShouldThrowException_WhenNoRolesFound() {
        Jwt token = mockJwtToken(user, List.of());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authorizationRegistrationService.registerAuthorizationUser(user, token));

        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals("no team found for given roles []", exception.getReason());
    }

    private static void assertRoles(List<AuthorizationReadRole> readRoles, List<AuthorizationWriteRole> writeRoles,
            AuthorizationUser authorizationUser) {
        assertThat(readRoles).hasSameElementsAs(authorizationUser.readRoles());
        assertThat(writeRoles).hasSameElementsAs(authorizationUser.writeRoles());
    }
}
