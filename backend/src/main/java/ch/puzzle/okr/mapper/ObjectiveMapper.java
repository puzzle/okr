package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.business.QuarterBusinessService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ObjectiveMapper {

    private final TeamPersistenceService teamPersistenceService;
    private final QuarterBusinessService quarterBusinessService;

    public ObjectiveMapper(TeamPersistenceService teamPersistenceService, QuarterBusinessService quarterBusinessService) {
        this.teamPersistenceService = teamPersistenceService;
        this.quarterBusinessService = quarterBusinessService;
    }

    public ObjectiveDto toDto(Objective objective) {
        return new ObjectiveDto(objective.getId(), objective.getTitle(), objective.getTeam().getId(),
                objective.getQuarter().getId(), objective.getDescription(), objective.getState(),
                objective.getCreatedOn(), objective.getModifiedOn());
    }

    public Objective toObjective(ObjectiveDto objectiveDto) {
        return Objective.Builder.builder().withId(objectiveDto.id()).withTitle(objectiveDto.title())
                .withTeam(teamPersistenceService.findById(objectiveDto.teamId()))
                .withDescription(objectiveDto.description()).withModifiedOn(LocalDateTime.now())
                .withState(objectiveDto.state()).withCreatedOn(objectiveDto.createdOn())
                .withQuarter(quarterBusinessService.getQuarterById(objectiveDto.quarterId())).build();
    }
}
