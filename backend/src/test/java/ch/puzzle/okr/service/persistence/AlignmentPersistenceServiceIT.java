package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.OkrResponseStatusException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@SpringIntegrationTest
class AlignmentPersistenceServiceIT {
    @Autowired
    private AlignmentPersistenceService alignmentPersistenceService;
    private Alignment createdAlignment;

    private static ObjectiveAlignment createObjectiveAlignment(Long id) {
        return ObjectiveAlignment.Builder.builder().withId(id)
                .withAlignedObjective(Objective.Builder.builder().withId(5L).build())
                .withTargetObjective(Objective.Builder.builder().withId(4L).build()).build();
    }

    private static KeyResultAlignment createKeyResultAlignment(Long id) {
        return createKeyResultAlignment(id, 1);
    }

    private static KeyResultAlignment createKeyResultAlignment(Long id, int version) {
        return KeyResultAlignment.Builder.builder().withId(id).withVersion(version)
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
            // created alignment already deleted
        } finally {
            createdAlignment = null;
        }
    }

    @Test
    void saveAlignmentShouldSaveNewObjectiveAlignment() {
        Alignment alignment = createObjectiveAlignment(null);

        createdAlignment = alignmentPersistenceService.save(alignment);

        assertNotNull(createdAlignment.getId());
        assertEquals(5L, createdAlignment.getAlignedObjective().getId());
        assertEquals(4L, ((ObjectiveAlignment) createdAlignment).getAlignmentTarget().getId());
    }

    @Test
    void saveAlignmentShouldSaveNewKeyResultAlignment() {
        Alignment alignment = createKeyResultAlignment(null);

        createdAlignment = alignmentPersistenceService.save(alignment);

        assertNotNull(createdAlignment.getId());
        assertEquals(5L, createdAlignment.getAlignedObjective().getId());
        assertEquals(8L, ((KeyResultAlignment) createdAlignment).getAlignmentTarget().getId());
    }

    @Test
    void updateAlignmentShouldSaveKeyResultAlignment() {
        createdAlignment = alignmentPersistenceService.save(createKeyResultAlignment(null));
        Alignment updateAlignment = createKeyResultAlignment(createdAlignment.getId(), createdAlignment.getVersion());
        updateAlignment.setAlignedObjective(Objective.Builder.builder().withId(8L).build());

        Alignment updatedAlignment = alignmentPersistenceService.save(updateAlignment);

        assertEquals(createdAlignment.getId(), updatedAlignment.getId());
        assertEquals(createdAlignment.getVersion() + 1, updatedAlignment.getVersion());
    }

    @Test
    void updateAlignmentShouldThrowExceptionWhenAlreadyUpdated() {
        createdAlignment = alignmentPersistenceService.save(createKeyResultAlignment(null));
        Alignment updateAlignment = createKeyResultAlignment(createdAlignment.getId(), 0);
        updateAlignment.setAlignedObjective(Objective.Builder.builder().withId(8L).build());

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> alignmentPersistenceService.save(updateAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("DATA_HAS_BEEN_UPDATED", List.of("Alignment")));

        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void findByAlignedObjectiveIdShouldReturnListOfAlignments() {
        List<Alignment> alignments = alignmentPersistenceService.findByAlignedObjectiveId(4L);

        assertEquals(2, alignments.size());
        alignments.forEach(this::assertAlignment);
    }

    @Test
    void findByKeyResultAlignmentIdShouldReturnListOfAlignments() {
        List<KeyResultAlignment> alignments = alignmentPersistenceService.findByKeyResultAlignmentId(8L);

        assertEquals(1, alignments.size());
        assertAlignment(alignments.get(0));
    }

    @Test
    void findByObjectiveAlignmentIdShouldReturnListOfAlignments() {
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
