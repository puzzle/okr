package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@SpringIntegrationTest
class ActionPersistenceServiceIT {
    Action createdAction;
    @Autowired
    private ActionPersistenceService actionPersistenceService;

    private static Action createAction(Long id) {
        return createAction(id, 1);
    }

    private static Action createAction(Long id, int version) {
        return Action.Builder.builder().withId(id).withVersion(version).withAction("Neue Katze").withPriority(0)
                .withIsChecked(false)
                .withKeyResult(KeyResultMetric.Builder.builder().withBaseline(1.0).withStretchGoal(13.0).withId(8L)
                        .withObjective(Objective.Builder.builder().withId(1L).build()).build())
                .build();
    }

    @AfterEach
    void tearDown() {
        try {
            if (createdAction != null) {
                actionPersistenceService.findById(createdAction.getId());
                actionPersistenceService.deleteById(createdAction.getId());
            }
        } catch (ResponseStatusException ex) {
            // created Action already deleted
        } finally {
            createdAction = null;
        }
    }

    @Test
    void saveActionShouldSaveNewAction() {
        Action action = createAction(null);

        createdAction = actionPersistenceService.save(action);

        assertNotNull(createdAction.getId());
        assertEquals(action.getAction(), createdAction.getAction());
        assertEquals(action.getKeyResult(), createdAction.getKeyResult());
        assertEquals(action.getPriority(), createdAction.getPriority());
        assertEquals(action.isChecked(), createdAction.isChecked());
    }

    @Test
    void updateActionShouldUpdateAction() {
        Action action = createAction(null);
        createdAction = actionPersistenceService.save(action);
        createdAction.setAction("Updated Action");
        createdAction.setPriority(4);

        Action updateAction = actionPersistenceService.save(createdAction);

        assertEquals(createdAction.getId(), updateAction.getId());
        assertEquals("Updated Action", updateAction.getAction());
        assertEquals(4, updateAction.getPriority());
    }

    @Test
    void updateActionShouldThrowExceptionWhenAlreadyUpdated() {
        createdAction = actionPersistenceService.save(createAction(null));
        Action changedAction = createAction(createdAction.getId(), 0);
        changedAction.setAction("Updated Action");

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> actionPersistenceService.save(changedAction));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("DATA_HAS_BEEN_UPDATED", List.of("Action")));

        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void getAllActionsShouldReturnListOfAllActions() {
        List<Action> actions = actionPersistenceService.findAll();

        assertEquals(11, actions.size());
    }

    @Test
    void getAllActionsByKeyResultIdShouldReturnListOfAllActionsFromThisKeyResultOrderASC() {
        List<Action> actions = actionPersistenceService.getActionsByKeyResultIdOrderByPriorityAsc(6L);

        assertEquals(3, actions.size());
        assertEquals(1, actions.get(0).getPriority());
        assertEquals(2, actions.get(1).getPriority());
        assertEquals(3, actions.get(2).getPriority());
    }

    @Test
    void getActionByIdShouldReturnActionProperly() {
        Action action = actionPersistenceService.findById(1L);

        assertEquals(1L, action.getId());
        assertEquals("Neues Haus", action.getAction());
        assertEquals(1, action.getPriority());
        assertEquals(true, action.isChecked());
        assertEquals(8L, action.getKeyResult().getId());
    }

    @Test
    void shouldDeleteActionById() {
        Action action = createAction(null);

        createdAction = actionPersistenceService.save(action);

        List<Action> actions = actionPersistenceService.findAll();
        assertEquals(12, actions.size());

        actionPersistenceService.deleteById(createdAction.getId());
        actions = actionPersistenceService.findAll();
        assertEquals(11, actions.size());
    }
}
