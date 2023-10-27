package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
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
        return overviewPersistenceService.getFilteredOverview(quarterId, teamIds, objectiveQuery, authorizationUser);
    }
}
