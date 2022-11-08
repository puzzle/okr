package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.QuarterService;
import ch.puzzle.okr.service.TeamService;
import ch.puzzle.okr.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ObjectiveMapper {

    private final TeamService teamService;
    private final QuarterService quarterService;
    private final UserService userService;

    public ObjectiveMapper(TeamService teamService, QuarterService quarterService, UserService userService) {
        this.teamService = teamService;
        this.quarterService = quarterService;
        this.userService = userService;
    }

    public ObjectiveDto toDto(Objective objective) {
        return new ObjectiveDto(
                objective.getId(), objective.getTitle(), objective.getOwner().getId(),
                objective.getOwner().getFirstname(), objective.getOwner().getLastname(),
                objective.getTeam().getId(), objective.getTeam().getName(),
                objective.getQuarter().getId(), objective.getQuarter().getNumber(), objective.getQuarter().getYear(),
                objective.getDescription(), objective.getProgress());
    }

    public Objective toObjective(ObjectiveDto objectiveDto) {
        return Objective.Builder.builder()
                .withId(objectiveDto.getId())
                .withTitle(objectiveDto.getTitle())
                .withOwner(userService.getOwnerById(objectiveDto.getOwnerId()))
                .withTeam(teamService.getTeamById(objectiveDto.getTeamId()))
                .withQuarter(quarterService.getQuarterById(objectiveDto.getQuarterId()))
                .withDescription(objectiveDto.getDescription())
                .withProgress(objectiveDto.getProgress())
                .withCreatedOn(LocalDateTime.now())
                .build();
    }
}
