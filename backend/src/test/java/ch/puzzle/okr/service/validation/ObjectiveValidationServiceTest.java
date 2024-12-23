package ch.puzzle.okr.service.validation;

import static ch.puzzle.okr.Constants.BACK_LOG_QUARTER_LABEL;
import static ch.puzzle.okr.test.TestConstants.BACK_LOG_QUARTER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.test.TestHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ObjectiveValidationServiceTest {
    @MockBean
    ObjectivePersistenceService objectivePersistenceService = Mockito.mock(ObjectivePersistenceService.class);

    Objective objective;
    Objective objectiveMinimal;
    User user;
    Quarter quarter;
    Team team;
    @Spy
    @InjectMocks
    private ObjectiveValidationService validator;

    private static Stream<Arguments> nameValidationArguments() {
        return Stream
                .of(arguments(StringUtils.repeat('1', 251),
                              List
                                      .of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("title", "Objective", "2", "250")))),
                    arguments(StringUtils.repeat('1', 1),
                              List
                                      .of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("title", "Objective", "2", "250")))),

                    arguments("",
                              List
                                      .of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("title", "Objective", "2", "250")),
                                          new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "Objective")))),

                    arguments(" ",
                              List
                                      .of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("title", "Objective", "2", "250")),
                                          new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "Objective")))),

                    arguments("         ", List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "Objective")))),
                    arguments(null,
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "Objective")),
                                          new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("title", "Objective")))));
    }

    @BeforeEach
    void setUp() {
        this.user = User.Builder
                .builder()
                .withId(1L)
                .withFirstName("Bob")
                .withLastName("Kaufmann")
                .withEmail("kaufmann@puzzle.ch")
                .build();
        this.team = Team.Builder.builder().withId(1L).withName("Team1").build();
        this.quarter = Quarter.Builder
                .builder()
                .withId(1L)
                .withLabel("GJ 22/23-Q2")
                .withStartDate(LocalDate.of(2022, 1, 1))
                .withEndDate(LocalDate.of(2022, 3, 31))
                .build();

        this.objective = Objective.Builder
                .builder()
                .withId(1L)
                .withTitle("Objective 1")
                .withCreatedBy(user)
                .withTeam(team)
                .withQuarter(quarter)
                .withDescription("This is our description")
                .withModifiedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withModifiedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .build();

        this.objectiveMinimal = Objective.Builder
                .builder()
                .withId(null)
                .withTitle("Objective 2")
                .withCreatedBy(user)
                .withTeam(team)
                .withQuarter(quarter)
                .withState(State.DRAFT)
                .withCreatedOn(LocalDateTime.MAX)
                .build();

        when(objectivePersistenceService.findById(1L)).thenReturn(objective);
        when(objectivePersistenceService.getModelName()).thenReturn("Objective");
        doThrow(new OkrResponseStatusException(HttpStatus.NOT_FOUND,
                                               String
                                                       .format("%s with id %s not found",
                                                               objectivePersistenceService.getModelName(),
                                                               2L)))
                .when(objectivePersistenceService)
                .findById(2L);
    }

    @DisplayName("Should be successful on validateOnGet() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnGetWhenValidObjectiveId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @DisplayName("Should be successful on validateOnCreate() when objective is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnGetWhenTeamIsValid() {
        validator.validateOnCreate(objectiveMinimal);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(objectiveMinimal);
        verify(validator, times(1)).validate(objectiveMinimal);
    }

    @DisplayName("Should throw exception on validateOnCreate() when objective is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnCreate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenIdIsNotNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(objective));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest(name = "should throw exception on validateOnCreate() when model has invalid name {0}")
    @MethodSource("nameValidationArguments")
    void shouldThrowExceptionOnValidateOnCreateWhenTitleIsInvalid(String title, List<ErrorDto> expectedErrors) {
        Objective objective = Objective.Builder
                .builder()
                .withId(null)
                .withTitle(title)
                .withCreatedBy(this.user)
                .withTeam(this.team)
                .withQuarter(this.quarter)
                .withDescription("This is our description 2")
                .withModifiedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withCreatedOn(LocalDateTime.MAX)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(objective));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnCreate() when attributes are missing")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenAttrsAreMissing() {
        Objective objectiveInvalid = Objective.Builder
                .builder()
                .withId(null)
                .withTitle("Title")
                .withQuarter(quarter)
                .build();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(objectiveInvalid));

        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("team", "Objective")),
                    new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "Objective")),
                    new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "Objective")),
                    new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("state", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnCreate() when model was modified")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenAttrModifiedByIsSet() {
        Objective objectiveInvalid = Objective.Builder
                .builder()
                .withId(null)
                .withTitle("ModifiedBy is not null on create")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withTeam(team)
                .withQuarter(quarter)
                .withModifiedBy(user)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(objectiveInvalid));
        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_SET_FORBIDDEN",
                                 List
                                         .of("ModifiedBy",
                                             "User{id=1, version=0, firstName='Bob', lastName='Kaufmann', email='kaufmann@puzzle.ch', okrChampion='false'}")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnCreate() when start date is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenStartDateIsNull() {
        Quarter invalidQuarter = Quarter.Builder
                .builder()
                .withId(1L)
                .withLabel("GJ-22/23-Q3")
                .withEndDate(LocalDate.of(2022, 7, 31))
                .build();
        Objective objectiveInvalid = Objective.Builder
                .builder()
                .withId(null)
                .withTitle("Start date is missing")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withTeam(team)
                .withQuarter(invalidQuarter)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(objectiveInvalid));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("StartDate", "GJ-22/23-Q3")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnCreate() when end date is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenEndDateIsNull() {
        Quarter invalidQuarter = Quarter.Builder
                .builder()
                .withId(1L)
                .withLabel("GJ-22/23-Q3")
                .withStartDate(LocalDate.of(2022, 4, 1))
                .build();
        Objective objectiveInvalid = Objective.Builder
                .builder()
                .withId(null)
                .withTitle("End date is missing")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withTeam(team)
                .withQuarter(invalidQuarter)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(objectiveInvalid));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("EndDate", "GJ-22/23-Q3")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be successful on validateOnCreate() when model is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenObjectiveIsValid() {
        validator.validateOnUpdate(objective.getId(), objective);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(objective);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(objective.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(objective.getId(), objective.getId());
        verify(validator, times(1)).validate(objective);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when model date is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(1L, null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(null, objectiveMinimal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(objectiveMinimal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id has changed")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdHasChanged() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(7L, objective));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(objective);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(objective.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(7L, objective.getId());
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", "7", "1")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest(name = "should throw exception on validateOnUpdate() when model has invalid name {0}")
    @MethodSource("nameValidationArguments")
    void shouldThrowExceptionOnValidateOnUpdateWhenTitleIsInvalid(String title, List<ErrorDto> expectedErrors) {
        Objective objective = Objective.Builder
                .builder()
                .withId(3L)
                .withTitle(title)
                .withCreatedBy(this.user)
                .withTeam(this.team)
                .withQuarter(this.quarter)
                .withDescription("This is our description 2")
                .withModifiedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withModifiedBy(this.user)
                .withCreatedOn(LocalDateTime.MAX)
                .build();
        when(objectivePersistenceService.findById(objective.getId())).thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(3L, objective));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when attributes are missing")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenAttrsAreMissing() {
        Objective objective = Objective.Builder
                .builder()
                .withId(5L)
                .withTitle("Title")
                .withQuarter(quarter)
                .withModifiedBy(user)
                .build();
        when(objectivePersistenceService.findById(objective.getId())).thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(5L, objective));
        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("team", "Objective")),
                    new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "Objective")),
                    new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "Objective")),
                    new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("state", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when modifiedBy is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenAttrModifiedByIsNotSet() {
        Objective objectiveInvalid = Objective.Builder
                .builder()
                .withId(1L)
                .withTitle("ModifiedBy is not null on create")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withTeam(team)
                .withQuarter(quarter)
                .withModifiedBy(null)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(1L, objectiveInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_SET", List.of("modifiedBy")));

        assertEquals(INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when start date is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenStartDateIsNull() {
        Quarter invalidQuarter = Quarter.Builder
                .builder()
                .withId(1L)
                .withLabel("GJ-22/23-Q3")
                .withEndDate(LocalDate.of(2022, 7, 31))
                .build();
        Objective objectiveInvalid = Objective.Builder
                .builder()
                .withId(1L)
                .withTitle("Start date is missing")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withTeam(team)
                .withQuarter(invalidQuarter)
                .withModifiedBy(user)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(1L, objectiveInvalid));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("StartDate", "GJ-22/23-Q3")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when end date is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenEndDateIsNull() {
        Quarter invalidQuarter = Quarter.Builder
                .builder()
                .withId(1L)
                .withLabel("GJ-22/23-Q3")
                .withStartDate(LocalDate.of(2022, 4, 1))
                .build();
        Objective objectiveInvalid = Objective.Builder
                .builder()
                .withId(1L)
                .withTitle("End date is missing")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withTeam(team)
                .withQuarter(invalidQuarter)
                .withModifiedBy(user)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(1L, objectiveInvalid));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("EndDate", "GJ-22/23-Q3")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when team has changed")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenTeamHasChanged() {
        Objective savedObjective = Objective.Builder
                .builder()
                .withId(1L)
                .withTitle("Team has changed")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withTeam(team)
                .withQuarter(quarter)
                .withModifiedBy(null)
                .build();
        Objective updatedObjective = Objective.Builder
                .builder()
                .withId(1L)
                .withTitle("Team has changed")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withTeam(Team.Builder.builder().withId(2L).withName("other team").build())
                .withQuarter(quarter)
                .withModifiedBy(user)
                .build();
        when(objectivePersistenceService.findById(savedObjective.getId())).thenReturn(savedObjective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(1L, updatedObjective));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_CANNOT_CHANGE", List.of("Team", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest(name = "should throw exception on validateOnCreate() when the objective is in the backlog and the state is {0} instead of draft")
    @EnumSource(value = State.class, names = { "DRAFT" }, mode = EnumSource.Mode.EXCLUDE)
    void shouldThrowExceptionOnValidateOnCreateWhenQuarterIsBacklogAndStateIsNotDraft(State state) {
        Quarter backlogQuarter = Quarter.Builder
                .builder()
                .withId(BACK_LOG_QUARTER_ID)
                .withLabel(BACK_LOG_QUARTER_LABEL)
                .withStartDate(null)
                .withEndDate(null)
                .build();

        Objective invalidObjective = Objective.Builder
                .builder()
                .withTitle("Invalid Objective")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(state)
                .withTeam(team)
                .withQuarter(backlogQuarter)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(invalidObjective));
        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_MUST_BE_DRAFT", List.of("Objective", "Draft", state.toString())));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest(name = "should throw exception on validateOnUpdate() when the objective is in the backlog and the state is {0} instead of DRAFT")
    @EnumSource(value = State.class, names = { "DRAFT" }, mode = EnumSource.Mode.EXCLUDE)
    void shouldThrowExceptionOnValidateOnUpdateWhenQuarterIsBacklogAndStateIsNotDraft(State state) {
        Quarter backlogQuarter = Quarter.Builder
                .builder()
                .withId(BACK_LOG_QUARTER_ID)
                .withLabel(BACK_LOG_QUARTER_LABEL)
                .withStartDate(null)
                .withEndDate(null)
                .build();

        Objective invalidObjective = Objective.Builder
                .builder()
                .withId(1L)
                .withTitle("Invalid Objective")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(state)
                .withTeam(team)
                .withQuarter(backlogQuarter)
                .withModifiedBy(user)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(1L, invalidObjective));
        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_MUST_BE_DRAFT", List.of("Objective", "Draft", state.toString())));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be successful on validateOnUpdate() when the objective is in the backlog and the state is DRAFT")
    @Test
    void shouldPassOnValidateOnUpdateWhenQuarterIsBacklogAndStateIsDraft() {
        Quarter backlogQuarter = Quarter.Builder
                .builder()
                .withId(BACK_LOG_QUARTER_ID)
                .withLabel(BACK_LOG_QUARTER_LABEL)
                .withStartDate(null)
                .withEndDate(null)
                .build();

        Objective validObjective = Objective.Builder
                .builder()
                .withId(1L)
                .withTitle("Invalid Objective")
                .withCreatedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withTeam(team)
                .withQuarter(backlogQuarter)
                .withModifiedBy(user)
                .build();

        assertDoesNotThrow(() -> validator.validateOnUpdate(1L, validObjective));
    }

    @DisplayName("Should throw exception on validateOnGet() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnGetWhenObjectiveIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }
}
