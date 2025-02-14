package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.exception.OkrResponseStatusException.of;
import static ch.puzzle.okr.test.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.State;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

// tests are using data from V100_0_0__TestData.sql
@SpringIntegrationTest
class ObjectivePersistenceServiceIT {
    private static final long INVALID_OBJECTIVE_ID = 321L;
    private static final long INVALID_KEY_RESULT_ID = 321L;
    private static final long INVALID_CHECK_IN_ID = 321L;
    private static final long INVALID_TEAM_ID = 321L;
    private static final long INVALID_QUARTER_ID = 12L;

    private static final long ID_OF_OBJECTIVE_3 = 3L;
    private static final long ID_OF_OBJECTIVE_8 = 8L;
    private static final long ID_OF_OBJECTIVE_9 = 9L;
    private static final long ID_OF_OBJECTIVE_10 = 10L;

    private static final String TITLE_OF_OBJECTIVE_3 = "Wir wollen die Kundenzufriedenheit steigern";
    private static final String TITLE_OF_OBJECTIVE_8 = "consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua";
    private static final String TITLE_OF_OBJECTIVE_9 = "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
    private static final String TITLE_OF_OBJECTIVE_10 = "should not appear on staging, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

    private static final long ID_OF_KEY_RESULT_5 = 5L;
    private static final long ID_OF_CHECK_IN_7 = 7L;
    private static final long ID_OF_TEAM_6 = 6L;

    private static final String REASON_UNAUTHORIZED = "not authorized to read objective";
    private static final OkrResponseStatusException NO_RESULT_EXCEPTION = of(REASON_UNAUTHORIZED);

    private static final String OBJECTIVE = "Objective";
    private static final String ATTRIBUTE_NULL = "ATTRIBUTE_NULL";
    private static final long CURRENT_QUARTER_ID = 2L;

    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    @MockitoSpyBean
    private ObjectiveRepository objectiveRepository;

    @Autowired
    private ObjectivePersistenceService objectivePersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @AfterEach
    void tearDown() {
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Should return correct objective on findObjectiveById()")
    @Test
    void shouldFindObjectiveById() {
        // act
        var objective = objectivePersistenceService
                .findObjectiveById(ID_OF_OBJECTIVE_3, authorizationUser, NO_RESULT_EXCEPTION);

        // assert
        assertObjective(ID_OF_OBJECTIVE_3, TITLE_OF_OBJECTIVE_3, objective);
    }

    @DisplayName("Should throw exception on findObjectiveById() when objective not found")
    @Test
    void shouldThrowExceptionWhenObjectiveNotFoundOnFindObjectiveById() {
        // act
        var exception = assertThrows(OkrResponseStatusException.class,
                                     () -> objectivePersistenceService
                                             .findObjectiveById(INVALID_OBJECTIVE_ID,
                                                                authorizationUser,
                                                                NO_RESULT_EXCEPTION));

        // assert
        var expectedErrors = List.of(new ErrorDto(REASON_UNAUTHORIZED, List.of()));
        assertResponseStatusException(UNAUTHORIZED, expectedErrors, exception);
    }

    @DisplayName("Should throw exception on findObjectiveById() when objective id is null")
    @Test
    void shouldThrowExceptionWhenObjectiveIdIsNullOnFindObjectiveById() {
        // act
        var exception = assertThrows(OkrResponseStatusException.class,
                                     () -> objectivePersistenceService
                                             .findObjectiveById(null, authorizationUser, NO_RESULT_EXCEPTION));

        // assert
        var expectedErrors = List.of(new ErrorDto(ATTRIBUTE_NULL, List.of("ID", OBJECTIVE)));
        assertResponseStatusException(BAD_REQUEST, expectedErrors, exception);
    }

    @DisplayName("Should return correct objective on findObjectiveByKeyResultId()")
    @Test
    void shouldFindObjectiveByKeyResultId() {
        // act
        var objective = objectivePersistenceService
                .findObjectiveByKeyResultId(ID_OF_KEY_RESULT_5, authorizationUser, NO_RESULT_EXCEPTION);

        // assert
        assertObjective(ID_OF_OBJECTIVE_3, TITLE_OF_OBJECTIVE_3, objective);
    }

    @DisplayName("Should throw exception on findObjectiveByKeyResultId() when objective not found")
    @Test
    void shouldThrowExceptionWhenObjectiveNotFoundOnFindObjectiveByKeyResultId() {
        // act
        var exception = assertThrows(OkrResponseStatusException.class,
                                     () -> objectivePersistenceService
                                             .findObjectiveByKeyResultId(INVALID_KEY_RESULT_ID,
                                                                         authorizationUser,
                                                                         NO_RESULT_EXCEPTION));

        // assert
        var expectedErrors = List.of(new ErrorDto(REASON_UNAUTHORIZED, List.of()));
        assertResponseStatusException(UNAUTHORIZED, expectedErrors, exception);
    }

    @DisplayName("Should throw exception on findObjectiveByKeyResultId() when objective id is null")
    @Test
    void shouldThrowExceptionWhenObjectiveIdIsNullOnFindObjectiveByKeyResultId() {
        // act
        var exception = assertThrows(OkrResponseStatusException.class,
                                     () -> objectivePersistenceService
                                             .findObjectiveByKeyResultId(null, authorizationUser, NO_RESULT_EXCEPTION));

        // assert
        var expectedErrors = List.of(new ErrorDto(ATTRIBUTE_NULL, List.of("ID", OBJECTIVE)));
        assertResponseStatusException(BAD_REQUEST, expectedErrors, exception);
    }

    @DisplayName("Should return correct objective on findObjectiveByCheckInId()")
    @Test
    void shouldFindObjectiveByCheckInId() {
        // act
        var objective = objectivePersistenceService
                .findObjectiveByCheckInId(ID_OF_CHECK_IN_7, authorizationUser, NO_RESULT_EXCEPTION);

        // assert
        assertObjective(ID_OF_OBJECTIVE_3, TITLE_OF_OBJECTIVE_3, objective);
    }

    @DisplayName("Should throw exception on findObjectiveByCheckInId() when objective not found")
    @Test
    void shouldThrowExceptionWhenObjectiveNotFoundOnFindObjectiveByCheckInId() {
        // act
        var exception = assertThrows(OkrResponseStatusException.class,
                                     () -> objectivePersistenceService
                                             .findObjectiveByCheckInId(INVALID_CHECK_IN_ID,
                                                                       authorizationUser,
                                                                       NO_RESULT_EXCEPTION));

        // assert
        var expectedErrors = List.of(new ErrorDto(REASON_UNAUTHORIZED, List.of()));
        assertResponseStatusException(UNAUTHORIZED, expectedErrors, exception);
    }

    @DisplayName("Should throw exception on findObjectiveByCheckInId() when objective id is null")
    @Test
    void shouldThrowExceptionWhenObjectiveIdIsNullOnFindObjectiveByCheckInId() {
        // act
        var exception = assertThrows(OkrResponseStatusException.class,
                                     () -> objectivePersistenceService
                                             .findObjectiveByCheckInId(null,
                                                                       authorizationUser,
                                                                       ObjectivePersistenceServiceIT.NO_RESULT_EXCEPTION));

        // assert
        var expectedErrors = List.of(new ErrorDto(ATTRIBUTE_NULL, List.of("ID", OBJECTIVE)));
        assertResponseStatusException(BAD_REQUEST, expectedErrors, exception);
    }

    @DisplayName("Should return correct objective on findObjectiveByTeamId()")
    @Test
    void shouldFindObjectivesByTeamId() {
        // act
        var objectives = objectivePersistenceService.findObjectiveByTeamId(ID_OF_TEAM_6);

        // assert
        assertEquals(3, objectives.size());
        assertObjective(ID_OF_OBJECTIVE_8, TITLE_OF_OBJECTIVE_8, objectives.get(0));
        assertObjective(ID_OF_OBJECTIVE_9, TITLE_OF_OBJECTIVE_9, objectives.get(1));
        assertObjective(ID_OF_OBJECTIVE_10, TITLE_OF_OBJECTIVE_10, objectives.get(2));
    }

    @DisplayName("Should return empty list on findObjectiveByTeamId() when objective not found")
    @Test
    void shouldReturnEmptyListWhenObjectiveNotFoundOnFindObjectiveByTeamId() {
        // act
        var objectives = objectivePersistenceService.findObjectiveByTeamId(INVALID_TEAM_ID);

        // assert
        assertTrue(objectives.isEmpty());
    }

    @DisplayName("Should return empty list on findObjectiveByTeamId() when objective id is null")
    @Test
    void shouldReturnEmptyListWhenObjectiveIdIsNullOnFindObjectiveByTeamId() {
        // act
        var objectives = objectivePersistenceService.findObjectiveByTeamId(null);

        // assert
        assertTrue(objectives.isEmpty());
    }

    @DisplayName("Should return correct number of objectives for current quarter on findObjectiveByTeamId()")
    @Test
    void shouldReturnNumberOfObjectivesForCurrentQuarterOnCountByTeamAndQuarter() {
        // arrange: there are 3 objectives for the current quarter (id 2) for team with
        // id 6
        var team = Team.Builder.builder().withId(ID_OF_TEAM_6).build();
        var quarter = Quarter.Builder.builder().withId(CURRENT_QUARTER_ID).build();

        // act
        var count = objectivePersistenceService.countByTeamAndQuarter(team, quarter);

        // assert
        assertEquals(3, count);
    }

    @DisplayName("Should return zero on findObjectiveByTeamId() when team or quarter is not valid or null")
    @ParameterizedTest
    @MethodSource("invalidTeamsAndQuarters")
    void countByTeamAndQuarterShouldReturnZeroWhenTeamOrQuarterIsNotValidOrNull(Team team, Quarter quarter) {
        // act
        var count = objectivePersistenceService.countByTeamAndQuarter(team, quarter);

        // assert
        assertEquals(0, count);
    }

    private static Stream<Arguments> invalidTeamsAndQuarters() {
        var validTeam = Team.Builder.builder().withId(ID_OF_TEAM_6).build();
        var invalidTeam = Team.Builder.builder().withId(INVALID_TEAM_ID).build();
        var validQuarter = Quarter.Builder.builder().withId(CURRENT_QUARTER_ID).build();
        var invalidQuarter = Quarter.Builder.builder().withId(INVALID_QUARTER_ID).build();

        return Stream
                .of(
                    // valid team + invalid quarter
                    arguments(validTeam, invalidQuarter),
                    // valid team + null quarter
                    arguments(validTeam, null),
                    // invalid team + valid quarter
                    arguments(invalidTeam, validQuarter),
                    // invalid team + null quarter
                    arguments(null, validQuarter),
                    // invalid team + invalid quarter
                    arguments(invalidTeam, invalidQuarter),
                    // null team + null quarter
                    arguments(null, null));
    }

    @DisplayName("Should return objective on getModelName()")
    @Test
    void shouldReturnObjectiveOnGetModelName() {
        assertEquals(OBJECTIVE, objectivePersistenceService.getModelName());
    }

    @DisplayName("Should mark as deleted on deleteById() per default")
    @Test
    void shouldMarkAsDeletedOnMethodCall() {
        // arrange
        var entity = Objective.Builder
                .builder()
                .withTitle("title")
                .withCreatedBy(glUser())
                .withQuarter(Quarter.Builder.builder().withId(2L).build())
                .withTeam(defaultTeam(4L))
                .withCreatedOn(LocalDateTime.now())
                .withModifiedOn(LocalDateTime.now())
                .withState(State.SUCCESSFUL)
                .build();
        var newEntity = objectivePersistenceService.save(entity);

        long entityId = newEntity.getId();

        // act
        objectivePersistenceService.deleteById(entityId);

        // assert
        assertTrue(objectivePersistenceService.findById(entityId).isDeleted());
        Mockito.verify(objectiveRepository, Mockito.times(1)).markAsDeleted(entityId);
    }

    private void assertResponseStatusException(HttpStatus expectedStatus, List<ErrorDto> expectedErrors,
                                               OkrResponseStatusException currentException) {
        assertEquals(expectedStatus, currentException.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(currentException.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(currentException.getReason()));
    }

    private void assertObjective(Long expectedId, String expectedTitle, Objective currentObjective) {
        assertEquals(expectedId, currentObjective.getId());
        assertEquals(expectedTitle, currentObjective.getTitle());
    }

}
