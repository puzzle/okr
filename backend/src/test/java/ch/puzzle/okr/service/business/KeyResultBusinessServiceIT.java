package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.models.keyresult.KeyResultWithActionList;
import ch.puzzle.okr.service.authorization.AuthorizationService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringIntegrationTest
class KeyResultBusinessServiceIT {
    private static final String KEY_RESULT_UPDATED = "Updated Key Result";
    private static final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    private KeyResult createdKeyResult;
    private Action action1;
    private Action action2;

    @Autowired
    private KeyResultBusinessService keyResultBusinessService;

    @Autowired
    private CheckInBusinessService checkInBusinessService;

    @Autowired
    private ActionBusinessService actionBusinessService;

    @Mock
    private AuthorizationService authorizationService;

    private static KeyResult createKeyResultMetric(Long id) {
        return KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(5.0).withUnit(Unit.FTE).withId(id)
                .withTitle("Title").withCreatedBy(User.Builder.builder().withId(1L).build())
                .withOwner(User.Builder.builder().withId(1L).build())
                .withObjective(Objective.Builder.builder().withId(4L).build()).withCreatedOn(LocalDateTime.now())
                .build();
    }

    private static KeyResult createKeyResultOrdinal(Long id) {
        return KeyResultOrdinal.Builder.builder().withCommitZone("Hamster").withTargetZone("Katze").withId(id)
                .withTitle("Ordinal KeyResult").withStretchZone("ZOO")
                .withCreatedBy(User.Builder.builder().withId(1L).build())
                .withOwner(User.Builder.builder().withId(1L).build())
                .withObjective(Objective.Builder.builder().withId(4L).build()).withCreatedOn(LocalDateTime.now())
                .build();
    }

    private static CheckIn createCheckInMetric(KeyResult keyResult) {
        return CheckInMetric.Builder.builder().withKeyResult(keyResult).withConfidence(5).withValue(7.5).build();
    }

    private static CheckIn createCheckInOrdinal(KeyResult keyResult) {
        return CheckInOrdinal.Builder.builder().withKeyResult(keyResult).withConfidence(5).withZone(Zone.COMMIT)
                .build();
    }

    private static Action createAction1(KeyResult keyResult) {
        return Action.Builder.builder().withIsChecked(false).withAction("Neuer Drucker").withPriority(0)
                .withKeyResult(keyResult).build();
    }

    private static Action createAction2(KeyResult keyResult) {
        return Action.Builder.builder().withIsChecked(false).withAction("Neues Papier").withPriority(0)
                .withKeyResult(keyResult).build();
    }

    @BeforeEach
    void setUp() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
    }

    @AfterEach
    void tearDown() {
        deleteCreatedKeyResult();
        action1 = deleteCreatedAction(action1);
        action2 = deleteCreatedAction(action2);
    }

    private void deleteCreatedKeyResult() {
        try {
            if (createdKeyResult != null) {
                keyResultBusinessService.getEntityById(createdKeyResult.getId());
                keyResultBusinessService.deleteEntityById(createdKeyResult.getId());
            }
        } catch (ResponseStatusException ex) {
            // created key result already deleted
        } finally {
            createdKeyResult = null;
        }
    }

    private Action deleteCreatedAction(Action action) {
        try {
            if (action != null) {
                actionBusinessService.getEntityById(action.getId());
                actionBusinessService.deleteEntityById(action.getId());
            }
        } catch (ResponseStatusException ex) {
            // created action already deleted
        }
        return null;
    }

    @Test
    void updateEntitiesShouldUpdateKeyResultWithSameTypeMetric() {
        createdKeyResult = keyResultBusinessService.createEntity(createKeyResultMetric(null), authorizationUser);
        createdKeyResult.setTitle(KEY_RESULT_UPDATED);

        KeyResultWithActionList updatedKeyResult = keyResultBusinessService.updateEntities(createdKeyResult.getId(),
                createdKeyResult, List.of());

        assertSameKeyResult(createdKeyResult, updatedKeyResult.keyResult());
    }

    @Test
    void updateEntitiesShouldUpdateKeyResultWithSameTypeMetricWithActionList() {
        createdKeyResult = keyResultBusinessService.createEntity(createKeyResultMetric(null), authorizationUser);
        createdKeyResult.setTitle(KEY_RESULT_UPDATED);
        action1 = actionBusinessService.createEntity(createAction1(createdKeyResult));
        action2 = actionBusinessService.createEntity(createAction2(createdKeyResult));

        KeyResultWithActionList updatedKeyResult = keyResultBusinessService.updateEntities(createdKeyResult.getId(),
                createdKeyResult, List.of(action1, action2));

        assertSameKeyResult(createdKeyResult, updatedKeyResult.keyResult());
        assertSameActions(List.of(action1, action2), updatedKeyResult);
    }

    @Test
    void updateEntitiesShouldUpdateKeyResultWithSameTypeOrdinal() {
        createdKeyResult = keyResultBusinessService.createEntity(createKeyResultOrdinal(null), authorizationUser);
        createdKeyResult.setTitle(KEY_RESULT_UPDATED);

        KeyResultWithActionList updatedKeyResult = keyResultBusinessService.updateEntities(createdKeyResult.getId(),
                createdKeyResult, List.of());

        assertSameKeyResult(createdKeyResult, updatedKeyResult.keyResult());
    }

    @Test
    void updateEntitiesShouldRecreateKeyResultMetric() {
        KeyResult savedKeyResult = keyResultBusinessService.createEntity(createKeyResultOrdinal(null),
                authorizationUser);
        KeyResult changedKeyResult = createKeyResultMetric(savedKeyResult.getId());

        KeyResultWithActionList updatedKeyResult = keyResultBusinessService.updateEntities(changedKeyResult.getId(),
                changedKeyResult, List.of());
        createdKeyResult = updatedKeyResult.keyResult();

        assertRecreatedKeyResult(changedKeyResult, createdKeyResult);
    }

    @Test
    void updateEntitiesShouldRecreateKeyResultMetricWithActionList() {
        KeyResult savedKeyResult = keyResultBusinessService.createEntity(createKeyResultOrdinal(null),
                authorizationUser);
        action1 = actionBusinessService.createEntity(createAction1(savedKeyResult));
        action2 = actionBusinessService.createEntity(createAction2(savedKeyResult));
        KeyResult changedKeyResult = createKeyResultMetric(savedKeyResult.getId());

        KeyResultWithActionList updatedKeyResult = keyResultBusinessService.updateEntities(changedKeyResult.getId(),
                changedKeyResult, List.of(action1, action2));
        createdKeyResult = updatedKeyResult.keyResult();

        assertRecreatedKeyResult(changedKeyResult, createdKeyResult);
        assertSameActions(List.of(action1, action2), updatedKeyResult);
    }

    @Test
    void updateEntitiesShouldRecreateKeyResultOrdinal() {
        KeyResult savedKeyResult = keyResultBusinessService.createEntity(createKeyResultMetric(null),
                authorizationUser);
        KeyResult changedKeyResult = createKeyResultOrdinal(savedKeyResult.getId());

        KeyResultWithActionList updatedKeyResult = keyResultBusinessService.updateEntities(changedKeyResult.getId(),
                changedKeyResult, List.of());
        createdKeyResult = updatedKeyResult.keyResult();

        assertRecreatedKeyResult(changedKeyResult, createdKeyResult);
    }

    @Test
    void updateEntitiesShouldUpdateKeyResultWithDifferentTypeAndCheckInMetric() {
        createdKeyResult = keyResultBusinessService.createEntity(createKeyResultOrdinal(null), authorizationUser);
        checkInBusinessService.createEntity(createCheckInOrdinal(createdKeyResult), authorizationUser);

        KeyResult changedKeyResult = createKeyResultMetric(createdKeyResult.getId());

        KeyResultWithActionList updatedKeyResult = keyResultBusinessService.updateEntities(changedKeyResult.getId(),
                changedKeyResult, List.of());

        assertUpdatedKeyResult(changedKeyResult, updatedKeyResult.keyResult());
    }

    @Test
    void updateEntitiesShouldUpdateKeyResultWithDifferentTypeAndCheckInMetricWithActionList() {
        createdKeyResult = keyResultBusinessService.createEntity(createKeyResultOrdinal(null), authorizationUser);
        checkInBusinessService.createEntity(createCheckInOrdinal(createdKeyResult), authorizationUser);
        action1 = actionBusinessService.createEntity(createAction1(createdKeyResult));
        action2 = actionBusinessService.createEntity(createAction2(createdKeyResult));

        KeyResult changedKeyResult = createKeyResultMetric(createdKeyResult.getId());
        action1.setChecked(true);
        action2.setChecked(true);

        KeyResultWithActionList updatedKeyResult = keyResultBusinessService.updateEntities(changedKeyResult.getId(),
                changedKeyResult, List.of(action1, action2));

        assertUpdatedKeyResult(changedKeyResult, updatedKeyResult.keyResult());
        assertUpdatedActions(List.of(action1, action2), updatedKeyResult);
    }

    @Test
    void updateEntitiesShouldUpdateKeyResultWithDifferentTypeAndCheckInOrdinal() {
        createdKeyResult = keyResultBusinessService.createEntity(createKeyResultMetric(null), authorizationUser);
        checkInBusinessService.createEntity(createCheckInMetric(createdKeyResult), authorizationUser);

        KeyResult changedKeyResult = createKeyResultOrdinal(createdKeyResult.getId());

        KeyResultWithActionList updatedKeyResult = keyResultBusinessService.updateEntities(changedKeyResult.getId(),
                changedKeyResult, List.of());

        assertUpdatedKeyResult(changedKeyResult, updatedKeyResult.keyResult());
    }

    private void assertSameKeyResult(KeyResult expected, KeyResult actual) {
        assertEquals(expected.getId(), actual.getId());
        assertKeyResult(expected, actual);
    }

    private void assertRecreatedKeyResult(KeyResult expected, KeyResult actual) {
        assertEquals(expected.getId() + 1, actual.getId());
        assertKeyResult(expected, actual);
    }

    private void assertUpdatedKeyResult(KeyResult expected, KeyResult actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        if (expected instanceof KeyResultMetric) {
            assertEquals(KEY_RESULT_TYPE_ORDINAL, actual.getKeyResultType());
        } else if (expected instanceof KeyResultOrdinal) {
            assertEquals(KEY_RESULT_TYPE_METRIC, actual.getKeyResultType());
        } else {
            throw new IllegalArgumentException("keyResultType not supported, " + expected);
        }
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getOwner(), actual.getOwner());
        assertEquals(expected.getModifiedOn(), actual.getModifiedOn());
    }

    private static void assertKeyResult(KeyResult expected, KeyResult actual) {
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getKeyResultType(), actual.getKeyResultType());
        assertEquals(expected.getModifiedOn(), actual.getModifiedOn());
        if (expected instanceof KeyResultMetric keyResultMetric) {
            assertEquals(keyResultMetric.getBaseline(), ((KeyResultMetric) actual).getBaseline());
            assertEquals(keyResultMetric.getStretchGoal(), ((KeyResultMetric) actual).getStretchGoal());
            assertEquals(keyResultMetric.getUnit(), ((KeyResultMetric) actual).getUnit());
        } else if (expected instanceof KeyResultOrdinal keyResultOrdinal) {
            assertEquals(keyResultOrdinal.getCommitZone(), ((KeyResultOrdinal) actual).getCommitZone());
            assertEquals(keyResultOrdinal.getStretchZone(), ((KeyResultOrdinal) actual).getStretchZone());
            assertEquals(keyResultOrdinal.getTargetZone(), ((KeyResultOrdinal) actual).getTargetZone());
        } else {
            throw new IllegalArgumentException("keyResultType not supported, " + expected);
        }
    }

    private void assertSameActions(List<Action> expected, KeyResultWithActionList actual) {
        assertSameKeyResultIds(expected, actual);
        expected.forEach(this::removeKeyResult);
        actual.actionList().forEach(this::removeKeyResult);
        assertThat(expected).hasSameElementsAs(actual.actionList());
    }

    private void assertUpdatedActions(List<Action> expected, KeyResultWithActionList actual) {
        assertSameKeyResultIds(expected, actual);
        assertSameActionIds(expected, actual);
        assertUpdateVersions(expected, actual);
    }

    private void assertSameKeyResultIds(List<Action> expected, KeyResultWithActionList actual) {
        List<Long> expectedIds = expected.stream().map(action -> action.getKeyResult().getId()).toList();
        List<Long> updatedIds = actual.actionList().stream().map(action -> action.getKeyResult().getId()).toList();
        assertThat(expectedIds).hasSameElementsAs(updatedIds);
    }

    private void assertSameActionIds(List<Action> expected, KeyResultWithActionList actual) {
        List<Long> expectedIds = expected.stream().map(Action::getId).toList();
        List<Long> updatedIds = actual.actionList().stream().map(Action::getId).toList();
        assertThat(expectedIds).hasSameElementsAs(updatedIds);
    }

    private void assertUpdateVersions(List<Action> expected, KeyResultWithActionList actual) {
        List<Integer> expectedVersions = expected.stream().map(action -> action.getVersion() + 1).toList();
        List<Integer> updatedVerisons = actual.actionList().stream().map(Action::getVersion).toList();
        assertThat(expectedVersions).hasSameElementsAs(updatedVerisons);
    }

    private void removeKeyResult(Action action) {
        action.setKeyResult(null);
    }
}
