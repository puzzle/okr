package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.CompletedDto;
import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CompletedMapperTest {

    private static final long ID = 0L;
    private static final String COMMENT = "some comment";

    @InjectMocks
    private CompletedMapper completedMapper;

    @DisplayName("toDo() should map Completed to Dto")
    @Test
    void toDtoShouldMapCompletedToDto() {
        // arrange
        Objective objective = Objective.Builder.builder()
                                               .withId(23L)
                                               .build();

        Completed completed = Completed.Builder.builder() //
                                               .withId(ID) //
                                               .withComment(COMMENT) //
                                               .withObjective(objective) //
                                               .build();

        // act
        CompletedDto completedDto = completedMapper.toDto(completed);

        // assert
        assertNotNull(completedDto);
        assertCompletedDto(completed, completedDto);
    }

    private void assertCompletedDto(Completed expected, CompletedDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getComment(), actual.comment());
        assertEquals(expected.getObjective()
                             .getId(),
                     actual.objective()
                           .getId());
    }

    @DisplayName("toCompleted() should map Dto to Completed")
    @Test
    void toCompletedShouldMapDtoToCompleted() {
        // arrange
        Objective objective = Objective.Builder.builder()
                                               .withId(23L)
                                               .build();
        CompletedDto completedDto = new CompletedDto(ID, objective, COMMENT);

        // act
        Completed completed = completedMapper.toCompleted(completedDto);

        // assert
        assertNotNull(completed);
        assertCompleted(completedDto, completed);
    }

    private void assertCompleted(CompletedDto expected, Completed actual) {
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.comment(), actual.getComment());
        assertEquals(expected.objective()
                             .getId(),
                     actual.getObjective()
                           .getId());
    }

}
