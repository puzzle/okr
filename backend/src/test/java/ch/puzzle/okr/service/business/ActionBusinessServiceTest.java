package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import ch.puzzle.okr.service.validation.ActionValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActionBusinessServiceTest {
    private static final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @Mock
    ActionPersistenceService actionPersistenceService;
    @Mock
    ActionValidationService validator;
    @InjectMocks
    private ActionBusinessService actionBusinessService;

    private KeyResult metricKeyResult = KeyResultMetric.Builder.builder().withBaseline(10D).withStretchGoal(50D)
            .withUnit(Unit.CHF).withId(8L).withTitle("Keyresult Metric").build();
    private Action action1 = Action.Builder.builder().withId(1L).withAction("Neue Katze").withIsChecked(false)
            .withPriority(0).withKeyResult(metricKeyResult).build();
    private Action action2 = Action.Builder.builder().withId(2L).withAction("Neues Lama").withIsChecked(true)
            .withPriority(1).withKeyResult(metricKeyResult).build();
    private List<Action> actionList = List.of(action1, action2);

    @Test
    void shouldGetAction() {
        when(actionPersistenceService.findById(action1.getId())).thenReturn(action1);
        Action action = actionBusinessService.getEntityById(action1.getId());

        assertEquals(action1.getId(), action.getId());
        assertEquals(action1.getAction(), action.getAction());
        assertEquals(action1.getKeyResult(), action.getKeyResult());
        assertEquals(action1.getPriority(), action.getPriority());
        assertEquals(action1.isChecked(), action.isChecked());
    }

    @Test
    void shouldGetActionsFromKeyResult() {
        when(actionPersistenceService.getActionsByKeyResultIdOrderByPriorityAsc(metricKeyResult.getId()))
                .thenReturn(actionList);
        List<Action> actions = actionBusinessService.getActionsByKeyResultId(metricKeyResult.getId());

        assertIterableEquals(actions, actionList);
    }

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

    @Test
    void shouldCreateActionWhenUpdateWithNoId() {
        Action action = Action.Builder.builder().withAction("Neue Katze").withPriority(0).withIsChecked(false)
                .withKeyResult(metricKeyResult).build();
        actionBusinessService.updateEntities(List.of(action));

        verify(actionPersistenceService, times(1)).save(action);
    }

    @Test
    void shouldUpdateMultipleActionsNormal() {
        actionBusinessService.updateEntities(actionList);

        verify(actionPersistenceService, times(1)).save(action1);
        verify(actionPersistenceService, times(1)).save(action2);
    }

    @Test
    void shouldCreateMultipleActions() {
        Action newAction1 = Action.Builder.builder().withAction("Neuer Drucker").withKeyResult(metricKeyResult)
                .withPriority(0).withIsChecked(false).build();
        Action newAction2 = Action.Builder.builder().withAction("Neues Papier").withKeyResult(metricKeyResult)
                .withPriority(1).withIsChecked(false).build();

        when(actionPersistenceService.save(newAction1)).thenReturn(action1);
        when(actionPersistenceService.save(newAction2)).thenReturn(action2);
        List<Action> createdActions = actionBusinessService.createEntities(List.of(newAction1, newAction2));

        verify(actionPersistenceService, times(1)).save(newAction1);
        verify(actionPersistenceService, times(1)).save(newAction2);

        assertIterableEquals(createdActions, actionList);
    }

    @Test
    void shouldCreateOneAction() {
        Action newAction1 = Action.Builder.builder().withAction("Neuer Drucker").withKeyResult(metricKeyResult)
                .withPriority(0).withIsChecked(false).build();

        when(actionPersistenceService.save(newAction1)).thenReturn(action1);
        List<Action> createdActions = actionBusinessService.createEntities(List.of(newAction1));

        verify(actionPersistenceService, times(1)).save(newAction1);

        assertIterableEquals(createdActions, List.of(action1));
    }

    @Test
    void shouldDeleteAction() {
        actionBusinessService.deleteEntityById(action1.getId());
        verify(actionPersistenceService, times(1)).deleteById(action1.getId());
    }
}
