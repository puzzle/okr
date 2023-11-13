package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.converter.JwtOrganisationConverter;
import ch.puzzle.okr.mapper.RoleMapper;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UserBusinessService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthorizationRegistrationService {

    @Value("${okr.organisation.name.1stLevel}")
    private String firstLevelOrganisationName;

    private final UserBusinessService userBusinessService;
    private final TeamPersistenceService teamPersistenceService;
    private final JwtOrganisationConverter jwtOrganisationConverter;
    private final RoleMapper roleMapper;

    public AuthorizationRegistrationService(UserBusinessService userBusinessService,
            TeamPersistenceService teamPersistenceService, JwtOrganisationConverter jwtOrganisationConverter,
            RoleMapper roleMapper) {
        this.userBusinessService = userBusinessService;
        this.teamPersistenceService = teamPersistenceService;
        this.jwtOrganisationConverter = jwtOrganisationConverter;
        this.roleMapper = roleMapper;
    }

    @Cacheable(value = AUTHORIZATION_USER_CACHE, key = "#user.username")
    public AuthorizationUser registerAuthorizationUser(User user, Jwt token) {
        List<String> organisationNames = jwtOrganisationConverter.convert(token);
        return new AuthorizationUser(userBusinessService.getOrCreateUser(user), getTeamIds(organisationNames),
                getFirstLevelTeamIds(), roleMapper.mapOrganisationNames(organisationNames));
    }

    private List<Long> getTeamIds(List<String> organisationNames) {
        return teamPersistenceService.findTeamIdsByOrganisationNames(organisationNames);
    }

    private List<Long> getFirstLevelTeamIds() {
        return teamPersistenceService.findTeamIdsByOrganisationName(firstLevelOrganisationName);
    }
}
