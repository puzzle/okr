package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.repository.OverviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewPersistenceService {

    private final OverviewRepository overviewRepository;

    public OverviewPersistenceService(OverviewRepository overviewRepository) {
        this.overviewRepository = overviewRepository;
    }

    public List<Overview> getOverviewByQuarterAndTeamsAndObjectiveQuery(Long quarterId, List<Long> teamIds,
            String objectiveQuery) {
        return overviewRepository.getFilteredOverview(quarterId, teamIds, objectiveQuery.toLowerCase());
    }
}
