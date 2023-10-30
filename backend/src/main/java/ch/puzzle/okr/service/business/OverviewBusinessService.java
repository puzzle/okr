package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import ch.puzzle.okr.service.validation.OverviewValidationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class OverviewBusinessService {
    private final OverviewPersistenceService overviewPersistenceService;
    private final QuarterBusinessService quarterBusinessService;
    private final OverviewValidationService validator;

    private final TeamBusinessService teamBusinessService;

    public OverviewBusinessService(OverviewPersistenceService overviewPersistenceService,
            QuarterBusinessService quarterBusinessService, OverviewValidationService validator,
            TeamBusinessService teamBusinessService) {
        this.overviewPersistenceService = overviewPersistenceService;
        this.quarterBusinessService = quarterBusinessService;
        this.validator = validator;
        this.teamBusinessService = teamBusinessService;
    }

    public List<Overview> getFilteredOverview(Long quarterId, List<Long> teamIds, String objectiveQuery,
            AuthorizationUser authorizationUser) {
        if (Objects.isNull(quarterId)) {
            quarterId = quarterBusinessService.getCurrentQuarter().getId();
        }

        if (CollectionUtils.isEmpty(teamIds)) {
            teamIds = teamBusinessService.getAllTeams().stream().map(Team::getId).toList();
        }
        validator.validateOnGet(quarterId, teamIds);
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(quarterId, teamIds, objectiveQuery,
                authorizationUser);
        return sortOverview(overviews, authorizationUser);
    }

    private List<Overview> sortOverview(List<Overview> overviews, AuthorizationUser authorizationUser) {
        overviews.sort(new OverviewComparator(authorizationUser));
        return overviews;
    }

    private record OverviewComparator(AuthorizationUser authorizationUser) implements Comparator<Overview> {

        @Override
        public int compare(Overview o1, Overview o2) {
            boolean containsUserTeam1 = authorizationUser.userTeamIds().contains(o1.getOverviewId().getTeamId());
            boolean containsUserTeam2 = authorizationUser.userTeamIds().contains(o2.getOverviewId().getTeamId());
            boolean containsFirstLevelTeam1 = authorizationUser.firstLevelTeamIds()
                    .contains(o1.getOverviewId().getTeamId());
            boolean containsFirstLevelTeam2 = authorizationUser.firstLevelTeamIds()
                    .contains(o2.getOverviewId().getTeamId());

            if (containsUserTeam1 == containsUserTeam2) {
                if (containsFirstLevelTeam1 == containsFirstLevelTeam2) {
                    if (Objects.equals(o1.getTeamName(), o2.getTeamName())) {
                        if (Objects.equals(o1.getObjectiveCreatedOn(), o2.getObjectiveCreatedOn())) {
                            return o1.getOverviewId().compareTo(o2.getOverviewId());
                        }
                        return o1.getObjectiveCreatedOn().compareTo(o2.getObjectiveCreatedOn());
                    } else {
                        return o1.getTeamName().compareTo(o2.getTeamName());
                    }
                } else {
                    return (containsFirstLevelTeam1 && !containsFirstLevelTeam2) ? -1 : 1;
                }
            }
            return (containsUserTeam1 && !containsUserTeam2) ? -1 : 1;
        }
    }
}
