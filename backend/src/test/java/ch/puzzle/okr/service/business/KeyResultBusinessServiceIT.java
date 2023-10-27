package ch.puzzle.okr.service.business;

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
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringIntegrationTest
class KeyResultBusinessServiceIT {
    private static final String KEY_RESULT_UPDATED = "Updated Key Result";
    private static final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    private KeyResult createdKeyResult;

    @Autowired
    private KeyResultBusinessService keyResultBusinessService;

    @Autowired
    private CheckInBusinessService checkInBusinessService;

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

    @AfterEach
    void tearDown() {
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

    @Test
    void updateEntity_shouldUpdateKeyResultWithSameTypeMetric() {
        createdKeyResult = keyResultBusinessService.createEntity(createKeyResultMetric(null), authorizationUser);
        createdKeyResult.setTitle(KEY_RESULT_UPDATED);

        KeyResult updatedKeyResult = keyResultBusinessService.updateEntity(createdKeyResult.getId(), createdKeyResult,
                authorizationUser);

        assertSameKeyResult(createdKeyResult, updatedKeyResult);
    }

    @Test
    void updateEntity_shouldUpdateKeyResultWithSameTypeOrdinal() {
        createdKeyResult = keyResultBusinessService.createEntity(createKeyResultOrdinal(null), authorizationUser);
        createdKeyResult.setTitle(KEY_RESULT_UPDATED);

        KeyResult updatedKeyResult = keyResultBusinessService.updateEntity(createdKeyResult.getId(), createdKeyResult,
                authorizationUser);

        assertSameKeyResult(createdKeyResult, updatedKeyResult);
    }

    @Test
    void updateKeyResultShouldRecreateKeyResultMetric() {
        KeyResult savedKeyResult = keyResultBusinessService.createEntity(createKeyResultOrdinal(null),
                authorizationUser);
        KeyResult updatedKeyResult = createKeyResultMetric(savedKeyResult.getId());

        createdKeyResult = keyResultBusinessService.updateEntity(updatedKeyResult.getId(), updatedKeyResult,
                authorizationUser);

        assertRecreatedKeyResult(updatedKeyResult, createdKeyResult);
    }

    @Test
    void updateKeyResultShouldRecreateKeyResultOrdinal() {
        KeyResult savedKeyResult = keyResultBusinessService.createEntity(createKeyResultMetric(null),
                authorizationUser);
        KeyResult updatedKeyResult = createKeyResultOrdinal(savedKeyResult.getId());

        createdKeyResult = keyResultBusinessService.updateEntity(updatedKeyResult.getId(), updatedKeyResult,
                authorizationUser);

        assertRecreatedKeyResult(updatedKeyResult, createdKeyResult);
    }

    @Test
    void updateKeyResultShouldUpdateKeyResultWithDifferentTypeAndCheckInMetric() {
        KeyResult savedKeyResult = keyResultBusinessService.createEntity(createKeyResultOrdinal(null),
                authorizationUser);
        checkInBusinessService.createEntity(createCheckInOrdinal(savedKeyResult), authorizationUser);

        KeyResult updatedKeyResult = createKeyResultMetric(savedKeyResult.getId());

        createdKeyResult = keyResultBusinessService.updateEntity(updatedKeyResult.getId(), updatedKeyResult,
                authorizationUser);

        assertUpdatedKeyResult(updatedKeyResult, createdKeyResult);
    }

    @Test
    void updateKeyResultShouldUpdateKeyResultWithDifferentTypeAndCheckInOrdinal() {
        KeyResult savedKeyResult = keyResultBusinessService.createEntity(createKeyResultMetric(null),
                authorizationUser);
        checkInBusinessService.createEntity(createCheckInMetric(savedKeyResult), authorizationUser);

        KeyResult updatedKeyResult = createKeyResultOrdinal(savedKeyResult.getId());

        createdKeyResult = keyResultBusinessService.updateEntity(updatedKeyResult.getId(), updatedKeyResult,
                authorizationUser);

        assertUpdatedKeyResult(updatedKeyResult, createdKeyResult);
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
}
