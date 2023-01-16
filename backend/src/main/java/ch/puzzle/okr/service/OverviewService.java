package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.Team;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewService {
    private final OverviewMapper overviewMapper;
    private final ObjectiveService objectiveService;
    private final TeamService teamService;

    public OverviewService(OverviewMapper overviewMapper, ObjectiveService objectiveService, TeamService teamService) {
        this.objectiveService = objectiveService;
        this.overviewMapper = overviewMapper;
        this.teamService = teamService;
    }

    public List<OverviewDto> getOverview(List<Long> teamIds, Long quarterId) {
        List<Team> teams = teamService.getAllTeams(teamIds);
        return teams.stream()
                .map(team -> overviewMapper.toDto(team,
                        objectiveService.getObjectiveByTeamIdAndQuarterId(team.getId(), quarterId)))
                .filter(e -> !e.getObjectives().isEmpty()).toList();
    }
}
