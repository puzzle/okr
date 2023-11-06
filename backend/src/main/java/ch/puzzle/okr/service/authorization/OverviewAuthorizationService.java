package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OverviewAuthorizationService {

    private final OverviewBusinessService overviewBusinessService;
    private final AuthorizationService authorizationService;

    public OverviewAuthorizationService(OverviewBusinessService overviewBusinessService,
            AuthorizationService authorizationService) {
        this.overviewBusinessService = overviewBusinessService;
        this.authorizationService = authorizationService;
    }

    public List<Overview> getFilteredOverview(Long quarterId, List<Long> teamIds, String objectiveQuery) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        List<Overview> overviews = overviewBusinessService.getFilteredOverview(quarterId, teamIds, objectiveQuery,
                authorizationUser);
        setRoleCreateOrUpdateObjective(overviews, authorizationUser);
        return overviews;
    }

    private void setRoleCreateOrUpdateObjective(List<Overview> overviews, AuthorizationUser authorizationUser) {
        if (!CollectionUtils.isEmpty(overviews)) {
            Map<Long, Boolean> objectiveAccess = new HashMap<>(overviews.size());
            overviews.forEach(o -> setRoleCreateOrUpdateObjective(o, authorizationUser, objectiveAccess));
        }
    }

    private void setRoleCreateOrUpdateObjective(Overview overview, AuthorizationUser authorizationUser,
            Map<Long, Boolean> teamAccess) {
        if (overview.getOverviewId() != null && overview.getOverviewId().getObjectiveId() != null
                && overview.getOverviewId().getTeamId() != null) {
            Long teamId = overview.getOverviewId().getTeamId();
            teamAccess.putIfAbsent(teamId, isWriteable(authorizationUser, overview));
            overview.setWriteable(teamAccess.get(teamId));
        }
    }

    private boolean isWriteable(AuthorizationUser authorizationUser, Overview overview) {
        return authorizationService.isWriteable(authorizationUser, overview.getOverviewId().getTeamId());
    }
}
