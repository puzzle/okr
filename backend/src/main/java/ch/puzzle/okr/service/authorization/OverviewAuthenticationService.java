package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewAuthenticationService {

    private final OverviewBusinessService overviewBusinessService;
    private final AuthorizationService authorizationService;

    public OverviewAuthenticationService(OverviewBusinessService overviewBusinessService,
            AuthorizationService authorizationService) {
        this.overviewBusinessService = overviewBusinessService;
        this.authorizationService = authorizationService;
    }

    public List<Overview> getOverviewByQuarterIdAndTeamIds(Long quarterId, List<Long> teamIds, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        return overviewBusinessService.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds, authorizationUser);
    }
}
