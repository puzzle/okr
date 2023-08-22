package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<OverviewDto> getOverview(List<Long> teamIds, Long quarterId) {
        List<Team> teams = teamBusinessService.getAllTeams();
        return teams.stream().map(team -> overviewMapper.toDto(team,
                objectiveBusinessService.getObjectiveByTeamIdAndQuarterId(team.getId(), quarterId))).toList();
    }
}
