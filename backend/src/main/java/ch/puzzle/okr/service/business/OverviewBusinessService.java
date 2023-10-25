package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.validation.OverviewValidationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class OverviewBusinessService {
    private final OverviewPersistenceService overviewPersistenceService;
    private final QuarterBusinessService quarterBusinessService;
    private final TeamPersistenceService teamPersistenceService;
    private final TeamBusinessService teamBusinessService;
    private final OverviewValidationService validator;

    public OverviewBusinessService(OverviewPersistenceService overviewPersistenceService,
            QuarterBusinessService quarterBusinessService, TeamPersistenceService teamPersistenceService,
            OverviewValidationService validator,
             TeamBusinessService teamBusinessService
            ) {
        this.overviewPersistenceService = overviewPersistenceService;
        this.quarterBusinessService = quarterBusinessService;
        this.teamPersistenceService = teamPersistenceService;
        this.teamBusinessService = teamBusinessService;
        this.validator = validator;
    }

    public List<Overview> getOverviewByQuarterIdAndTeamIds(Long quarterId, List<Long> teamIds,String objectiveQuery,
            AuthorizationUser authorizationUser) {
        if (Objects.isNull(quarterId)) {
            quarterId = quarterBusinessService.getCurrentQuarter().getId();
        }

        if (CollectionUtils.isEmpty(teamIds)) {
            teamIds = authorizationUser.teamIds();
        }
        validator.validateOnGet(quarterId, teamIds);
        return overviewPersistenceService.getOverviewByQuarterAndTeamsAndObjectiveQuery(quarterId, teamIds,
                objectiveQuery, authorizationUser);
    }
}
