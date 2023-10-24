package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationReadRole;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.authorization.AuthorizationWriteRole;
import ch.puzzle.okr.service.business.UserBusinessService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;
import static ch.puzzle.okr.models.authorization.AuthorizationReadRole.*;
import static ch.puzzle.okr.models.authorization.AuthorizationWriteRole.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthorizationRegistrationService {

    @Value("${okr.team.role.name.1stLevel}")
    private String firstLevelRoleName;
    @Value("${okr.team.role.name.2ndLevel}")
    private String secondLevelRoleName;

    private final UserBusinessService userBusinessService;
    private final TeamPersistenceService teamPersistenceService;
    private final JwtRolesConverter jwtRolesConverter;

    public AuthorizationRegistrationService(UserBusinessService userBusinessService,
            TeamPersistenceService teamPersistenceService, JwtRolesConverter jwtRolesConverter) {
        this.userBusinessService = userBusinessService;
        this.teamPersistenceService = teamPersistenceService;
        this.jwtRolesConverter = jwtRolesConverter;
    }

    @Cacheable(value = AUTHORIZATION_USER_CACHE, key = "#user.username")
    public AuthorizationUser registerAuthorizationUser(User user, Jwt token) {
        List<String> roles = jwtRolesConverter.convert(token);
        return new AuthorizationUser(userBusinessService.getOrCreateUser(user), getTeamIds(roles),
                // TODO: teamPersistenceService.findByRoleName(firstLevelRoleName).getId(), getReadRoles(roles),
                13L, getReadRoles(roles), getWriteRoles(roles));
    }

    private List<Long> getTeamIds(List<String> tokenRoles) {
        // TODO: List<Long> teamIds = teamPersistenceService.findByRoleNames(tokenRoles);
        List<Long> teamIds = List.of();
        if (teamIds.isEmpty()) {
            throw new ResponseStatusException(UNAUTHORIZED, "no team found for given roles " + tokenRoles);
        }
        return teamIds;
    }

    private List<AuthorizationReadRole> getReadRoles(List<String> tokenRoles) {
        List<AuthorizationReadRole> roles = new ArrayList<>();
        if (hasFirstLevelRole(tokenRoles)) {
            roles.add(READ_ALL_DRAFT);
        } else if (hasSecondLevelRole(tokenRoles)) {
            roles.add((READ_TEAMS_DRAFT));
        } else if (hasOkrRole(tokenRoles)) {
            roles.add(READ_TEAM_DRAFT);
        }
        roles.add(READ_ALL_PUBLISHED);
        return roles;
    }

    private List<AuthorizationWriteRole> getWriteRoles(List<String> tokenRoles) {
        List<AuthorizationWriteRole> roles = new ArrayList<>();
        if (hasFirstLevelRole(tokenRoles)) {
            roles.add(WRITE_ALL);
        } else if (hasSecondLevelRole(tokenRoles)) {
            roles.add(WRITE_ALL_TEAMS);
        } else if (hasOkrRole(tokenRoles)) {
            roles.add(WRITE_TEAM);
        }
        return roles;
    }

    private boolean hasFirstLevelRole(List<String> tokenRoles) {
        return tokenRoles.contains(firstLevelRoleName);
    }

    private boolean hasSecondLevelRole(List<String> tokenRoles) {
        return tokenRoles.contains(secondLevelRoleName);
    }

    private static boolean hasOkrRole(List<String> tokenRoles) {
        return !tokenRoles.isEmpty();
    }
}
