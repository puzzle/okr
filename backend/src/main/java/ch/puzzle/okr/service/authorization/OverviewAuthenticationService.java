package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        List<Overview> overviews = overviewBusinessService.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds,
                authorizationUser);
        overviews.forEach(o -> setRoleCreateOrUpdate(o, authorizationUser));
        return overviews;
    }

    private void setRoleCreateOrUpdate(Overview overview, AuthorizationUser authorizationUser) {
        if (overview.getOverviewId() != null && overview.getOverviewId().getObjectiveId() != null) {
            overview.setWriteable(hasRoleCreateOrUpdate(overview.getOverviewId().getObjectiveId(), authorizationUser));
        } else {
            overview.setWriteable(false);
        }
    }

    private boolean hasRoleCreateOrUpdate(Long objectiveId, AuthorizationUser authorizationUser) {
        try {
            authorizationService.hasRoleCreateOrUpdateByObjectiveId(objectiveId, authorizationUser);
            return true;
        } catch (ResponseStatusException ex) {
            return false;
        }
    }
}
