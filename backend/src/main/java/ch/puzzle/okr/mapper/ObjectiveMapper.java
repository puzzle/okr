package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.business.QuarterBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ObjectiveMapper {

    private final TeamBusinessService teamBusinessService;
    private final QuarterBusinessService quarterBusinessService;

    public ObjectiveMapper(TeamBusinessService teamBusinessService, QuarterBusinessService quarterBusinessService) {
        this.teamBusinessService = teamBusinessService;
        this.quarterBusinessService = quarterBusinessService;
    }

    public ObjectiveDto toDto(Objective objective) {
        return new ObjectiveDto(objective.getId(), objective.getVersion(), objective.getTitle(),
                objective.getTeam().getId(), objective.getQuarter().getId(), objective.getDescription(),
                objective.getState(), objective.getCreatedOn(), objective.getModifiedOn(), objective.isWriteable());
    }

    public Objective toObjective(ObjectiveDto objectiveDto) {
        return Objective.Builder.builder().withId(objectiveDto.id()).withVersion(objectiveDto.version())
                .withTitle(objectiveDto.title()).withTeam(teamBusinessService.getTeamById(objectiveDto.teamId()))
                .withDescription(objectiveDto.description()).withModifiedOn(LocalDateTime.now())
                .withState(objectiveDto.state()).withCreatedOn(objectiveDto.createdOn())
                .withQuarter(quarterBusinessService.getQuarterById(objectiveDto.quarterId())).build();
    }
}
