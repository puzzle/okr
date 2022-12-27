package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewService {
    private final TeamRepository teamRepository;
    private final OverviewMapper overviewMapper;
    private final ObjectiveRepository objectiveRepository;

    public OverviewService(TeamRepository teamRepository, OverviewMapper overviewMapper,
            ObjectiveRepository objectiveRepository) {
        this.teamRepository = teamRepository;
        this.overviewMapper = overviewMapper;
        this.objectiveRepository = objectiveRepository;
    }

    public List<OverviewDto> getOverview(List<Long> teamFilter, Long quarterFilter) {
        List<Team> teamList = teamRepository.findAllById(teamFilter);
        return teamList.stream().map(team -> overviewMapper.toDto(team,
                objectiveRepository.findByQuarterIdAndTeamId(quarterFilter, team.getId()))).toList();
    }
}
