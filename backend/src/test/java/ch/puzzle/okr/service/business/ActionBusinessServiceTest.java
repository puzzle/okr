package ch.puzzle.okr.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import ch.puzzle.okr.service.validation.ActionValidationService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActionBusinessServiceTest {
    @Mock
    ActionPersistenceService actionPersistenceService;
    @Mock
    ActionValidationService validator;
    @InjectMocks
    private ActionBusinessService actionBusinessService;

    private KeyResult metricKeyResult = KeyResultMetric.Builder
            .builder()
            .withBaseline(10D)
            .withStretchGoal(50D)
            .withUnit(Unit.CHF)
            .withId(8L)
            .withTitle("Keyresult Metric")
            .build();
    private Action action1 = Action.Builder
            .builder()
            .withId(1L)
            .withAction("Neue Katze")
            .isChecked(false)
            .withPriority(0)
            .withKeyResult(metricKeyResult)
            .build();
    private Action action2 = Action.Builder
            .builder()
            .withId(2L)
            .withAction("Neues Lama")
            .isChecked(true)
            .withPriority(1)
            .withKeyResult(metricKeyResult)
            .build();
    private List<Action> actionList = List.of(action1, action2);

    @DisplayName("Should get action by id")
    @Test
    void shouldGetAction() {
        when(actionPersistenceService.findById(action1.getId())).thenReturn(action1);
        Action action = actionBusinessService.getEntityById(action1.getId());

        assertEquals(action1.getId(), action.getId());
        assertEquals(action1.getActionPoint(), action.getActionPoint());
        assertEquals(action1.getKeyResult(), action.getKeyResult());
        assertEquals(action1.getPriority(), action.getPriority());
        assertEquals(action1.isChecked(), action.isChecked());
    }

    @DisplayName("Should get actions from key-result")
    @Test
    void shouldGetActionsFromKeyResult() {
        when(actionPersistenceService.getActionsByKeyResultIdOrderByPriorityAsc(metricKeyResult.getId()))
                .thenReturn(actionList);
        List<Action> actions = actionBusinessService.getActionsByKeyResultId(metricKeyResult.getId());

        assertIterableEquals(actions, actionList);
    }

    @DisplayName("Should update multiple actions without key result")
    @Test
    void shouldUpdateMultipleActionsNoKeyResult() {
        when(actionPersistenceService.findById(action1.getId())).thenReturn(action1);
        when(actionPersistenceService.findById(action2.getId())).thenReturn(action2);

        action1.setKeyResult(null);
        action2.setKeyResult(null);
        List<Action> actions = List.of(action1, action2);
        actionBusinessService.updateEntities(actions);

        verify(actionPersistenceService, times(1)).save(action1);
        verify(actionPersistenceService, times(1)).save(action2);
        verify(actionPersistenceService, times(2)).findById(any());
    }

    @DisplayName("Should create action when action has no id")
    @Test
    void shouldCreateActionWhenUpdateWithNoId() {
        Action action = Action.Builder
                .builder()
                .withAction("Neue Katze")
                .withPriority(0)
                .isChecked(false)
                .withKeyResult(metricKeyResult)
                .build();
        actionBusinessService.updateEntities(List.of(action));

        verify(actionPersistenceService, times(1)).save(action);
    }

    @DisplayName("Should update multiple actions")
    @Test
    void shouldUpdateMultipleActions() {
        actionBusinessService.updateEntities(actionList);

        verify(actionPersistenceService, times(1)).save(action1);
        verify(actionPersistenceService, times(1)).save(action2);
    }

    @DisplayName("Should create multiple actions")
    @Test
    void shouldCreateMultipleActions() {
        Action newAction1 = Action.Builder
                .builder()
                .withAction("Neuer Drucker")
                .withKeyResult(metricKeyResult)
                .withPriority(0)
                .isChecked(false)
                .build();
        Action newAction2 = Action.Builder
                .builder()
                .withAction("Neues Papier")
                .withKeyResult(metricKeyResult)
                .withPriority(1)
                .isChecked(false)
                .build();

        when(actionPersistenceService.save(newAction1)).thenReturn(action1);
        when(actionPersistenceService.save(newAction2)).thenReturn(action2);
        List<Action> createdActions = actionBusinessService.createEntities(List.of(newAction1, newAction2));

        verify(actionPersistenceService, times(1)).save(newAction1);
        verify(actionPersistenceService, times(1)).save(newAction2);

        assertIterableEquals(createdActions, actionList);
    }

    @DisplayName("Should create one action")
    @Test
    void shouldCreateOneAction() {
        Action newAction1 = Action.Builder
                .builder()
                .withAction("Neuer Drucker")
                .withKeyResult(metricKeyResult)
                .withPriority(0)
                .isChecked(false)
                .build();

        when(actionPersistenceService.save(newAction1)).thenReturn(action1);
        List<Action> createdActions = actionBusinessService.createEntities(List.of(newAction1));

        verify(actionPersistenceService, times(1)).save(newAction1);

        assertIterableEquals(createdActions, List.of(action1));
    }

    @DisplayName("Should delete action")
    @Test
    void shouldDeleteAction() {
        actionBusinessService.deleteEntityById(action1.getId());
        verify(actionPersistenceService, times(1)).deleteById(action1.getId());
    }

    @ParameterizedTest(name = "Should return a empty list when actions are {0}")
    @NullAndEmptySource
    void shouldReturnEmptyListWhenActionsAreEmpty(List<Action> actions) {
        KeyResult newKeyResult = KeyResultMetric.Builder.builder().withId(9L).build();
        KeyResult oldKeyResult = KeyResultMetric.Builder.builder().withId(6L).build();

        when(actionPersistenceService.getActionsByKeyResultIdOrderByPriorityAsc(oldKeyResult.getId()))
                .thenReturn(actions);

        List<Action> result = actionBusinessService.duplicateActions(oldKeyResult, newKeyResult);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @DisplayName("Should duplicate all actions when there are actions defined")
    @Test
    void shouldDuplicateActionsWhenActionsExist() {
        KeyResult newKeyResult = KeyResultMetric.Builder.builder().withId(9L).build();
        KeyResult oldKeyResult = KeyResultMetric.Builder.builder().withId(6L).build();

        Action action1 = Action.Builder
                .builder()
                .withAction("Neuer Drucker")
                .withKeyResult(newKeyResult)
                .isChecked(false)
                .withPriority(1)
                .build();
        Action action2 = Action.Builder
                .builder()
                .withAction("Neues Papier")
                .withKeyResult(newKeyResult)
                .isChecked(true)
                .withPriority(2)
                .build();

        when(actionPersistenceService.getActionsByKeyResultIdOrderByPriorityAsc(oldKeyResult.getId()))
                .thenReturn(List.of(action1, action2));

        List<Action> result = actionBusinessService.duplicateActions(oldKeyResult, newKeyResult);

        assertNotNull(result);
        assertEquals(2, result.size());

        Action newAction1 = result.getFirst();
        assertEquals("Neuer Drucker", newAction1.getActionPoint());
        assertFalse(newAction1.isChecked());
        assertEquals(1, newAction1.getPriority());
        assertEquals(newKeyResult, newAction1.getKeyResult());

        Action newAction2 = result.get(1);
        assertEquals("Neues Papier", newAction2.getActionPoint());
        assertTrue(newAction2.isChecked());
        assertEquals(2, newAction2.getPriority());
        assertEquals(newKeyResult, newAction2.getKeyResult());

        verify(validator, times(2)).validate(any(Action.class));
        verify(actionPersistenceService, times(2)).save(any(Action.class));
    }
}
