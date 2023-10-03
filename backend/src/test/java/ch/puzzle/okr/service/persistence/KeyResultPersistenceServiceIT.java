package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
public class KeyResultPersistenceServiceIT {
    KeyResult createdKeyResult;
    @Autowired
    private KeyResultPersistenceService keyResultPersistenceService;
    @Autowired
    private ObjectivePersistenceService objectivePersistenceService;

    private static KeyResult createKeyResultMetric(Long id) {
        return KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(5.0).withUnit("ECTS").withId(id)
                .withTitle("Title").withCreatedBy(User.Builder.builder().withId(1L).build())
                .withOwner(User.Builder.builder().withId(1L).build())
                .withObjective(Objective.Builder.builder().withId(4L).build()).withCreatedOn(LocalDateTime.now())
                .build();
    }

    private static KeyResult createKeyResultOrdinal(Long id) {
        return KeyResultOrdinal.Builder.builder().withCommitZone("Hamster").withTargetZone("Katze")
                .withStretchZone("ZOO").withId(id).withTitle("Ordinal KeyResult")
                .withCreatedBy(User.Builder.builder().withId(1L).build())
                .withOwner(User.Builder.builder().withId(1L).build())
                .withObjective(Objective.Builder.builder().withId(4L).build()).withCreatedOn(LocalDateTime.now())
                .build();
    }

    private static final String KEY_RESULT_UPDATED = "Updated Key Result";

    @AfterEach
    void tearDown() {
        try {
            if (createdKeyResult != null) {
                keyResultPersistenceService.findById(createdKeyResult.getId());
                keyResultPersistenceService.deleteById(createdKeyResult.getId());
            }
        } catch (ResponseStatusException ex) {
            // created key result already deleted
        } finally {
            createdKeyResult = null;
        }
    }

    @Test
    void saveKeyResult_ShouldSaveNewKeyResult() {
        KeyResult keyResult = createKeyResultMetric(null);

        createdKeyResult = keyResultPersistenceService.save(keyResult);

        assertNotNull(createdKeyResult.getId());
        assertEquals(keyResult.getModifiedOn(), createdKeyResult.getModifiedOn());
        assertEquals(keyResult.getTitle(), createdKeyResult.getTitle());
        assertEquals(keyResult.getObjective(), createdKeyResult.getObjective());
        assertEquals(keyResult.getOwner(), createdKeyResult.getOwner());
        assertEquals(keyResult.getDescription(), createdKeyResult.getDescription());
    }

    @Test
    void getKeyResultById_ShouldReturnKeyResultProperly() {
        KeyResult keyResult = keyResultPersistenceService.findById(3L);

        assertEquals(3L, keyResult.getId());
        assertEquals("Steigern der URS um 25%", keyResult.getTitle());
    }

    @Test
    void getKeyResultById_ShouldThrowExceptionWhenKeyResultNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultPersistenceService.findById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("KeyResult with id 321 not found", exception.getReason());
    }

    @Test
    void getKeyResultById_ShouldThrowExceptionWhenKeyResultIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultPersistenceService.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing identifier for KeyResult", exception.getReason());
    }

    @Test
    void recreateEntity_ShouldUpdateKeyResultNoTypeChange() {
        KeyResult keyResult = createKeyResultOrdinal(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        createdKeyResult.setTitle(KEY_RESULT_UPDATED);

        KeyResult updatedKeyResult = keyResultPersistenceService.recreateEntity(createdKeyResult.getId(),
                createdKeyResult);

        assertNotNull(createdKeyResult.getId());
        assertEquals(KEY_RESULT_UPDATED, updatedKeyResult.getTitle());
        assertEquals(createdKeyResult.getOwner().getId(), updatedKeyResult.getOwner().getId());
        assertEquals(createdKeyResult.getObjective().getId(), updatedKeyResult.getObjective().getId());

        // Should delete the old KeyResult
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            keyResultPersistenceService.findById(createdKeyResult.getId());
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("KeyResult with id " + createdKeyResult.getId() + " not found", exception.getReason());
    }

    @Test
    void recreateEntity_ShouldUpdateKeyResultWithTypeChange() {
        KeyResult keyResult = createKeyResultMetric(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        createdKeyResult.setKeyResultType("ordinal");

        KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder().withCommitZone("Hund")
                .withTargetZone("Hund + Katze").withStretchZone("Zoo").withId(createdKeyResult.getId())
                .withTitle(KEY_RESULT_UPDATED).withObjective(createdKeyResult.getObjective())
                .withOwner(createdKeyResult.getOwner()).withCreatedBy(createdKeyResult.getCreatedBy())
                .withCreatedOn(createdKeyResult.getCreatedOn()).build();

        KeyResult updatedKeyResult = keyResultPersistenceService.recreateEntity(keyResultOrdinal.getId(),
                keyResultOrdinal);

        assertNotNull(createdKeyResult.getId());
        assertEquals(createdKeyResult.getObjective().getId(), updatedKeyResult.getObjective().getId());
        assertEquals(KEY_RESULT_UPDATED, updatedKeyResult.getTitle());
        assertEquals(createdKeyResult.getOwner().getId(), updatedKeyResult.getOwner().getId());

        // Should delete the old KeyResult
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            keyResultPersistenceService.findById(createdKeyResult.getId());
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("KeyResult with id " + createdKeyResult.getId() + " not found", exception.getReason());
    }

    @Test
    void updateEntity_ShouldUpdateKeyResult() {
        KeyResult keyResult = createKeyResultOrdinal(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        createdKeyResult.setTitle(KEY_RESULT_UPDATED);
        createdKeyResult.setDescription("This is a new description");

        KeyResult updatedKeyResult = keyResultPersistenceService.updateEntity(createdKeyResult);

        assertEquals(createdKeyResult.getId(), updatedKeyResult.getId());
        assertEquals(KEY_RESULT_UPDATED, updatedKeyResult.getTitle());
        assertEquals("This is a new description", updatedKeyResult.getDescription());
        assertEquals(createdKeyResult.getOwner().getId(), updatedKeyResult.getOwner().getId());
        assertEquals(createdKeyResult.getObjective().getId(), updatedKeyResult.getObjective().getId());
        assertEquals(createdKeyResult.getModifiedOn(), updatedKeyResult.getModifiedOn());
    }

    @Test
    void getKeyResultsByObjective_ShouldReturnListOfKeyResults() {
        List<KeyResult> keyResultsByObjective = keyResultPersistenceService
                .getKeyResultsByObjective(Objective.Builder.builder().withId(3L).build());

        assertEquals(3, keyResultsByObjective.size());
    }

    @Test
    void deleteKeyResultById_ShouldDeleteExistingKeyResult() {
        KeyResult keyResult = createKeyResultMetric(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        keyResultPersistenceService.deleteById(createdKeyResult.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultPersistenceService.findById(createdKeyResult.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("KeyResult with id %d not found", createdKeyResult.getId()), exception.getReason());
    }

    @Test
    void deleteKeyResult_ShouldThrowExceptionWhenKeyResultNotFound() {
        KeyResult keyResult = createKeyResultMetric(35234L);
        KeyResult newKeyResult = keyResultPersistenceService.save(keyResult);
        keyResultPersistenceService.deleteById(newKeyResult.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultPersistenceService.findById(newKeyResult.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("KeyResult with id %d not found", newKeyResult.getId()), exception.getReason());
    }
}
