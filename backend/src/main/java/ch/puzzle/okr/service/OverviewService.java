package ch.puzzle.okr.service;

import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.stereotype.Service;

@Service
public class OverviewService {
    private final OverviewMapper overviewMapper;
    private final ObjectiveBusinessService objectiveBusinessService;
    private final TeamBusinessService teamBusinessService;

    public OverviewService(OverviewMapper overviewMapper, ObjectiveBusinessService objectiveBusinessService,
            TeamBusinessService teamBusinessService) {
        this.objectiveBusinessService = objectiveBusinessService;
        this.overviewMapper = overviewMapper;
        this.teamBusinessService = teamBusinessService;
    }
}
