package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Action;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActionValidationServiceTest {
    @Mock
    ActionPersistenceService actionPersistenceService;

    Action action1;
    Action action2;
    List<Action> actionList;

    @BeforeEach
    void setUp() {
        this.action1 = Action.Builder.builder().withId(null).withAction("Neue Katze").withIsChecked(false)
                .withPriority(0)
                .withKeyResult(KeyResultMetric.Builder.builder().withId(10L).withTitle("KR Title").build()).build();
        this.action2 = Action.Builder.builder().withId(2L).withAction("Neues Lama").withIsChecked(true).withPriority(1)
                .withKeyResult(KeyResultMetric.Builder.builder().withId(10L).withTitle("KR Title").build()).build();
        this.actionList = List.of(action1, action2);

        Mockito.lenient().when(actionPersistenceService.getModelName()).thenReturn("Action");
    }

    @Spy
    @InjectMocks
    private ActionValidationService validator;

    private static Stream<Arguments> actionValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 5000),
                        List.of("Attribute Action has a max length of 4096 characters")),
                arguments(null, List.of("Action must not be null")));
    }

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidActionId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGetShouldThrowExceptionIfActionIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnCreateShouldBeSuccessfulWhenActionIsValid() {
        validator.validateOnCreate(this.action1);

        verify(validator, times(1)).throwExceptionIfModelIsNull(this.action1);
        verify(validator, times(1)).validate(this.action1);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals("Given model Action is null", exception.getReason());
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(this.action2));

        assertEquals("Model Action cannot have id while create. Found id 2", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("actionValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenActionIsInvalid(String actionText, List<String> errors) {
        Action action = Action.Builder.builder().withId(null).withAction(actionText).withIsChecked(false)
                .withPriority(1)
                .withKeyResult(KeyResultMetric.Builder.builder().withId(10L).withTitle("KR Title").build()).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(action));

        String[] exceptionParts = exception.getReason().split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            System.out.println(exceptionParts[i].strip());
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            System.out.println(exceptionParts[i]);
            assert (errors.contains(errorArray[i]));
        }
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        Action actionInvalid = Action.Builder.builder().withIsChecked(true).build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(actionInvalid));

        assertThat(exception.getReason().strip()).contains("Action must not be null");
        assertThat(exception.getReason().strip()).contains("KeyResult must not be null");
    }

    @Test
    void validateOnUpdateShouldBeSuccessfulWhenActionIsValid() {
        validator.validateOnUpdate(this.action2.getId(), this.action2);

        verify(validator, times(1)).throwExceptionIfModelIsNull(this.action2);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(this.action2.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(this.action2.getId(), this.action2.getId());
        verify(validator, times(1)).validate(this.action2);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals("Given model Action is null", exception.getReason());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(null, this.action1));

        verify(validator, times(1)).throwExceptionIfModelIsNull(this.action1);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdHasChanged() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, this.action2));

        verify(validator, times(1)).throwExceptionIfModelIsNull(this.action2);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(this.action2.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(1L, this.action2.getId());
        assertEquals("Id 1 has changed to 2 during update", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("actionValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenTitleIsInvalid(String actionText, List<String> errors) {
        Action action = Action.Builder.builder().withId(3L).withAction(actionText).withIsChecked(false).withPriority(1)
                .withKeyResult(KeyResultMetric.Builder.builder().withId(10L).withTitle("KR Title").build()).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, action));

        String[] exceptionParts = exception.getReason().split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrsAreMissing() {
        Action actionInvalid = Action.Builder.builder().withId(11L).withIsChecked(true).build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(11L, actionInvalid));

        assertThat(exception.getReason().strip()).contains("Action must not be null.");
        assertThat(exception.getReason().strip()).contains("KeyResult must not be null.");
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidActionId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfActionIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

}
