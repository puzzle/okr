package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.CompletedDto;
import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.business.QuarterBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import ch.puzzle.okr.test.dto.builder.CompletedDtoBuilder;
import ch.puzzle.okr.test.dto.builder.ObjectiveDtoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CompletedMapperTest {
    private static final long COMPLETED_ID = 0L;
    private static final String COMPLETED_COMMENT = "some comment";
    private static final long OBJECTIVE_ID = 23L;

    private static final long NOT_USED_LONG = 1L;

    private CompletedMapper completedMapper;

    @InjectMocks
    private ObjectiveMapper objectiveMapper;

    @Mock
    private TeamBusinessService teamBusinessService;

    @Mock
    private QuarterBusinessService quarterBusinessService;

    @BeforeEach
    void setUp() {
        objectiveMapper = new ObjectiveMapper(teamBusinessService, quarterBusinessService);
        completedMapper = new CompletedMapper(objectiveMapper);
    }

    @DisplayName("Should map completed to dto when calling toDto()")
    @Test
    void shouldMapCompletedToDto() {
        // arrange
        var completed = Completed.Builder.builder() //
                .withId(COMPLETED_ID) //
                .withComment(COMPLETED_COMMENT) //
                .withObjective(Objective.Builder.builder() //
                        .withId(OBJECTIVE_ID)//
                        .withTeam(Team.Builder.builder().withId(NOT_USED_LONG).build()) //
                        .withQuarter(Quarter.Builder.builder().withId(NOT_USED_LONG).build()) //
                        .build()) //
                .build();

        // act
        var completedDto = completedMapper.toDto(completed);

        // assert
        assertNotNull(completedDto);
        assertCompletedDto(completed, completedDto);
    }

    private void assertCompletedDto(Completed expected, CompletedDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getComment(), actual.comment());
        assertEquals(expected.getObjective().getId(), actual.objective().id());
    }

    @DisplayName("Should map dto to completed when calling toCompleted()")
    @Test
    void shouldMapDtoToCompleted() {
        // arrange
        var completedDto = CompletedDtoBuilder.builder().withId(COMPLETED_ID).withComment(COMPLETED_COMMENT)
                .withObjectiveDto(ObjectiveDtoBuilder.builder().withId(OBJECTIVE_ID).build()).build();

        // act
        var completed = completedMapper.toCompleted(completedDto);

        // assert
        assertNotNull(completed);
        assertCompleted(completedDto, completed);
    }

    private void assertCompleted(CompletedDto expected, Completed actual) {
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.comment(), actual.getComment());
        assertEquals(expected.objective().id(), actual.getObjective().getId());
    }

}
