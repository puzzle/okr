package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.AssertionHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static ch.puzzle.okr.AssertionHelper.assertOkrResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ExtendWith(MockitoExtension.class)
class ObjectiveValidationServiceTest {
    @MockBean
    ObjectivePersistenceService objectivePersistenceService = Mockito.mock(ObjectivePersistenceService.class);

    @Spy
    @InjectMocks
    private ObjectiveValidationService validator;

    private static final State STATE_NOT_DRAFT = State.SUCCESSFUL;

    private static final User user = User.Builder.builder() //
            .withId(1L) //
            .withFirstname("Bob") //
            .withLastname("Kaufmann") //
            .withUsername("bkaufmann") //
            .withEmail("kaufmann@puzzle.ch").build();

    private static final Quarter quarter = Quarter.Builder.builder() //
            .withId(1L) //
            .withLabel("GJ 22/23-Q2") //
            .withStartDate(LocalDate.of(2022, 1, 1)) //
            .withEndDate(LocalDate.of(2022, 3, 31)).build();

    private static final Team team = Team.Builder.builder() //
            .withId(1L) //
            .withName("Team1").build();

    private static Stream<Arguments> validBacklogObjectives() {
        return Stream.of( //
                Arguments.of(customizedObjective("Backlog", State.DRAFT, null, null)), //
                Arguments.of(customizedObjective("Backlog", STATE_NOT_DRAFT, LocalDate.now(), null)), //
                Arguments.of(customizedObjective("Backlog", STATE_NOT_DRAFT, null, LocalDate.now())) //
        );
    }

    private static Stream<Arguments> invalidBacklogObjectives() {
        return Stream.of(//
                Arguments.of(customizedObjective("Backlog", STATE_NOT_DRAFT, null, null)) //
        );
    }

    private static Objective customizedObjective(String label, State state, LocalDate startDate, LocalDate endDate) {
        return Objective.Builder.builder() //
                .withId(1L) //
                .withTitle("Objective with label and state") //
                .withCreatedBy(user) //
                .withCreatedOn(LocalDateTime.MAX) //
                .withState(state) //
                .withTeam(team) //
                .withQuarter(Quarter.Builder.builder() //
                        .withId(199L) //
                        .withLabel(label) //
                        .withStartDate(startDate) //
                        .withEndDate(endDate) //
                        .build()) //
                .withModifiedBy(user) //
                .build();
    }

    @DisplayName("validateOnCreate() should be successful when team is valid")
    @Test
    void validateOnCreateShouldBeSuccessfulWhenTeamIsValid() {
        // arrange
        Objective objectiveMinimal = Objective.Builder.builder() //
                .withId(null) //
                .withTitle("Objective") //
                .withCreatedBy(user) //
                .withTeam(team) //
                .withQuarter(quarter) //
                .withState(State.DRAFT) //
                .withCreatedOn(LocalDateTime.MAX) //
                .build();

        // act
        validator.validateOnCreate(objectiveMinimal);

        // assert
        verify(validator, times(1)).throwExceptionWhenModelIsNull(objectiveMinimal);
        verify(validator, times(1)).validate(objectiveMinimal);
    }

    @DisplayName("validateOnUpdate() should be successful when ModifiedBy is not null")
    @Test
    void validateOnUpdateShouldBeSuccessfulWhenModifiedByIsNotNull() {
        // arrange
        User modifiedBy = user;
        Objective objectiveValid = Objective.Builder.builder() //
                .withId(1L) //
                .withTitle("ModifiedBy is not null on create") //
                .withCreatedBy(user) //
                .withCreatedOn(LocalDateTime.MAX) //
                .withState(State.DRAFT) //
                .withTeam(team) //
                .withQuarter(quarter) //
                .withModifiedBy(modifiedBy) //
                .build();

        when(objectivePersistenceService.findById(1L)).thenReturn(objectiveValid);

        // act + assert
        assertDoesNotThrow(() -> validator.validateOnUpdate(1L, objectiveValid));
    }

    @DisplayName("validateOnCreate() should throw exception when ModifiedBy is set")
    @Test
    void validateOnCreateShouldThrowExceptionWhenModifiedByIsSet() {
        // arrange
        User modifiedBy = user;
        Objective objectiveInvalid = Objective.Builder.builder() //
                .withId(null) //
                .withTitle("ModifiedBy is not null on create") //
                .withCreatedBy(user) //
                .withCreatedOn(LocalDateTime.MAX) //
                .withState(State.DRAFT) //
                .withTeam(team) //
                .withQuarter(quarter) //
                .withModifiedBy(modifiedBy) //
                .build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_SET_FORBIDDEN", List.of("ModifiedBy",
                "User{id=1, version=0, username='bkaufmann', firstname='Bob', lastname='Kaufmann', email='kaufmann@puzzle.ch', writeable=false}")));

        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("validateOnUpdate() should throw exception when ModifiedBy is null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenModifiedByIsNull() {
        // arrange
        User modifiedBy = null;
        Objective objectiveInvalid = Objective.Builder.builder() //
                .withId(1L) //
                .withTitle("ModifiedBy is not null on create") //
                .withCreatedBy(user) //
                .withCreatedOn(LocalDateTime.MAX) //
                .withState(State.DRAFT) //
                .withTeam(team) //
                .withQuarter(quarter) //
                .withModifiedBy(modifiedBy) //
                .build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, objectiveInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_SET", List.of("modifiedBy")));
        AssertionHelper.assertOkrResponseStatusException(INTERNAL_SERVER_ERROR, exception, expectedErrors);
    }

    @DisplayName("validateOnUpdate() should throw exception when team has changed")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenTeamHasChanged() {
        // arrange
        Objective savedObjectiveWithoutTeam = Objective.Builder.builder() //
                .withId(1L) //
                .withTitle("Team has changed") //
                .withCreatedBy(user) //
                .withCreatedOn(LocalDateTime.MAX) //
                .withState(State.DRAFT).withTeam(team) //
                .withTeam(null) //
                .withQuarter(quarter) //
                .withModifiedBy(null) //
                .build();

        Objective updatedObjectiveWithTeam = Objective.Builder.builder() //
                .withId(1L) //
                .withTitle("Team has changed") //
                .withCreatedBy(user) //
                .withCreatedOn(LocalDateTime.MAX) //
                .withState(State.DRAFT) //
                .withTeam(team) //
                .withQuarter(quarter) //
                .withModifiedBy(user) //
                .build();

        when(objectivePersistenceService.findById(savedObjectiveWithoutTeam.getId()))
                .thenReturn(savedObjectiveWithoutTeam);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, updatedObjectiveWithTeam));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_CANNOT_CHANGE", List.of("Team", "Objective")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("validateOnUpdate() should be successful when Objective is valid Backlog Objective")
    @ParameterizedTest
    @MethodSource("validBacklogObjectives")
    void validateOnUpdateShouldBeSuccessfulWhenObjectiveIsValidBacklogObjective(Objective validBacklogObjective) {
        // arrange
        when(objectivePersistenceService.findById(1L)).thenReturn(validBacklogObjective);

        // act + assert
        assertDoesNotThrow(() -> validator.validateOnUpdate(1L, validBacklogObjective));
    }

    @DisplayName("validateOnUpdate() should throw exception when Objective is invalid Backlog Objective")
    @ParameterizedTest
    @MethodSource("invalidBacklogObjectives")
    void validateOnUpdateShouldThrowExceptionWhenObjectiveIsInvalidBacklogObjective(Objective invalidBacklogObjective) {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, invalidBacklogObjective));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_MUST_BE_DRAFT",
                List.of("Objective", "Draft", invalidBacklogObjective.getState().toString())));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

}
