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
public class CompletedPersistenceServiceIT {
    @Autowired
    private CompletedPersistenceService completedPersistenceService;

    Completed successfulCompleted = Completed.Builder.builder().withId(1L)
            .withObjective(Objective.Builder.builder().withId(3L).withTitle("Gute Lernende").build())
            .withComment("Wir haben es gut geschafft").build();

    Completed createCompleted = Completed.Builder.builder()
            .withObjective(Objective.Builder.builder().withId(3L).withTitle("Gute Lernende").build())
            .withComment("Wir haben es gut geschafft").build();

    @Test
    void saveCompleted_ShouldSaveCompleted() {
        Completed createdCompleted = completedPersistenceService.save(createCompleted);

        assertNotNull(createdCompleted.getId());
        assertEquals(createdCompleted.getComment(), createdCompleted.getComment());
        assertEquals(createdCompleted.getObjective(), createdCompleted.getObjective());
    }

    @Test
    void deleteCompleted_ShouldGetCompletedByObjectiveId() {
        Completed savedCompleted = completedPersistenceService.getCompletedByObjectiveId(10L);

        assertNotNull(savedCompleted.getId());
        assertEquals(savedCompleted.getComment(), "Schade");
        assertEquals(savedCompleted.getObjective().getTitle(),
                "should not appear on staging, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    }

    @Test
    void deleteCompletedId_ShouldDeleteExistingCompletedByObjectiveId() {

        completedPersistenceService.deleteById(3L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> completedPersistenceService.findById(3L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Completed with id %d not found", 3), exception.getReason());
    }

    @Test
    void deleteCompleted_ShouldThrowExceptionWhenCompletedNotFound() {
        Completed newCompleted = completedPersistenceService.save(successfulCompleted);
        completedPersistenceService.deleteById(newCompleted.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> completedPersistenceService.findById(newCompleted.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Completed with id %d not found", newCompleted.getId()), exception.getReason());
    }
}
