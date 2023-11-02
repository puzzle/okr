package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.CompletedDto;
import ch.puzzle.okr.models.Completed;
import org.springframework.stereotype.Component;

@Component
public class CompletedMapper {

    public CompletedDto toDto(Completed completed) {
        return new CompletedDto(completed.getId(), completed.getObjective(), completed.getComment());
    }

    public Completed toCompleted(CompletedDto completedDto) {
        return Completed.Builder.builder().withId(completedDto.id()).withObjective(completedDto.objective())
                .withComment(completedDto.comment()).build();
    }
}
