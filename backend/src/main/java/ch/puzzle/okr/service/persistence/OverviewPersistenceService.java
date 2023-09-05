package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Overview;
import ch.puzzle.okr.repository.OverviewRepositroy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewPersistenceService {

    private final OverviewRepositroy overviewRepositroy;

    public OverviewPersistenceService(OverviewRepositroy overviewRepositroy) {
        this.overviewRepositroy = overviewRepositroy;
    }

    public List<Overview> getOverviewByQuarterIdAndTeamIds(Long quarterId, List<Long> teamIds) {
        return overviewRepositroy.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds);
    }
}
