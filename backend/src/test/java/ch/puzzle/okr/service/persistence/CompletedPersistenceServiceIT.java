package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@SpringIntegrationTest
class CompletedPersistenceServiceIT {
    @Autowired
    private CompletedPersistenceService completedPersistenceService;
    private Completed createdCompleted;

    private static Completed createCompleted(Long id) {
        return createCompleted(id, 1);
    }

    private static Completed createCompleted(Long id, int version) {
        return Completed.Builder.builder().withId(id).withVersion(version)
                .withObjective(Objective.Builder.builder().withId(3L).withTitle("Gute Lernende").build())
                .withComment("Wir haben es gut geschafft").build();
    }

    private static final String COMPLETED = "Completed";

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
    }

    @Test
    void saveCompletedShouldSaveCompleted() {
        createdCompleted = completedPersistenceService.save(createCompleted(null));

        assertNotNull(createdCompleted.getId());
        assertEquals(createdCompleted.getComment(), createdCompleted.getComment());
        assertEquals(createdCompleted.getObjective(), createdCompleted.getObjective());
    }

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

    @Test
    void getCompletedShouldGetCompletedByObjectiveId() {
        Completed savedCompleted = completedPersistenceService.getCompletedByObjectiveId(6L);

        assertNotNull(savedCompleted.getId());
        assertEquals("War leider nicht moeglich", savedCompleted.getComment());
        assertEquals("Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.",
                savedCompleted.getObjective().getTitle());
    }

    @Test
    void deleteCompletedIdShouldDeleteExistingCompletedByObjectiveId() {

        completedPersistenceService.deleteById(3L);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> completedPersistenceService.findById(3L));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of(COMPLETED, "3")));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void deleteCompletedShouldThrowExceptionWhenCompletedNotFound() {
        createdCompleted = completedPersistenceService.save(createCompleted(33L));
        completedPersistenceService.deleteById(createdCompleted.getId());

        Long completedId = createdCompleted.getId();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> completedPersistenceService.findById(completedId));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of(COMPLETED, "200")));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }
}
