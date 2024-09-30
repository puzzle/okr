package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.puzzle.okr.service.authorization.AuthorizationService.hasRoleWriteAndReadAll;

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
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        List<Overview> overviews = overviewBusinessService.getFilteredOverview(quarterId, teamIds, objectiveQuery,
                authorizationUser);
        setRoleCreateOrUpdateTeam(overviews, authorizationUser);
        return overviews;
    }

    private void setRoleCreateOrUpdateTeam(List<Overview> overviews, AuthorizationUser authorizationUser) {
        if (!CollectionUtils.isEmpty(overviews)) {
            Map<Long, Boolean> teamAccess = new HashMap<>(overviews.size());
            overviews.forEach(o -> setRoleCreateOrUpdateTeam(o, authorizationUser, teamAccess));
        }
    }

    private void setRoleCreateOrUpdateTeam(Overview overview, AuthorizationUser authorizationUser,
            Map<Long, Boolean> teamAccess) {
        if (hasOverviewTeamIdAndObjectiveId(overview)) {
            Long teamId = overview.getOverviewId().getTeamId();
            teamAccess.putIfAbsent(teamId, isWriteable(authorizationUser, overview));
            overview.setWriteable(teamAccess.get(teamId));
        }
    }

    private boolean hasOverviewTeamIdAndObjectiveId(Overview overview) {
        return overview.getOverviewId() != null && overview.getOverviewId().getObjectiveId() != null
                && overview.getOverviewId().getTeamId() != null;
    }

    public boolean hasWriteAllAccess() {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        return hasRoleWriteAndReadAll(authorizationUser);
    }

    private boolean isWriteable(AuthorizationUser authorizationUser, Overview overview) {
        return AuthorizationService.hasRoleWriteForTeam(authorizationUser, overview.getOverviewId().getTeamId());
    }
}
