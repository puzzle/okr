package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringIntegrationTest
public class AlignmentPersistenceServiceIT {
    Alignment createdAlignment;
    @Autowired
    private AlignmentPersistenceService alignmentPersistenceService;

    private static ObjectiveAlignment createObjectiveAlignment(Long id) {
        return ObjectiveAlignment.Builder.builder().withId(id)
                .withAlignedObjective(Objective.Builder.builder().withId(5L).build())
                .withTargetObjective(Objective.Builder.builder().withId(4L).build()).build();
    }

    private static KeyResultAlignment createKeyResultAlignment(Long id) {
        return KeyResultAlignment.Builder.builder().withId(id)
                .withAlignedObjective(Objective.Builder.builder().withId(5L).build())
                .withTargetKeyResult(KeyResultMetric.Builder.builder().withId(8L).build()).build();
    }

    @AfterEach
    void tearDown() {
        try {
            if (createdAlignment != null) {
                alignmentPersistenceService.findById(createdAlignment.getId());
                alignmentPersistenceService.deleteById(createdAlignment.getId());
            }
        } catch (ResponseStatusException ex) {
            // created key result already deleted
        } finally {
            createdAlignment = null;
        }
    }

    @Test
    void save_ShouldSaveNewObjectiveAlignment() {
        Alignment alignment = createObjectiveAlignment(null);

        createdAlignment = alignmentPersistenceService.save(alignment);

        assertNotNull(createdAlignment.getId());
        assertEquals(5L, createdAlignment.getAlignedObjective().getId());
        assertEquals(4L, ((ObjectiveAlignment) createdAlignment).getAlignmentTarget().getId());
    }

    @Test
    void save_ShouldSaveNewKeyResultAlignment() {
        Alignment alignment = createKeyResultAlignment(null);

        createdAlignment = alignmentPersistenceService.save(alignment);

        assertNotNull(createdAlignment.getId());
        assertEquals(5L, createdAlignment.getAlignedObjective().getId());
        assertEquals(8L, ((KeyResultAlignment) createdAlignment).getAlignmentTarget().getId());
    }

    @Test
    void findByAlignedObjectiveId_shouldReturnListOfAlignments() {
        List<Alignment> alignments = alignmentPersistenceService.findByAlignedObjectiveId(4L);

        assertEquals(2, alignments.size());
        alignments.forEach(this::assertAlignment);
    }

    @Test
    void findByKeyResultAlignmentId_shouldReturnListOfAlignments() {
        List<KeyResultAlignment> alignments = alignmentPersistenceService.findByKeyResultAlignmentId(8L);

        assertEquals(1, alignments.size());
        assertAlignment(alignments.get(0));
    }

    @Test
    void findByObjectiveAlignmentId_shouldReturnListOfAlignments() {
        List<ObjectiveAlignment> alignments = alignmentPersistenceService.findByObjectiveAlignmentId(3L);

        assertEquals(1, alignments.size());
        assertAlignment(alignments.get(0));
    }

    private void assertAlignment(Alignment alignment) {
        if (alignment instanceof ObjectiveAlignment objectiveAlignment) {
            assertAlignment(objectiveAlignment);
        } else if (alignment instanceof KeyResultAlignment keyResultAlignment) {
            assertAlignment(keyResultAlignment);
        } else {
            throw new IllegalArgumentException("unsupported alignment type: " + alignment.getClass());
        }
    }

    private void assertAlignment(ObjectiveAlignment objectiveAlignment) {
        assertEquals(1L, objectiveAlignment.getId());
        assertEquals(3L, objectiveAlignment.getAlignmentTarget().getId());
        assertEquals(4L, objectiveAlignment.getAlignedObjective().getId());
    }

    private void assertAlignment(KeyResultAlignment keyResultAlignment) {
        assertEquals(2L, keyResultAlignment.getId());
        assertEquals(8L, keyResultAlignment.getAlignmentTarget().getId());
        assertEquals(4L, keyResultAlignment.getAlignedObjective().getId());
    }
}
