package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.test.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.models.State.DRAFT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@SpringIntegrationTest
class AlignmentPersistenceServiceIT {
    @Autowired
    private AlignmentPersistenceService alignmentPersistenceService;
    private Alignment createdAlignment;
    private final String ALIGNMENT = "Alignment";

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

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
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
        TenantContext.setCurrentTenant(null);
    }

    @Test
    void saveAlignmentShouldSaveNewObjectiveAlignment() {
        // arrange
        Alignment alignment = createObjectiveAlignment(null);

        // act
        createdAlignment = alignmentPersistenceService.save(alignment);

        // assert
        assertNotNull(createdAlignment.getId());
        assertEquals(5L, createdAlignment.getAlignedObjective().getId());
        assertEquals(4L, ((ObjectiveAlignment) createdAlignment).getAlignmentTarget().getId());
    }

    @Test
    void saveAlignmentShouldSaveNewKeyResultAlignment() {
        // arrange
        Alignment alignment = createKeyResultAlignment(null);

        // act
        createdAlignment = alignmentPersistenceService.save(alignment);

        // assert
        assertNotNull(createdAlignment.getId());
        assertEquals(5L, createdAlignment.getAlignedObjective().getId());
        assertEquals(8L, ((KeyResultAlignment) createdAlignment).getAlignmentTarget().getId());
    }

    @Test
    void updateAlignmentShouldSaveKeyResultAlignment() {
        // arrange
        createdAlignment = alignmentPersistenceService.save(createKeyResultAlignment(null));
        Alignment updateAlignment = createKeyResultAlignment(createdAlignment.getId(), createdAlignment.getVersion());
        updateAlignment.setAlignedObjective(Objective.Builder.builder().withId(8L).build());

        // act
        Alignment updatedAlignment = alignmentPersistenceService.save(updateAlignment);

        // assert
        assertEquals(createdAlignment.getId(), updatedAlignment.getId());
        assertEquals(createdAlignment.getVersion() + 1, updatedAlignment.getVersion());
    }

    @Test
    void updateAlignmentShouldThrowExceptionWhenAlreadyUpdated() {
        // arrange
        createdAlignment = alignmentPersistenceService.save(createKeyResultAlignment(null));
        Alignment updateAlignment = createKeyResultAlignment(createdAlignment.getId(), 0);
        updateAlignment.setAlignedObjective(Objective.Builder.builder().withId(8L).build());
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("DATA_HAS_BEEN_UPDATED", List.of(ALIGNMENT)));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> alignmentPersistenceService.save(updateAlignment));

        // assert
        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void findByAlignedObjectiveIdShouldReturnAlignmentModel() {
        // act
        Alignment alignment = alignmentPersistenceService.findByAlignedObjectiveId(4L);

        // assert
        assertNotNull(alignment);
        assertEquals(4, alignment.getAlignedObjective().getId());
    }

    @Test
    void findByKeyResultAlignmentIdShouldReturnListOfAlignments() {
        // act
        List<KeyResultAlignment> alignments = alignmentPersistenceService.findByKeyResultAlignmentId(8L);

        // assert
        assertEquals(1, alignments.size());
        assertAlignment(alignments.get(0));
    }

    @Test
    void findByObjectiveAlignmentIdShouldReturnListOfAlignments() {
        // act
        List<ObjectiveAlignment> alignments = alignmentPersistenceService.findByObjectiveAlignmentId(3L);

        // assert
        assertEquals(1, alignments.size());
        assertAlignment(alignments.get(0));
    }

    @Test
    void recreateEntityShouldUpdateAlignmentNoTypeChange() {
        // arrange
        Objective objective1 = Objective.Builder.builder().withId(5L).withTitle("Objective 1").withState(DRAFT).build();
        Objective objective2 = Objective.Builder.builder().withId(8L).withTitle("Objective 2").withState(DRAFT).build();
        Objective objective3 = Objective.Builder.builder().withId(4L)
                .withTitle("Build a company culture that kills the competition.").build();
        ObjectiveAlignment objectiveALignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective1)
                .withTargetObjective(objective2).build();
        createdAlignment = alignmentPersistenceService.save(objectiveALignment);
        ObjectiveAlignment createObjectiveAlignment = (ObjectiveAlignment) createdAlignment;
        createObjectiveAlignment.setAlignmentTarget(objective3);
        Long alignmentId = createObjectiveAlignment.getId();

        // act
        Alignment recreatedAlignment = alignmentPersistenceService.recreateEntity(createdAlignment.getId(),
                createObjectiveAlignment);
        createObjectiveAlignment = (ObjectiveAlignment) recreatedAlignment;

        // assert
        assertNotNull(recreatedAlignment.getId());
        assertEquals(4L, createObjectiveAlignment.getAlignmentTarget().getId());
        assertEquals("Build a company culture that kills the competition.",
                createObjectiveAlignment.getAlignmentTarget().getTitle());
        shouldDeleteOldAlignment(alignmentId);

        // delete re-created Alignment in tearDown()
        createdAlignment = createObjectiveAlignment;
    }

    @Test
    void recreateEntityShouldUpdateAlignmentWithTypeChange() {
        // arrange
        Objective objective1 = Objective.Builder.builder().withId(5L).withTitle("Objective 1").withState(DRAFT).build();
        Objective objective2 = Objective.Builder.builder().withId(8L).withTitle("Objective 2").withState(DRAFT).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withId(10L)
                .withTitle("Im Durchschnitt soll die Lautstärke 60dB nicht überschreiten").build();
        ObjectiveAlignment objectiveALignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective1)
                .withTargetObjective(objective2).build();
        createdAlignment = alignmentPersistenceService.save(objectiveALignment);
        KeyResultAlignment keyResultAlignment = KeyResultAlignment.Builder.builder().withId(createdAlignment.getId())
                .withAlignedObjective(objective1).withTargetKeyResult(keyResult).build();
        Long alignmentId = createdAlignment.getId();

        // act
        Alignment recreatedAlignment = alignmentPersistenceService.recreateEntity(keyResultAlignment.getId(),
                keyResultAlignment);
        KeyResultAlignment returnedKeyResultAlignment = (KeyResultAlignment) recreatedAlignment;

        // assert
        assertNotNull(recreatedAlignment.getId());
        assertEquals(createdAlignment.getAlignedObjective().getId(), recreatedAlignment.getAlignedObjective().getId());
        assertEquals("Im Durchschnitt soll die Lautstärke 60dB nicht überschreiten",
                returnedKeyResultAlignment.getAlignmentTarget().getTitle());
        shouldDeleteOldAlignment(alignmentId);

        // delete re-created Alignment in tearDown()
        createdAlignment = returnedKeyResultAlignment;
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

    private void shouldDeleteOldAlignment(Long alignmentId) {
        // Should delete the old Alignment
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> alignmentPersistenceService.findById(alignmentId));

        List<ErrorDto> expectedErrors = List
                .of(ErrorDto.of("MODEL_WITH_ID_NOT_FOUND", List.of(ALIGNMENT, alignmentId)));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    private void assertAlignment(ObjectiveAlignment objectiveAlignment) {
        assertEquals(1L, objectiveAlignment.getId());
        assertEquals(3L, objectiveAlignment.getAlignmentTarget().getId());
        assertEquals(4L, objectiveAlignment.getAlignedObjective().getId());
    }

    private void assertAlignment(KeyResultAlignment keyResultAlignment) {
        assertEquals(2L, keyResultAlignment.getId());
        assertEquals(8L, keyResultAlignment.getAlignmentTarget().getId());
        assertEquals(9L, keyResultAlignment.getAlignedObjective().getId());
    }
}
