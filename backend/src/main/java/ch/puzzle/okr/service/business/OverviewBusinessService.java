package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Overview;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class OverviewBusinessService {

    private final OverviewPersistenceService overviewPersistenceService;
    private final QuarterBusinessService quarterBusinessService;

    public OverviewBusinessService(OverviewPersistenceService overviewPersistenceService,
            QuarterBusinessService quarterBusinessService) {
        this.overviewPersistenceService = overviewPersistenceService;
        this.quarterBusinessService = quarterBusinessService;
    }

    public List<Overview> getOverviewByQuarterIdAndTeamIds(Long quarterId, List<Long> teamIds) {
        if (Objects.isNull(quarterId)) {
            quarterId = quarterBusinessService.getCurrentQuarter().getId();
        }
        if (CollectionUtils.isEmpty(teamIds)) {
            // TODO get current team (of current user) if teamIds is empty and remove temp implementation
            return overviewPersistenceService.getOverviewByQuarterId(quarterId);
        }
        return overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds);
    }
}
