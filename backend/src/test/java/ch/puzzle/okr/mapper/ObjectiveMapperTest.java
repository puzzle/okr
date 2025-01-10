package ch.puzzle.okr.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.State;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.business.QuarterBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ObjectiveMapperTest {

    private static final long TEAM_ID = 23L;
    private static final long QUARTER_ID = 10L;
    private static final String QUARTER_LABEL = "GJ 2024 Q1";
    private static final long ID = 0L;
    private static final int VERSION = 1;
    private static final String TITLE = "objective title";
    private static final String DESCRIPTION = "objective description";
    private static final State STATE = State.DRAFT;
    private static final boolean IS_WRITEABLE = true;

    private static final LocalDateTime CREATE_DATE_TIME = LocalDateTime.of(2024, Month.MAY, 20, 12, 35, 0);
    private static final LocalDateTime MODIFIED_DATE_TIME = LocalDateTime.of(2024, Month.MAY, 21, 8, 0, 0);

    private final Team team = Team.Builder.builder().withId(TEAM_ID).build();
    private final Quarter quarter = Quarter.Builder
            .builder() //
            .withId(QUARTER_ID) //
            .withLabel(QUARTER_LABEL) //
            .build();

    private ObjectiveMapper objectiveMapper;

    @Mock
    private TeamBusinessService teamBusinessService;

    @Mock
    private QuarterBusinessService quarterBusinessService;

    @BeforeEach
    void setup() {
        objectiveMapper = new ObjectiveMapper(teamBusinessService, quarterBusinessService);
    }

    @DisplayName("Should map objective to dto when calling toDto()")
    @Test
    void shouldMapObjectiveToDto() {
        // arrange
        Objective objective = Objective.Builder
                .builder() //
                .withId(ID) //
                .withVersion(VERSION) //
                .withTitle(TITLE) //
                .withTeam(team) //
                .withQuarter(quarter) //
                .withDescription(DESCRIPTION) //
                .withState(STATE) //
                .withCreatedOn(CREATE_DATE_TIME) //
                .withModifiedOn(MODIFIED_DATE_TIME) //
                .build();
        objective.setWriteable(IS_WRITEABLE);

        // act
        ObjectiveDto objectiveDto = objectiveMapper.toDto(objective);

        // assert
        assertNotNull(objectiveDto);
        assertObjectiveDto(objective, objectiveDto);
    }

    private void assertObjectiveDto(Objective expected, ObjectiveDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getVersion(), actual.version());
        assertEquals(expected.getTitle(), actual.title());
        assertEquals(expected.getTeam().getId(), actual.teamId());
        assertEquals(expected.getQuarter().getId(), actual.quarterId());
        assertEquals(expected.getQuarter().getLabel(), actual.quarterLabel());
        assertEquals(expected.getDescription(), actual.description());
        assertEquals(expected.getState(), actual.state());
        assertEquals(expected.getCreatedOn(), actual.createdOn());
        assertEquals(expected.getModifiedOn(), actual.modifiedOn());
    }

    @DisplayName("Should map dto to objective when calling toObjective()")
    @Test
    void shouldMapDtoToObjective() {
        // arrange
        when(teamBusinessService.getTeamById(TEAM_ID)).thenReturn(team);
        when(quarterBusinessService.getQuarterById(QUARTER_ID)).thenReturn(quarter);

        ObjectiveDto objectiveDto = new ObjectiveDto( //
                ID, //
                VERSION, //
                TITLE, //
                TEAM_ID, //
                QUARTER_ID, //
                QUARTER_LABEL, //
                DESCRIPTION, //
                STATE, //
                CREATE_DATE_TIME, //
                MODIFIED_DATE_TIME, //
                IS_WRITEABLE //
        );

        // mock (LocalDateTime.now()) + act + assert
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(MODIFIED_DATE_TIME);

            // act
            Objective objective = objectiveMapper.toObjective(objectiveDto);

            // assert
            assertNotNull(objectiveDto);
            assertObjective(objectiveDto, objective);
        }
    }

    private void assertObjective(ObjectiveDto expected, Objective actual) {
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.version(), actual.getVersion());
        assertEquals(expected.title(), actual.getTitle());
        assertEquals(expected.teamId(), actual.getTeam().getId());
        assertEquals(expected.quarterId(), actual.getQuarter().getId());
        assertEquals(expected.quarterLabel(), actual.getQuarter().getLabel());
        assertEquals(expected.description(), actual.getDescription());
        assertEquals(expected.state(), actual.getState());
        assertEquals(expected.createdOn(), actual.getCreatedOn());
        assertEquals(expected.modifiedOn(), actual.getModifiedOn());
        assertFalse(actual.isWriteable());
    }
}
