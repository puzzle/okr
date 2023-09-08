package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Overview;
import ch.puzzle.okr.repository.OverviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewPersistenceService {

    private final OverviewRepository overviewRepository;

    public OverviewPersistenceService(OverviewRepository overviewRepository) {
        this.overviewRepository = overviewRepository;
    }

    public List<Overview> getOverviewByQuarterIdAndTeamIds(Long quarterId, List<Long> teamIds) {
        return overviewRepository.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds);
    }

    public List<Overview> getOverviewByQuarterId(Long quarterId) {
        // TODO remove function as soon as teamids are able to be read from jwt token
        return overviewRepositroy.getOverviewByQuarterId(quarterId);
    }
}
