package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OverviewMapper {

    private final ObjectiveMapper objectiveMapper;
    private final TeamMapper teamMapper;

    public OverviewMapper(ObjectiveMapper objectiveMapper, TeamMapper teamMapper) {
        this.objectiveMapper = objectiveMapper;
        this.teamMapper = teamMapper;
    }

    public OverviewDto toDto(Team team, List<Objective> objectives) {
        return new OverviewDto(teamMapper.toDto(team), objectives.stream().map(objectiveMapper::toDto).toList());
    }
}
