package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static ch.puzzle.okr.test.AssertionHelper.assertOkrResponseStatusException;
import static ch.puzzle.okr.Constants.KEY_RESULT;
import static ch.puzzle.okr.Constants.OBJECTIVE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyResultValidationServiceTest {
    @MockBean
    KeyResultPersistenceService keyResultPersistenceService = Mockito.mock(KeyResultPersistenceService.class);

    private final User user = User.Builder.builder().withId(1L).withFirstName("Bob").withLastName("Kaufmann")
            .withEmail("kaufmann@puzzle.ch").build();
    private final Quarter quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
    private final Team team = Team.Builder.builder().withId(1L).withName("Team1").build();
    private final Objective objective = Objective.Builder.builder().withId(1L).withTitle("Objective 1")
            .withCreatedBy(user).withTeam(team).withQuarter(quarter).withDescription("This is our description")
            .withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(user)
            .withCreatedOn(LocalDateTime.MAX).build();
    private final KeyResult keyResultMetric = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0)
            .withUnit(Unit.NUMBER).withId(5L).withTitle("Keyresult Metric").withObjective(objective).withOwner(user)
            .build();
    private final KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder().withCommitZone("Ein Baum")
            .withTargetZone("Zwei Bäume").withTitle("Keyresult Ordinal").withObjective(objective).withOwner(user)
            .build();
    private final KeyResult fullKeyResult = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0)
            .withUnit(Unit.FTE).withId(null).withTitle("Keyresult Metric").withObjective(objective).withOwner(user)
            .withCreatedOn(LocalDateTime.MIN).withModifiedOn(LocalDateTime.MAX).withDescription("Description")
            .withCreatedBy(user).build();

    @BeforeEach
    void setUp() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(keyResultMetric);
        when(keyResultPersistenceService.getModelName()).thenReturn("KeyResult");
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("%s with id %s not found", keyResultPersistenceService.getModelName(), 2L)))
                        .when(keyResultPersistenceService).findById(2L);
    }

    @Spy
    @InjectMocks
    private KeyResultValidationService validator;

    private static Stream<Arguments> nameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 251),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "KeyResult", "2", "250")))),
                arguments(StringUtils.repeat('1', 1),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "KeyResult", "2", "250")))),
                arguments("",
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "KeyResult", "2", "250")),
                                new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "KeyResult")))),
                arguments(" ",
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "KeyResult", "2", "250")),
                                new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "KeyResult")))),
                arguments("         ", List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "KeyResult")))),
                arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("title", "KeyResult")),
                        new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "KeyResult")))));
    }

    @DisplayName("Should be successful on validateOnGet() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnGetWhenValidKeyResultId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @DisplayName("Should throw exception on validateOnGet() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnGetIfKeyResultIdIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "KeyResult")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should be successful on validateOnCreate() when key result is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenKeyResultIsValid() {
        validator.validateOnCreate(fullKeyResult);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(fullKeyResult);
        verify(validator, times(1)).validate(fullKeyResult);
    }

    @DisplayName("Should throw exception on validateOnCreate() when key result is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenModelIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("KeyResult")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnCreate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenIdIsNotNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultMetric));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "KeyResult")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @ParameterizedTest(name = "should throw exception on validateOnCreate() when model has invalid name {0}")
    @MethodSource("nameValidationArguments")
    void shouldThrowExceptionOnValidateOnCreateWhenTitleIsInvalid(String title, List<ErrorDto> errors) {
        // arrange
        KeyResult keyResult = KeyResultMetric.Builder.builder() //
                .withBaseline(3.0) //
                .withStretchGoal(5.0) //
                .withUnit(Unit.EUR) //
                .withId(null) //
                .withTitle(title) //
                .withOwner(user) //
                .withObjective(objective) //
                .withCreatedBy(user) //
                .withCreatedOn(LocalDateTime.MIN) //
                .build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(keyResult));
        assertOkrResponseStatusException(exception, errors);
    }

    @DisplayName("Should throw exception on validateOnCreate() when attributes are missing")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenAttrsAreMissing() {
        // arrange
        KeyResult keyResultInvalid = KeyResultMetric.Builder.builder() //
                .withId(null) //
                .withTitle("Title") //
                .build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultInvalid));

        List<ErrorDto> expectedErrors = List.of( //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("owner", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("stretchGoal", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("objective", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("baseline", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("unit", "KeyResult")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should be successful on validateOnUpdate() when model is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnUpdateWhenKeyResultIsValid() {
        // arrange
        Long id = 5L;
        KeyResult keyResult = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0)
                .withUnit(Unit.EUR).withId(id).withTitle("Keyresult Metric").withObjective(objective).withOwner(user)
                .withCreatedOn(LocalDateTime.MIN).withModifiedOn(LocalDateTime.MAX).withDescription("Description")
                .withCreatedBy(user).build();
        when(keyResultPersistenceService.findById(id)).thenReturn(keyResult);

        // act
        validator.validateOnUpdate(id, keyResult);

        // assert
        verify(validator, times(1)).throwExceptionWhenModelIsNull(keyResult);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(keyResult.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(keyResult.getId(), keyResult.getId());
        verify(validator, times(1)).validate(keyResult);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when model is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenModelIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("KeyResult")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(null, keyResultOrdinal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(keyResultOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "KeyResult")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id has changed")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdHasChanged() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, keyResultMetric));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(keyResultMetric);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(keyResultMetric.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(1L, keyResultMetric.getId());

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", "1", "5")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @ParameterizedTest(name = "should throw exception on validateOnUpdate() when model has invalid name {0}")
    @MethodSource("nameValidationArguments")
    void shouldThrowExceptionOnValidateOnUpdateWhenTitleIsInvalid(String title, List<ErrorDto> errors) {
        // arrange
        Long id = 3L;
        KeyResult keyResult = KeyResultMetric.Builder.builder() //
                .withBaseline(3.0) //
                .withStretchGoal(5.0) //
                .withUnit(Unit.FTE) //
                .withId(id) //
                .withTitle(title) //
                .withOwner(user) //
                .withObjective(objective) //
                .withCreatedBy(user) //
                .withCreatedOn(LocalDateTime.MIN) //
                .build();
        when(keyResultPersistenceService.findById(id)).thenReturn(keyResult);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, keyResult));

        assertOkrResponseStatusException(exception, errors);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when attributes are missing")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenAttrsAreMissing() {
        // arrange
        Long id = 11L;
        KeyResult keyResultInvalid = KeyResultMetric.Builder.builder() //
                .withId(id) //
                .withTitle("Title") //
                .withObjective(objective) //
                .build();
        when(keyResultPersistenceService.findById(id)).thenReturn(keyResultInvalid);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, keyResultInvalid));

        List<ErrorDto> expectedErrors = List.of( //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("baseline", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("stretchGoal", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("unit", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "KeyResult")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("owner", "KeyResult")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should be successful on validateOnDelete() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnDeleteWhenValidKeyResultId() {
        validator.validateOnDelete(1L);

        verify(validator, times(1)).validateOnDelete(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @DisplayName("Should throw exception on validateOnDelete() when id is valid")
    @Test
    void shouldThrowExceptionOnValidateOnDeleteWhenKeyResultIdIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnDelete(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "KeyResult")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when objective id (of input and saved KeyResult) has changed")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenObjectiveIdOfKeyResultHasChanged() {
        // arrange
        Long keyResultId = 1L;
        Long objectiveId = 2L;
        Objective objective = Objective.Builder.builder().withId(objectiveId).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder() //
                .withId(keyResultId) //
                .withObjective(objective).build();

        Long savedObjectiveId = 3L;
        Objective savedObjective = Objective.Builder.builder().withId(savedObjectiveId).build();
        KeyResult savedKeyResultWithDifferentObjectiveId = KeyResultMetric.Builder.builder() //
                .withId(keyResultId) //
                .withObjective(savedObjective).build();

        when(keyResultPersistenceService.findById(keyResultId)).thenReturn(savedKeyResultWithDifferentObjectiveId);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(keyResultId, keyResult));

        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_CANNOT_CHANGE", List.of(OBJECTIVE, KEY_RESULT)));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

}
