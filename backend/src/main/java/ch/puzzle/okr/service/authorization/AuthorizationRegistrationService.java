package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.converter.JwtConverterFactory;
import ch.puzzle.okr.mapper.role.RoleMapperFactory;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UserBusinessService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;

@Service
public class AuthorizationRegistrationService {

    @Value("${okr.organisation.name.1stLevel}")
    private String firstLevelOrganisationName;

    private final UserBusinessService userBusinessService;
    private final TeamPersistenceService teamPersistenceService;
    private final JwtConverterFactory jwtConverterFactory;
    private final RoleMapperFactory roleMapperFactory;

    public AuthorizationRegistrationService(UserBusinessService userBusinessService,
            TeamPersistenceService teamPersistenceService, JwtConverterFactory jwtConverterFactory,
            RoleMapperFactory roleMapperFactory) {
        this.userBusinessService = userBusinessService;
        this.teamPersistenceService = teamPersistenceService;
        this.jwtConverterFactory = jwtConverterFactory;
        this.roleMapperFactory = roleMapperFactory;
    }

    @Cacheable(value = AUTHORIZATION_USER_CACHE, key = "#user.email")
    public AuthorizationUser registerAuthorizationUser(User user) {
        return new AuthorizationUser(userBusinessService.getOrCreateUser(user),
                roleMapperFactory.getRoleMapper().mapAuthorizationRoles(user));
    }
}
