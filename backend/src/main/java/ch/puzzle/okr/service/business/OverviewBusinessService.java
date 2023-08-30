package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Overview;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewBusinessService {

    private final OverviewPersistenceService overviewPersistenceService;

    public OverviewBusinessService(OverviewPersistenceService overviewPersistenceService) {
        this.overviewPersistenceService = overviewPersistenceService;
    }

    public List<Overview> getOverviewByQuarterIdAndTeamIds(Long quarterId, List<Long> teamIds) {
        // TODO get current quarter if quarterId is not set
        // TODO get current team (of current user) if teamIds is empty
        return overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds);
    }
}
