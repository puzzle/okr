package ch.puzzle.okr.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringIntegrationTest
class KeyResultPersistenceServiceIT {
    KeyResult createdKeyResult;
    @Autowired
    private KeyResultPersistenceService keyResultPersistenceService;

    private static KeyResult createKeyResultMetric(Long id) {
        return KeyResultMetric.Builder
                .builder()
                .withBaseline(3.0)
                .withStretchGoal(5.0)
                .withUnit(Unit.FTE)
                .withId(id)
                .withTitle("Title")
                .withCreatedBy(User.Builder.builder().withId(1L).build())
                .withOwner(User.Builder.builder().withId(1L).build())
                .withObjective(Objective.Builder.builder().withId(4L).build())
                .withCreatedOn(LocalDateTime.now())
                .build();
    }

    private static KeyResult createKeyResultOrdinal(Long id) {
        return createKeyResultOrdinal(id, 1);
    }

    private static KeyResult createKeyResultOrdinal(Long id, int version) {
        return KeyResultOrdinal.Builder
                .builder()
                .withCommitZone("Hamster")
                .withTargetZone("Katze")
                .withStretchZone("ZOO")
                .withId(id)
                .withVersion(version)
                .withTitle("Ordinal KeyResult")
                .withCreatedBy(User.Builder.builder().withId(1L).build())
                .withOwner(User.Builder.builder().withId(1L).build())
                .withObjective(Objective.Builder.builder().withId(4L).build())
                .withCreatedOn(LocalDateTime.now())
                .build();
    }

    private static final String KEY_RESULT_TITLE = "Updated Key Result";
    private static final String KEY_RESULT_DESCRIPTION = "This is a new description";
    private static final String MODEL_NOT_FOUND = "MODEL_WITH_ID_NOT_FOUND";
    private static final String KEY_RESULT = "KeyResult";

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

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
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Should save new key result on save()")
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

    @DisplayName("Should return correct key result on findById()")
    @Test
    void shouldFindKeyResultById() {
        KeyResult keyResult = keyResultPersistenceService.findById(3L);

        assertEquals(3L, keyResult.getId());
        assertEquals("Steigern der URS um 25%", keyResult.getTitle());
    }

    @DisplayName("Should throw exception on findById() when id does not exist")
    @Test
    void getKeyResultByIdShouldThrowExceptionWhenKeyResultNotFound() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> keyResultPersistenceService.findById(321L));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto(MODEL_NOT_FOUND, List.of(KEY_RESULT, "321")));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on findById() when id is null")
    @Test
    void getKeyResultByIdShouldThrowExceptionWhenKeyResultIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> keyResultPersistenceService.findById(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", KEY_RESULT)));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should only update entity on recreateEntity() when no type change was done")
    @Test
    void recreateEntityShouldUpdateKeyResultNoTypeChange() {
        KeyResult keyResult = createKeyResultOrdinal(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        createdKeyResult.setTitle(KEY_RESULT_TITLE);
        Long keyResultId = createdKeyResult.getId();

        KeyResult recreatedKeyResult = keyResultPersistenceService
                .recreateEntity(createdKeyResult.getId(), createdKeyResult);

        assertNotNull(createdKeyResult.getId());
        assertEquals(KEY_RESULT_TITLE, recreatedKeyResult.getTitle());
        assertEquals(createdKeyResult.getOwner().getId(), recreatedKeyResult.getOwner().getId());
        assertEquals(createdKeyResult.getObjective().getId(), recreatedKeyResult.getObjective().getId());

        // Should delete the old KeyResult
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> keyResultPersistenceService.findById(keyResultId));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(MODEL_NOT_FOUND, List.of(KEY_RESULT, keyResultId)));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));

        // delete re-created key result in tearDown()
        createdKeyResult = recreatedKeyResult;
    }

    @DisplayName("Should change type on recreateEntity() when type change was done")
    @Test
    void recreateEntityShouldUpdateKeyResultWithTypeChange() {
        KeyResult keyResult = createKeyResultMetric(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);

        KeyResult keyResultOrdinal = KeyResultOrdinal.Builder
                .builder()
                .withCommitZone("Hund")
                .withTargetZone("Hund + Katze")
                .withStretchZone("Zoo")
                .withId(createdKeyResult.getId())
                .withTitle(KEY_RESULT_TITLE)
                .withObjective(createdKeyResult.getObjective())
                .withOwner(createdKeyResult.getOwner())
                .withCreatedBy(createdKeyResult.getCreatedBy())
                .withCreatedOn(createdKeyResult.getCreatedOn())
                .build();

        KeyResult recreatedKeyResult = keyResultPersistenceService
                .recreateEntity(keyResultOrdinal.getId(), keyResultOrdinal);

        assertNotNull(createdKeyResult.getId());
        assertEquals(createdKeyResult.getObjective().getId(), recreatedKeyResult.getObjective().getId());
        assertEquals(KEY_RESULT_TITLE, recreatedKeyResult.getTitle());
        assertEquals(createdKeyResult.getOwner().getId(), recreatedKeyResult.getOwner().getId());

        Long keyResultId = createdKeyResult.getId();
        // Should delete the old KeyResult
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> keyResultPersistenceService.findById(keyResultId));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(MODEL_NOT_FOUND, List.of(KEY_RESULT, keyResultId)));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));

        // delete re-created key result in tearDown()
        createdKeyResult = recreatedKeyResult;
    }

    @DisplayName("Should update key result on updateEntity()")
    @Test
    void updateEntityShouldUpdateKeyResult() {
        KeyResult keyResult = createKeyResultOrdinal(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        KeyResult updateKeyResult = createKeyResultOrdinal(createdKeyResult.getId(), createdKeyResult.getVersion());
        updateKeyResult.setTitle(KEY_RESULT_TITLE);
        updateKeyResult.setDescription(KEY_RESULT_DESCRIPTION);

        KeyResult updatedKeyResult = keyResultPersistenceService.updateEntity(updateKeyResult);

        assertEquals(createdKeyResult.getId(), updatedKeyResult.getId());
        assertEquals(createdKeyResult.getVersion() + 1, updatedKeyResult.getVersion());
        assertEquals(KEY_RESULT_TITLE, updatedKeyResult.getTitle());
        assertEquals(KEY_RESULT_DESCRIPTION, updatedKeyResult.getDescription());
        assertEquals(createdKeyResult.getOwner().getId(), updatedKeyResult.getOwner().getId());
        assertEquals(createdKeyResult.getObjective().getId(), updatedKeyResult.getObjective().getId());
        assertEquals(createdKeyResult.getModifiedOn(), updatedKeyResult.getModifiedOn());
    }

    @DisplayName("Should throw exception on updateEntity() when entity was already updated in the mean time")
    @Test
    void updateEntityShouldThrowExceptionWhenAlreadyUpdated() {
        KeyResult keyResult = createKeyResultOrdinal(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        KeyResult updateKeyResult = createKeyResultOrdinal(createdKeyResult.getId(), 0);
        updateKeyResult.setTitle(KEY_RESULT_TITLE);
        updateKeyResult.setDescription(KEY_RESULT_DESCRIPTION);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> keyResultPersistenceService
                                                                    .updateEntity(updateKeyResult));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("DATA_HAS_BEEN_UPDATED", List.of(KEY_RESULT)));

        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should return list of key results on getKeyResultsByObjective()")
    @Test
    void getKeyResultsByObjectiveShouldReturnListOfKeyResults() {
        List<KeyResult> keyResultsByObjective = keyResultPersistenceService.getKeyResultsByObjective(3L);

        assertEquals(3, keyResultsByObjective.size());
    }

    @DisplayName("Should delete entity on deleteById()")
    @Test
    void deleteKeyResultByIdShouldDeleteExistingKeyResult() {
        KeyResult keyResult = createKeyResultMetric(null);
        createdKeyResult = keyResultPersistenceService.save(keyResult);
        keyResultPersistenceService.deleteById(createdKeyResult.getId());

        Long keyResultId = createdKeyResult.getId();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> keyResultPersistenceService.findById(keyResultId));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(MODEL_NOT_FOUND, List.of(KEY_RESULT, keyResultId)));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on deleteById() when id does not exist")
    @Test
    void deleteKeyResultShouldThrowExceptionWhenKeyResultNotFound() {
        long nonExistentId = getNonExistentId();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> keyResultPersistenceService.findById(nonExistentId));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(MODEL_NOT_FOUND, List.of(KEY_RESULT, nonExistentId)));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    private long getNonExistentId() {
        long id = keyResultPersistenceService.findAll().stream().mapToLong(KeyResult::getId).max().orElse(10L);

        return id + 1;
    }

    private void execute() {
        keyResultPersistenceService.findById(createdKeyResult.getId());
    }
}
