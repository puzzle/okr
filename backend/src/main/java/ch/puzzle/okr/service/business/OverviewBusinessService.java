package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import ch.puzzle.okr.service.validation.OverviewValidationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class OverviewBusinessService {
    private final OverviewPersistenceService overviewPersistenceService;
    private final QuarterBusinessService quarterBusinessService;
    private final TeamBusinessService teamBusinessService;
    private final OverviewValidationService validator;

    public OverviewBusinessService(OverviewPersistenceService overviewPersistenceService,
            QuarterBusinessService quarterBusinessService, TeamBusinessService teamBusinessService,
            OverviewValidationService validator) {
        this.overviewPersistenceService = overviewPersistenceService;
        this.quarterBusinessService = quarterBusinessService;
        this.teamBusinessService = teamBusinessService;
        this.validator = validator;
    }

    public List<Overview> getFilteredOverview(Long quarterId, List<Long> teamIds, String objectiveQuery) {
        if (Objects.isNull(quarterId)) {
            quarterId = quarterBusinessService.getCurrentQuarter().getId();
        } else {
            validator.validateQuarter(quarterId);
        }
        if (CollectionUtils.isEmpty(teamIds)) {
            // TODO get current team (of current user) if teamIds is empty and remove temp implementation
            // TODO remove line below as soon as teamids are able to be read from jwt token
            teamIds = teamBusinessService.getAllTeams().stream().map(Team::getId).toList();
        }
        validator.validateOnGet(quarterId, teamIds);
        return overviewPersistenceService.getOverviewByQuarterAndTeamsAndObjectiveQuery(quarterId, teamIds,
                objectiveQuery);
    }
}
