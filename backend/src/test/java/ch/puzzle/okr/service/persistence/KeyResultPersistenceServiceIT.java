package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@SpringIntegrationTest
class KeyResultPersistenceServiceIT {
    KeyResult createdKeyResult;
    @Autowired
    private KeyResultPersistenceService keyResultPersistenceService;

    private static KeyResult createKeyResultMetric(Long id) {
        return KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(5.0).withUnit(Unit.FTE).withId(id)
                .withTitle("Title").withCreatedBy(User.Builder.builder().withId(1L).build())
                .withOwner(User.Builder.builder().withId(1L).build())
                .withObjective(Objective.Builder.builder().withId(4L).build()).withCreatedOn(LocalDateTime.now())
                .build();
    }

    private static KeyResult createKeyResultOrdinal(Long id) {
        return createKeyResultOrdinal(id, 1);
    }

    private static KeyResult createKeyResultOrdinal(Long id, int version) {
        return KeyResultOrdinal.Builder.builder().withCommitZone("Hamster").withTargetZone("Katze")
                .withStretchZone("ZOO").withId(id).withVersion(version).withTitle("Ordinal KeyResult")
                .withCreatedBy(User.Builder.builder().withId(1L).build())
                .withOwner(User.Builder.builder().withId(1L).build())
                .withObjective(Objective.Builder.builder().withId(4L).build()).withCreatedOn(LocalDateTime.now())
                .build();
    }

    private static final String KEY_RESULT_UPDATED = "Updated Key Result";
    private static final String THIS_IS_DESCRIPTION = "This is a new description";
    private static final String MODEL_NOT_FOUND = "MODEL_WITH_ID_NOT_FOUND";
    private static final String KEYRESULT = "KeyResult";

    @AfterEach
    void tearDown() {
        try {
            if (createdKeyResult != null) {
                keyResultPersistenceService.findById(createdKeyResult.getId());
                keyResultPersistenceService.deleteById(createdKeyResult.getId());
            }
        } catch (OkrResponseStatusException ex) {
            // created key result already deleted
        } finally {
            createdKeyResult = null;
        }
    }

    @Test
    void saveKeyResultShouldSaveNewKeyResult() {
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
    void getKeyResultByIdShouldReturnKeyResultProperly() {
        KeyResult keyResult = keyResultPersistenceService.findById(3L);

        assertEquals(3L, keyResult.getId());
        assertEquals("Steigern der URS um 25%", keyResult.getTitle());
    }

    @Test
    void getKeyResultByIdShouldThrowExceptionWhenKeyResultNotFound() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> keyResultPersistenceService.findById(321L));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto(MODEL_NOT_FOUND, List.of(KEYRESULT, "321")));

        assertEquals(NOT_FOUND, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void getKeyResultByIdShouldThrowExceptionWhenKeyResultIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> keyResultPersistenceService.findById(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", KEYRESULT)));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void recreateEntityShouldUpdateKeyResultNoTypeChange() {
        KeyResult keyResult = createKeyResultOrdinal(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        createdKeyResult.setTitle(KEY_RESULT_UPDATED);

        KeyResult recreatedKeyResult = keyResultPersistenceService.recreateEntity(createdKeyResult.getId(),
                createdKeyResult);

        assertNotNull(createdKeyResult.getId());
        assertEquals(KEY_RESULT_UPDATED, recreatedKeyResult.getTitle());
        assertEquals(createdKeyResult.getOwner().getId(), recreatedKeyResult.getOwner().getId());
        assertEquals(createdKeyResult.getObjective().getId(), recreatedKeyResult.getObjective().getId());

        Long keyResultId = createdKeyResult.getId();
        // Should delete the old KeyResult
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class, this::execute);

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(MODEL_NOT_FOUND, List.of(KEYRESULT, keyResultId)));

        assertEquals(NOT_FOUND, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));

        // delete re-created key result in tearDown()
        createdKeyResult = recreatedKeyResult;
    }

    @Test
    void recreateEntityShouldUpdateKeyResultWithTypeChange() {
        KeyResult keyResult = createKeyResultMetric(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);

        KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder().withCommitZone("Hund")
                .withTargetZone("Hund + Katze").withStretchZone("Zoo").withId(createdKeyResult.getId())
                .withTitle(KEY_RESULT_UPDATED).withObjective(createdKeyResult.getObjective())
                .withOwner(createdKeyResult.getOwner()).withCreatedBy(createdKeyResult.getCreatedBy())
                .withCreatedOn(createdKeyResult.getCreatedOn()).build();

        KeyResult recreatedKeyResult = keyResultPersistenceService.recreateEntity(keyResultOrdinal.getId(),
                keyResultOrdinal);

        assertNotNull(createdKeyResult.getId());
        assertEquals(createdKeyResult.getObjective().getId(), recreatedKeyResult.getObjective().getId());
        assertEquals(KEY_RESULT_UPDATED, recreatedKeyResult.getTitle());
        assertEquals(createdKeyResult.getOwner().getId(), recreatedKeyResult.getOwner().getId());

        Long keyResultId = createdKeyResult.getId();
        // Should delete the old KeyResult
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> keyResultPersistenceService.findById(keyResultId));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(MODEL_NOT_FOUND, List.of(KEYRESULT, keyResultId)));

        assertEquals(NOT_FOUND, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));

        // delete re-created key result in tearDown()
        createdKeyResult = recreatedKeyResult;
    }

    @Test
    void updateEntityShouldUpdateKeyResult() {
        KeyResult keyResult = createKeyResultOrdinal(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        KeyResult updateKeyResult = createKeyResultOrdinal(createdKeyResult.getId(), createdKeyResult.getVersion());
        updateKeyResult.setTitle(KEY_RESULT_UPDATED);
        updateKeyResult.setDescription(THIS_IS_DESCRIPTION);

        KeyResult updatedKeyResult = keyResultPersistenceService.updateEntity(updateKeyResult);

        assertEquals(createdKeyResult.getId(), updatedKeyResult.getId());
        assertEquals(createdKeyResult.getVersion() + 1, updatedKeyResult.getVersion());
        assertEquals(KEY_RESULT_UPDATED, updatedKeyResult.getTitle());
        assertEquals(THIS_IS_DESCRIPTION, updatedKeyResult.getDescription());
        assertEquals(createdKeyResult.getOwner().getId(), updatedKeyResult.getOwner().getId());
        assertEquals(createdKeyResult.getObjective().getId(), updatedKeyResult.getObjective().getId());
        assertEquals(createdKeyResult.getModifiedOn(), updatedKeyResult.getModifiedOn());
    }

    @Test
    void updateEntityShouldThrowExceptionWhenAlreadyUpdated() {
        KeyResult keyResult = createKeyResultOrdinal(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        KeyResult updateKeyResult = createKeyResultOrdinal(createdKeyResult.getId(), 0);
        updateKeyResult.setTitle(KEY_RESULT_UPDATED);
        updateKeyResult.setDescription(THIS_IS_DESCRIPTION);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> keyResultPersistenceService.updateEntity(updateKeyResult));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("DATA_HAS_BEEN_UPDATED", List.of(KEYRESULT)));

        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void getKeyResultsByObjectiveShouldReturnListOfKeyResults() {
        List<KeyResult> keyResultsByObjective = keyResultPersistenceService.getKeyResultsByObjective(3L);

        assertEquals(3, keyResultsByObjective.size());
    }

    @Test
    void deleteKeyResultByIdShouldDeleteExistingKeyResult() {
        KeyResult keyResult = createKeyResultMetric(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        keyResultPersistenceService.deleteById(createdKeyResult.getId());

        Long keyResultId = createdKeyResult.getId();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> keyResultPersistenceService.findById(keyResultId));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(MODEL_NOT_FOUND, List.of(KEYRESULT, keyResultId)));

        assertEquals(NOT_FOUND, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void deleteKeyResultShouldThrowExceptionWhenKeyResultNotFound() {
        KeyResult keyResult = createKeyResultMetric(35234L);
        KeyResult newKeyResult = keyResultPersistenceService.save(keyResult);
        keyResultPersistenceService.deleteById(newKeyResult.getId());

        Long keyResultId = newKeyResult.getId();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> keyResultPersistenceService.findById(keyResultId));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(MODEL_NOT_FOUND, List.of(KEYRESULT, keyResultId)));

        assertEquals(NOT_FOUND, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    private void execute() {
        keyResultPersistenceService.findById(createdKeyResult.getId());
    }
}
