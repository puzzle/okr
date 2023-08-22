package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ObjectiveMapper {

    private final TeamPersistenceService teamPersistenceService;
    private final QuarterPersistenceService quarterPersistenceService;
    private final UserPersistenceService userPersistenceService;

    public ObjectiveMapper(TeamPersistenceService teamPersistenceService,
            QuarterPersistenceService quarterPersistenceService, UserPersistenceService userPersistenceService) {
        this.teamPersistenceService = teamPersistenceService;
        this.quarterPersistenceService = quarterPersistenceService;
        this.userPersistenceService = userPersistenceService;
    }

    public ObjectiveDto toDto(Objective objective) {
        return new ObjectiveDto(objective.getId(), objective.getTitle(), objective.getOwner().getId(),
                objective.getOwner().getFirstname(), objective.getOwner().getLastname(), objective.getTeam().getId(),
                objective.getTeam().getName(), objective.getQuarter().getId(), objective.getQuarter().getLabel(),
                objective.getDescription(), objective.getProgress());
    }

    public Objective toObjective(ObjectiveDto objectiveDto) {
        return Objective.Builder.builder().withId(objectiveDto.id()).withTitle(objectiveDto.title())
                .withOwner(userPersistenceService.getOwnerById(objectiveDto.ownerId()))
                .withTeam(teamPersistenceService.getTeamById(objectiveDto.teamId()))
                .withDescription(objectiveDto.description()).withProgress(objectiveDto.progress())
                .withModifiedOn(LocalDateTime.now())
                .withQuarter(quarterPersistenceService.getQuarterById(objectiveDto.quarterId())).build();
    }
}
