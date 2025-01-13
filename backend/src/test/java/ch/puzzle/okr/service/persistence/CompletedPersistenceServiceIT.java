package ch.puzzle.okr.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

@SpringIntegrationTest
class CompletedPersistenceServiceIT {
    private static final Logger log = LoggerFactory.getLogger(CompletedPersistenceServiceIT.class);
    @Autowired
    private CompletedPersistenceService completedPersistenceService;
    private Completed createdCompleted;
    private static final String OBJECTIVE_COMMENT = "Wir haben es gut geschafft";
    public static final String OBJECTIVE_TITLE = "Gute Lernende";
    public static final long OBJECTIVE_ID = 3L;

    private static Completed createCompleted(Long id) {
        return createCompleted(id, 1);
    }

    private static Completed createCompleted(Long id, int version) {
        return Completed.Builder
                .builder()
                .withId(id)
                .withVersion(version)
                .withObjective(Objective.Builder.builder().withId(OBJECTIVE_ID).withTitle(OBJECTIVE_TITLE).build())
                .withComment(OBJECTIVE_COMMENT)
                .build();
    }

    private static final String COMPLETED = "Completed";

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @AfterEach
    void tearDown() {
        try {
            if (createdCompleted != null) {
                completedPersistenceService.findById(createdCompleted.getId());
                completedPersistenceService.deleteById(createdCompleted.getId());
            }
        } catch (ResponseStatusException ex) {
            // created completed already deleted
        } finally {
            createdCompleted = null;
        }
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Should save entity on save()")
    @Test
    void saveCompletedShouldSaveCompleted() {
        createdCompleted = completedPersistenceService.save(createCompleted(null));

        assertNotNull(createdCompleted.getId());
        assertEquals(OBJECTIVE_COMMENT, createdCompleted.getComment());
        assertEquals(OBJECTIVE_ID, createdCompleted.getObjective().getId());
        assertEquals(OBJECTIVE_TITLE, createdCompleted.getObjective().getTitle());
    }

    @DisplayName("Should update entity on save() when the entity already exists")
    @Test
    void updateCompletedShouldSaveCompleted() {
        createdCompleted = completedPersistenceService.save(createCompleted(null));
        Completed updateCompleted = createCompleted(createdCompleted.getId(), createdCompleted.getVersion());
        updateCompleted.setComment("Updated completed");

        Completed updatedCompleted = completedPersistenceService.save(updateCompleted);

        assertEquals(createdCompleted.getId(), updatedCompleted.getId());
        assertEquals(createdCompleted.getVersion() + 1, updatedCompleted.getVersion());
        assertEquals(updateCompleted.getComment(), updatedCompleted.getComment());
    }

    @DisplayName("Should throw exception on save() when entity was already updated in the mean time")
    @Test
    void updateCompletedShouldThrowExceptionWhenAlreadyUpdated() {
        createdCompleted = completedPersistenceService.save(createCompleted(null));
        Completed updateCompleted = createCompleted(createdCompleted.getId(), 0);
        updateCompleted.setComment("Updated completed");

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> completedPersistenceService.save(updateCompleted));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("DATA_HAS_BEEN_UPDATED", List.of(COMPLETED)));

        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should return correct entity on getCompletedByObjectiveId()")
    @Test
    void getCompletedShouldGetCompletedByObjectiveId() {
        Completed savedCompleted = completedPersistenceService.getCompletedByObjectiveId(6L);

        assertNotNull(savedCompleted.getId());
        assertEquals("War leider nicht moeglich", savedCompleted.getComment());
        assertEquals("Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.",
                     savedCompleted.getObjective().getTitle());
    }

    @DisplayName("Should throw exception on getCompletedByObjectiveId() when id does not exist")
    @Test
    void getCompletedShouldThrowExceptionWhenCompletedNotFound() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> completedPersistenceService
                                                                    .getCompletedByObjectiveId(-1L));

        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of(COMPLETED, String.valueOf(-1))));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should delete entity on deleteById()")
    @Test
    void deleteByIdShouldDeleteExistingCompletedByObjectiveId() {

        completedPersistenceService.deleteById(3L);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> completedPersistenceService.findById(3L));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of(COMPLETED, "3")));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on findById() when id does not exist")
    @Test
    void deleteCompletedShouldThrowExceptionWhenCompletedNotFound() {

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> completedPersistenceService.findById(-1L));

        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of(COMPLETED, String.valueOf(-1))));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }
}
