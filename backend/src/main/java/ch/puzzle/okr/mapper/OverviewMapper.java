package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.ObjectiveService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OverviewMapper {

    ObjectiveService objectiveService;
    ObjectiveMapper objectiveMapper;

    public OverviewMapper(ObjectiveService objectiveService, ObjectiveMapper objectiveMapper) {
        this.objectiveService = objectiveService;
        this.objectiveMapper = objectiveMapper;
    }

    public OverviewDto toDto(Team team, List<Objective> objectives) {
        return new OverviewDto(new TeamDto(team.getId(), team.getName()),
                objectives.stream().map(objectiveMapper::toDto).toList());
    }
}
