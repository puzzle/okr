package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import ch.puzzle.okr.service.validation.OverviewValidationService;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class OverviewBusinessService {
    private final OverviewPersistenceService overviewPersistenceService;
    private final QuarterBusinessService quarterBusinessService;
    private final OverviewValidationService validator;

    public OverviewBusinessService(OverviewPersistenceService overviewPersistenceService,
                                   QuarterBusinessService quarterBusinessService, OverviewValidationService validator) {
        this.overviewPersistenceService = overviewPersistenceService;
        this.quarterBusinessService = quarterBusinessService;
        this.validator = validator;
    }

    public List<Overview> getFilteredOverview(Long quarterId, List<Long> teamIds, String objectiveQuery,
                                              AuthorizationUser authorizationUser) {
        if (Objects.isNull(quarterId)) {
            quarterId = quarterBusinessService.getCurrentQuarter().getId();
        }
        teamIds = teamIds == null ? List.of() : teamIds;
        validator.validateOnGet(quarterId, teamIds);

        if (teamIds.isEmpty()) {
            return List.of();
        }

        List<Overview> overviews = overviewPersistenceService
                .getFilteredOverview(quarterId, teamIds, objectiveQuery, authorizationUser);

        this.setBacklogQuarters(overviews);
        return sortOverview(overviews, authorizationUser);
    }

    private void setBacklogQuarters(List<Overview> overviews) {
        for (Overview overview : overviews) {
            Quarter overviewQuarter = quarterBusinessService.getQuarterById(overview.getQuarterId());
            overview.setBacklogQuarter(overviewQuarter.isBacklogQuarter());
        }
    }

    private List<Overview> sortOverview(List<Overview> overviews, AuthorizationUser authorizationUser) {
        overviews.sort(new OverviewComparator(authorizationUser));
        return overviews;
    }

    private record OverviewComparator(AuthorizationUser authorizationUser) implements Comparator<Overview> {

        @Override
        public int compare(Overview o1, Overview o2) {
            boolean containsUserTeam1 = authorizationUser.isUserMemberInTeam(o1.getOverviewId().getTeamId());
            boolean containsUserTeam2 = authorizationUser.isUserMemberInTeam(o2.getOverviewId().getTeamId());

            if (containsUserTeam1 != containsUserTeam2) {
                return containsUserTeam1 ? -1 : 1;
            }
            if (!Objects.equals(o1.getTeamName(), o2.getTeamName())) {
                return o1.getTeamName().compareTo(o2.getTeamName());
            }
            if (Objects.equals(o1.getObjectiveCreatedOn(), o2.getObjectiveCreatedOn())) {
                return o1.getOverviewId().compareTo(o2.getOverviewId());
            }
            return o1.getObjectiveCreatedOn().compareTo(o2.getObjectiveCreatedOn());
        }
    }
}
