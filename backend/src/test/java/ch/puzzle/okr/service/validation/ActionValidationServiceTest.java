package ch.puzzle.okr.service.validation;

import java.util.List;
import java.util.stream.Stream;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static ch.puzzle.okr.test.AssertionHelper.assertOkrResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ActionValidationServiceTest {
    private final KeyResult keyResult = KeyResultMetric.Builder.builder() //
                                                               .withId(10L) //
                                                               .withTitle("KR Title")
                                                               .build(); //

    private final Action action1 = Action.Builder.builder() //
                                                 .withId(null) //
                                                 .withAction("Neue Katze") //
                                                 .withIsChecked(false) //
                                                 .withPriority(0) //
                                                 .withKeyResult(keyResult)
                                                 .build();

    private final Action action2 = Action.Builder.builder() //
                                                 .withId(2L) //
                                                 .withAction("Neues Lama") //
                                                 .withIsChecked(true) // //
                                                 .withPriority(1)
                                                 .withKeyResult(keyResult)
                                                 .build();

    @Mock
    ActionPersistenceService actionPersistenceService;

    @Mock
    KeyResultValidationService keyResultValidationService;

    @Spy @InjectMocks
    private ActionValidationService validator;

    private static Stream<Arguments> actionValidationArguments() {
        return Stream.of(arguments(StringUtils.repeat('1', 5000),
                                   List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                        List.of("action", "Action", "0", "4096")))),
                         arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("action", "Action")))));
    }

    private record ActionPair(Action action, Action saveAction) {
    }

    // generate Pairs of Actions with and without KeyResults
    private static Stream<Arguments> actionPairArgument() {
        Long id = 3L;
        KeyResult keyResult = KeyResultMetric.Builder.builder()
                                                     .withId(10L)
                                                     .withTitle("KR Title")
                                                     .build(); //

        return Stream.of( //
                         Arguments.of(new ActionPair( //
                                                     Action.Builder.builder() //
                                                                   .withId(id)
                                                                   .withAction("Action")
                                                                   .withIsChecked(false)
                                                                   .withPriority(1) //
                                                                   .withKeyResult(null)
                                                                   .build(),

                                                     Action.Builder.builder() //
                                                                   .withId(id)
                                                                   .withAction("Action")
                                                                   .withIsChecked(false)
                                                                   .withPriority(1) //
                                                                   .withKeyResult(null)
                                                                   .build())),

                         Arguments.of(new ActionPair( //
                                                     Action.Builder.builder() //
                                                                   .withId(id)
                                                                   .withAction("Action")
                                                                   .withIsChecked(false)
                                                                   .withPriority(1) //
                                                                   .withKeyResult(keyResult)
                                                                   .build(),

                                                     Action.Builder.builder() //
                                                                   .withId(id)
                                                                   .withAction("Action")
                                                                   .withIsChecked(false)
                                                                   .withPriority(1) //
                                                                   .withKeyResult(null)
                                                                   .build())),

                         Arguments.of(new ActionPair( //
                                                     Action.Builder.builder() //
                                                                   .withId(id)
                                                                   .withAction("Action")
                                                                   .withIsChecked(false)
                                                                   .withPriority(1) //
                                                                   .withKeyResult(null)
                                                                   .build(),

                                                     Action.Builder.builder() //
                                                                   .withId(id)
                                                                   .withAction("Action")
                                                                   .withIsChecked(false)
                                                                   .withPriority(1)
                                                                   .withKeyResult(keyResult)
                                                                   .build())));
    }

    @BeforeEach
    void setUp() {
        Mockito.lenient()
               .when(actionPersistenceService.getModelName())
               .thenReturn("Action");
    }

    @Test
    void validateOnCreateShouldBeSuccessfulWhenActionIsValid() {
        validator.validateOnCreate(action1);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action1);
        verify(validator, times(1)).validate(action1);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        // arrange
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(null));

        // act + assert
        List<ErrorDto> expectedErrors = List.of( //
                                                new ErrorDto("MODEL_NULL", List.of("Action")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        // arrange
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(action2));

        // act + assert
        List<ErrorDto> expectedErrors = List.of( //
                                                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "Action")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @ParameterizedTest
    @MethodSource("actionValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenActionIsInvalid(String actionText, List<ErrorDto> errors) {
        // arrange
        Action action = Action.Builder.builder()
                                      .withId(null)
                                      .withAction(actionText)
                                      .withIsChecked(false)
                                      .withPriority(1)
                                      .withKeyResult(keyResult)
                                      .build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(action));

        assertOkrResponseStatusException(exception, errors);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        // arrange
        Action actionInvalid = Action.Builder.builder()
                                             .withIsChecked(true)
                                             .build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(actionInvalid));

        List<ErrorDto> expectedErrors = List.of( //
                                                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("action", "Action")), //
                                                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("keyResult", "Action")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @Test
    void validateOnUpdateShouldBeSuccessfulWhenActionIsValid() {
        // arrange
        when(actionPersistenceService.findById(anyLong())).thenReturn(action2);

        // act
        validator.validateOnUpdate(action2.getId(), action2);

        // assert
        verify(validator, times(1)).throwExceptionWhenModelIsNull(action2);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(action2.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(action2.getId(), action2.getId());
        verify(validator, times(1)).doesEntityExist(action2.getId());
        verify(validator, times(1)).throwExceptionWhenKeyResultHasChanged(action2, action2);
        verify(validator, times(1)).validate(action2);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        // arrange
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(1L, null));

        // act + assert
        List<ErrorDto> expectedErrors = List.of( //
                                                new ErrorDto("MODEL_NULL", List.of("Action")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(null, action1));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action1);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of( //
                                                new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Action")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdHasChanged() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(1L, action2));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action2);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(action2.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(1L, action2.getId());

        List<ErrorDto> expectedErrors = List.of( //
                                                new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", "1", "2")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenEntityDoesNotExist() {
        // arrange
        String reason = "MODEL_WITH_ID_NOT_FOUND";
        when(actionPersistenceService.findById(anyLong())).thenThrow(new OkrResponseStatusException(BAD_REQUEST,
                                                                                                    reason));

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(action2.getId(), action2));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action2);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(action2.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(action2.getId(), action2.getId());

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("MODEL_WITH_ID_NOT_FOUND", exception.getReason());
    }

    @DisplayName("validateOnUpdate() should throw exception when KeyResult is not set")
    @ParameterizedTest
    @MethodSource("actionPairArgument")
    void validateOnUpdateShouldThrowExceptionWhenKeyResultNotSet(ActionPair actionPair) {
        // arrange
        Action action = actionPair.action();
        Action saveAction = actionPair.saveAction();
        Long id = 3L;

        when(actionPersistenceService.findById(id)).thenReturn(saveAction);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(id, action));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(action.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(id, action.getId());

        List<ErrorDto> expectedErrors = List.of( //
                                                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("KeyResult", "Action")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenKeyResultIdHasChanged() {
        // arrange
        Action action = Action.Builder.builder()
                                      .withId(action2.getId())
                                      .withAction("Action")
                                      .withIsChecked(false)
                                      .withPriority(1)
                                      .withKeyResult(KeyResultMetric.Builder.builder()
                                                                            .withId(11L)
                                                                            .withTitle("KR Title")
                                                                            .build())
                                      .build();
        when(actionPersistenceService.findById(anyLong())).thenReturn(action2);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(action.getId(), action));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(action.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(action.getId(), action2.getId());

        List<ErrorDto> expectedErrors = List.of( //
                                                new ErrorDto("ATTRIBUTE_CANNOT_CHANGE",
                                                             List.of("KeyResult", "Action")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @ParameterizedTest
    @MethodSource("actionValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenTitleIsInvalid(String actionText, List<ErrorDto> errors) {
        // arrange
        Action action = Action.Builder.builder()
                                      .withId(3L)
                                      .withAction(actionText)
                                      .withIsChecked(false)
                                      .withPriority(1)
                                      .withKeyResult(keyResult)
                                      .build();
        when(actionPersistenceService.findById(anyLong())).thenReturn(action);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(3L, action));
        assertOkrResponseStatusException(exception, errors);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenKeyResultIsMissing() {
        // arrange
        Action actionInvalid = Action.Builder.builder()
                                             .withId(11L)
                                             .withIsChecked(true)
                                             .build();
        when(actionPersistenceService.findById(anyLong())).thenReturn(actionInvalid);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(11L, actionInvalid));

        List<ErrorDto> expectedErrors = List.of( //
                                                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("KeyResult", "Action")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrsAreMissing() {
        // arrange
        Action actionInvalid = Action.Builder.builder()
                                             .withId(11L)
                                             .withIsChecked(true)
                                             .withKeyResult(keyResult)
                                             .build();
        when(actionPersistenceService.findById(anyLong())).thenReturn(actionInvalid);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(11L, actionInvalid));

        List<ErrorDto> expectedErrors = List.of( //
                                                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("action", "Action")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("validateOnGetByKeyResultId() should be successful when id is not null")
    @Test
    void validateOnGetByKeyResultIdShouldBeSuccessfulWhenIdIsNotNull() {
        // arrange
        Long id = 1L;
        doNothing().when(keyResultValidationService)
                   .validateOnGet(id);

        // act + assert
        assertDoesNotThrow(() -> validator.validateOnGetByKeyResultId(id));
        verify(actionPersistenceService, never()).getModelName();
    }

    @DisplayName("validateOnGetByKeyResultId() should throw exception when id is null")
    @Test
    void validateOnGetByKeyResultIdShouldThrowExceptionWhenIdIsNull() {
        // arrange
        Long id = null;
        doThrow(OkrResponseStatusException.class).when(keyResultValidationService)
                                                 .validateOnGet(id);

        // act + assert
        assertThrows(OkrResponseStatusException.class, () -> validator.validateOnGetByKeyResultId(id));
    }

}
