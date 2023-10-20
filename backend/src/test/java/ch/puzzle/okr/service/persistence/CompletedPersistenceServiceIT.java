package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
class CompletedPersistenceServiceIT {
    @Autowired
    private CompletedPersistenceService completedPersistenceService;

    private static Completed createCompleted(Long id) {
        return Completed.Builder.builder().withId(id)
                .withObjective(Objective.Builder.builder().withId(3L).withTitle("Gute Lernende").build())
                .withComment("Wir haben es gut geschafft").build();
    }

    @Test
    void saveCompletedShouldSaveCompleted() {
        Completed createdCompleted = completedPersistenceService.save(createCompleted(null));

        assertNotNull(createdCompleted.getId());
        assertEquals(createdCompleted.getComment(), createdCompleted.getComment());
        assertEquals(createdCompleted.getObjective(), createdCompleted.getObjective());
    }

    @Test
    void deleteCompletedShouldGetCompletedByObjectiveId() {
        Completed savedCompleted = completedPersistenceService.getCompletedByObjectiveId(6L);

        assertNotNull(savedCompleted.getId());
        assertEquals("War leider nicht moeglich", savedCompleted.getComment());
        assertEquals("Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.",
                savedCompleted.getObjective().getTitle());
    }

    @Test
    void deleteCompletedIdShouldDeleteExistingCompletedByObjectiveId() {

        completedPersistenceService.deleteById(3L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> completedPersistenceService.findById(3L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Completed with id %d not found", 3), exception.getReason());
    }

    @Test
    void deleteCompletedShouldThrowExceptionWhenCompletedNotFound() {
        Completed newCompleted = completedPersistenceService.save(createCompleted(33L));
        completedPersistenceService.deleteById(newCompleted.getId());

        Long completedId = newCompleted.getId();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> completedPersistenceService.findById(completedId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Completed with id %d not found", newCompleted.getId()), exception.getReason());
    }
}
