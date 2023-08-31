package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.keyresult.KeyResult;
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

    private static KeyResult createKeyResult(Long id) {
        User user = User.Builder.builder().withId(1L).build();
        Objective objective = Objective.Builder.builder().withId(4L).build();
        return KeyResult.Builder.builder().withId(id).withUnit(Unit.PERCENT).withTitle("Keyresult 1")
                .withObjective(objective).withOwner(user).withCreatedBy(user).withModifiedOn(LocalDateTime.now())
                .withExpectedEvolution(ExpectedEvolution.INCREASE).withTargetValue(2.0).build();
    }

    @AfterEach
    void tearDown() {
        try {
            if (createdKeyResult != null) {
                keyResultPersistenceService.getKeyResultById(createdKeyResult.getId());
                keyResultPersistenceService.deleteKeyResultById(createdKeyResult.getId());
            }
        } catch (ResponseStatusException ex) {
            // created key result already deleted
        } finally {
            createdKeyResult = null;
        }
    }

    @Test
    void saveKeyResult_ShouldSaveNewKeyResult() {
        KeyResult keyResult = createKeyResult(null);

        createdKeyResult = keyResultPersistenceService.saveKeyResult(keyResult);

        assertNotNull(createdKeyResult.getId());
        assertEquals(keyResult.getUnit(), createdKeyResult.getUnit());
        assertEquals(keyResult.getTitle(), createdKeyResult.getTitle());
        assertEquals(keyResult.getObjective(), createdKeyResult.getObjective());
        assertEquals(keyResult.getOwner(), createdKeyResult.getOwner());
        assertEquals(keyResult.getExpectedEvolution(), createdKeyResult.getExpectedEvolution());
        assertEquals(keyResult.getTargetValue(), createdKeyResult.getTargetValue());
    }

    @Test
    void getKeyResultById_ShouldReturnKeyResultProperly() {
        KeyResult keyResult = keyResultPersistenceService.getKeyResultById(3L);

        assertEquals(3L, keyResult.getId());
        assertEquals("Steigern der URS um 25%", keyResult.getTitle());
    }

    @Test
    void getKeyResultById_ShouldThrowExceptionWhenKeyResultNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultPersistenceService.getKeyResultById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Key result with id 321 not found", exception.getReason());
    }

    @Test
    void getKeyResultById_ShouldThrowExceptionWhenKeyResultIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultPersistenceService.getKeyResultById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing attribute key result id", exception.getReason());
    }

    @Test
    void updateKeyResult_ShouldUpdateKeyResult() {
        KeyResult keyResult = createKeyResult(null);
        createdKeyResult = keyResultPersistenceService.saveKeyResult(keyResult);
        createdKeyResult.setTitle("Updated Key Result");

        KeyResult updatedKeyResult = keyResultPersistenceService.updateKeyResult(createdKeyResult);

        assertEquals(createdKeyResult.getId(), updatedKeyResult.getId());
        assertEquals("Updated Key Result", updatedKeyResult.getTitle());
    }

    @Test
    void updateKeyResult_ShouldThrowExceptionWhenKeyResultNotFound() {
        KeyResult keyResult = createKeyResult(321L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultPersistenceService.updateKeyResult(keyResult));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Could not find key result with id 321", exception.getReason());
    }

    @Test
    void getKeyResultsByObjective_ShouldReturnListOfKeyResults() {
        List<KeyResult> keyResultsByObjective = keyResultPersistenceService
                .getKeyResultsByObjective(Objective.Builder.builder().withId(3L).build());

        assertEquals(3, keyResultsByObjective.size());
    }

    @Test
    void deleteKeyResultById_ShouldDeleteExistingKeyResult() {
        KeyResult keyResult = createKeyResult(null);
        createdKeyResult = keyResultPersistenceService.saveKeyResult(keyResult);
        keyResultPersistenceService.deleteKeyResultById(createdKeyResult.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultPersistenceService.getKeyResultById(createdKeyResult.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Key result with id %d not found", createdKeyResult.getId()), exception.getReason());
    }
}
