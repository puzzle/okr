package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewService {
    private final OverviewMapper overviewMapper;
    private final ObjectiveBusinessService objectiveBusinessService;
    private final OverviewBusinessService overviewBusinessService;
    private final TeamBusinessService teamBusinessService;

    public OverviewService(OverviewMapper overviewMapper, ObjectiveBusinessService objectiveBusinessService,
            OverviewBusinessService overviewBusinessService, TeamBusinessService teamBusinessService) {
        this.objectiveBusinessService = objectiveBusinessService;
        this.overviewMapper = overviewMapper;
        this.overviewBusinessService = overviewBusinessService;
        this.teamBusinessService = teamBusinessService;
    }

    public List<OverviewDto> getOverview(List<Long> teamIds, Long quarterId) {
        List<Team> teams = teamBusinessService.getAllTeams();
        return teams.stream().map(team -> overviewMapper.toDto(team,
                objectiveBusinessService.getObjectiveByTeamIdAndQuarterId(team.getId(), quarterId))).toList();
    }

    public List<ch.puzzle.okr.dto.overview.OverviewDto> getOverviewByQuarterIdAndTeamIds(Long quarterFilter,
            List<Long> teamFilter) {
        return overviewMapper
                .toDto(overviewBusinessService.getOverviewByQuarterIdAndTeamIds(quarterFilter, teamFilter));
    }
}
