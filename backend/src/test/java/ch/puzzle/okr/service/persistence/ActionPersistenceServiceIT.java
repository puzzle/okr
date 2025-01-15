package ch.puzzle.okr.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

@SpringIntegrationTest
class ActionPersistenceServiceIT {
    private static final String UPDATED_ACTION = "Updated Action";
    Action createdAction;
    @Autowired
    private ActionPersistenceService actionPersistenceService;

    private static Action createAction(Long id) {
        return createAction(id, 1);
    }

    private static Action createAction(Long id, int version) {
        return Action.Builder
                .builder()
                .withId(id)
                .withVersion(version)
                .withAction("Neue Katze")
                .withPriority(0)
                .isChecked(false)
                .withKeyResult(KeyResultMetric.Builder
                        .builder()
                        .withBaseline(1.0)
                        .withStretchGoal(13.0)
                        .withId(8L)
                        .withObjective(Objective.Builder.builder().withId(1L).build())
                        .build())
                .build();
    }

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
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
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Should create new entity on save()")
    @Test
    void shouldCreateNewEntityWhenSaveIsCalled() {
        Action action = createAction(null);

        createdAction = actionPersistenceService.save(action);

        assertNotNull(createdAction.getId());
        assertEquals(action.getActionPoint(), createdAction.getActionPoint());
        assertEquals(action.getKeyResult(), createdAction.getKeyResult());
        assertEquals(action.getPriority(), createdAction.getPriority());
        assertEquals(action.isChecked(), createdAction.isChecked());
    }

    @DisplayName("Should update action on save() when the action has been modified")
    @Test
    void shouldUpdateActionWhenSaveIsCalledWithModifiedAction() {
        Action action = createAction(null);
        createdAction = actionPersistenceService.save(action);
        createdAction.setActionPoint(UPDATED_ACTION);
        createdAction.setPriority(4);

        Action updateAction = actionPersistenceService.save(createdAction);

        assertEquals(createdAction.getId(), updateAction.getId());
        assertEquals(UPDATED_ACTION, updateAction.getActionPoint());
        assertEquals(4, updateAction.getPriority());
    }

    @DisplayName("Should throw exception on save() when the action has already been updated")
    @Test
    void shouldThrowExceptionWhenSaveIsCalledOnAlreadyUpdatedAction() {
        createdAction = actionPersistenceService.save(createAction(null));
        Action changedAction = createAction(createdAction.getId(), 0);
        changedAction.setActionPoint(UPDATED_ACTION);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> actionPersistenceService.save(changedAction));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("DATA_HAS_BEEN_UPDATED", List.of("Action")));

        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should return list of all actions on findAll()")
    @Test
    void shouldReturnListOfAllActionsWhenFindAllIsCalled() {
        List<Action> actions = actionPersistenceService.findAll();
        System.out.println(actions.getFirst());
        assertEquals(11, actions.size());
    }

    @DisplayName("Should return list of actions from a key result ordered by ascending priority on getActionsByKeyResultIdOrderByPriorityAsc()")
    @Test
    void shouldReturnListOfActionsForKeyResultOrderedByPriorityWhenGetActionsByKeyResultIdOrderByPriorityAscIsCalled() {
        List<Action> actions = actionPersistenceService.getActionsByKeyResultIdOrderByPriorityAsc(6L);

        assertEquals(3, actions.size());
        assertEquals(1, actions.get(0).getPriority());
        assertEquals(2, actions.get(1).getPriority());
        assertEquals(3, actions.get(2).getPriority());
    }

    @DisplayName("Should return correct action on findById()")
    @Test
    void shouldReturnCorrectActionWhenFindByIdIsCalled() {
        Action action = actionPersistenceService.findById(1L);

        assertEquals(1L, action.getId());
        assertEquals("Neues Haus", action.getActionPoint());
        assertEquals(1, action.getPriority());
        assertTrue(action.isChecked());
        assertEquals(8L, action.getKeyResult().getId());
    }

    @DisplayName("Should delete action on delete()")
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
