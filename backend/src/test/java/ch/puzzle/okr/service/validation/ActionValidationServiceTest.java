package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ActionValidationServiceTest {
    private final KeyResult keyResult = KeyResultMetric.Builder.builder().withId(10L).withTitle("KR Title").build();
    private final Action action1 = Action.Builder.builder().withId(null).withAction("Neue Katze").withIsChecked(false)
            .withPriority(0).withKeyResult(keyResult).build();
    private final Action action2 = Action.Builder.builder().withId(2L).withAction("Neues Lama").withIsChecked(true)
            .withPriority(1).withKeyResult(keyResult).build();
    @Mock
    ActionPersistenceService actionPersistenceService;
    @Spy
    @InjectMocks
    private ActionValidationService validator;

    private static Stream<Arguments> actionValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 5000),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("action", "Action", "0", "4096")))),
                arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("action", "Action")))));
    }

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(actionPersistenceService.getModelName()).thenReturn("Action");
    }

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidActionId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGetShouldThrowExceptionIfActionIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Action"))), exception.getErrors());
    }

    @Test
    void validateOnCreateShouldBeSuccessfulWhenActionIsValid() {
        validator.validateOnCreate(action1);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action1);
        verify(validator, times(1)).validate(action1);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("MODEL_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("MODEL_NULL", List.of("Action"))), exception.getErrors());
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(action2));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "Action")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("actionValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenActionIsInvalid(String actionText, List<ErrorDto> errors) {
        Action action = Action.Builder.builder().withId(null).withAction(actionText).withIsChecked(false)
                .withPriority(1).withKeyResult(keyResult).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(action));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        Action actionInvalid = Action.Builder.builder().withIsChecked(true).build();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(actionInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("action", "Action")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("keyResult", "Action")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldBeSuccessfulWhenActionIsValid() {
        when(actionPersistenceService.findById(anyLong())).thenReturn(action2);

        validator.validateOnUpdate(action2.getId(), action2);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action2);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(action2.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(action2.getId(), action2.getId());
        verify(validator, times(1)).doesEntityExist(action2.getId());
        verify(validator, times(1)).throwExceptionWhenKeyResultHasChanged(action2, action2);
        verify(validator, times(1)).validate(action2);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("MODEL_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("MODEL_NULL", List.of("Action"))), exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(null, action1));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action1);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Action"))), exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdHasChanged() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, action2));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action2);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(action2.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(1L, action2.getId());

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_CHANGED", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", "1", "2"))), exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenEntityDoesNotExist() {
        String reason = "MODEL_WITH_ID_NOT_FOUND";
        when(actionPersistenceService.findById(anyLong()))
                .thenThrow(new OkrResponseStatusException(BAD_REQUEST, reason));
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(action2.getId(), action2));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action2);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(action2.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(action2.getId(), action2.getId());

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("MODEL_WITH_ID_NOT_FOUND", exception.getReason());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenKeyResultNotSet() {
        Long id = 3L;
        Action action = Action.Builder.builder().withId(id).withAction("Action").withIsChecked(false).withPriority(1)
                .build();
        when(actionPersistenceService.findById(id)).thenReturn(action);
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, action));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(action.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(id, action.getId());

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NOT_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("KeyResult", "Action"))),
                exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenKeyResultIdHasChanged() {
        Action action = Action.Builder.builder().withId(action2.getId()).withAction("Action").withIsChecked(false)
                .withPriority(1)
                .withKeyResult(KeyResultMetric.Builder.builder().withId(11L).withTitle("KR Title").build()).build();
        when(actionPersistenceService.findById(anyLong())).thenReturn(action2);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(action.getId(), action));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(action);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(action.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(action.getId(), action2.getId());

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_CANNOT_CHANGE", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_CANNOT_CHANGE", List.of("KeyResult", "Action"))),
                exception.getErrors());
    }

    @ParameterizedTest
    @MethodSource("actionValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenTitleIsInvalid(String actionText, List<ErrorDto> errors) {
        Action action = Action.Builder.builder().withId(3L).withAction(actionText).withIsChecked(false).withPriority(1)
                .withKeyResult(keyResult).build();
        when(actionPersistenceService.findById(anyLong())).thenReturn(action);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, action));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenKeyResultIsMissing() {
        Action actionInvalid = Action.Builder.builder().withId(11L).withIsChecked(true).build();
        when(actionPersistenceService.findById(anyLong())).thenReturn(actionInvalid);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(11L, actionInvalid));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NOT_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("KeyResult", "Action"))),
                exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrsAreMissing() {
        Action actionInvalid = Action.Builder.builder().withId(11L).withIsChecked(true).withKeyResult(keyResult)
                .build();
        when(actionPersistenceService.findById(anyLong())).thenReturn(actionInvalid);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(11L, actionInvalid));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NOT_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("action", "Action"))), exception.getErrors());
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidActionId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfActionIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Action"))), exception.getErrors());
    }

}
