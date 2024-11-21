package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.CompletedDto;
import ch.puzzle.okr.models.Completed;
import org.springframework.stereotype.Component;

@Component
public class CompletedMapper {

    private final ObjectiveMapper objectiveMapper;

    public CompletedMapper(ObjectiveMapper objectiveMapper) {
        this.objectiveMapper = objectiveMapper;
    }

    public CompletedDto toDto(Completed completed) {
        return new CompletedDto( //
                completed.getId(), //
                objectiveMapper.toDto(completed.getObjective()), //
                completed.getComment());
    }

    public Completed toCompleted(CompletedDto completedDto) {
        return Completed.Builder.builder() //
                .withId(completedDto.id()) //
                .withObjective(objectiveMapper.toObjective(completedDto.objective())) //
                .withComment(completedDto.comment()) //
                .build();
    }
}
