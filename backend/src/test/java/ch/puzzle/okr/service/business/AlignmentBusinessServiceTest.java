package ch.puzzle.okr.service.business;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.dto.alignment.AlignedEntityDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.AlignmentValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ch.puzzle.okr.models.State.DRAFT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class AlignmentBusinessServiceTest {
    @Mock
    ObjectivePersistenceService objectivePersistenceService;
    @Mock
    KeyResultPersistenceService keyResultPersistenceService;
    @Mock
    AlignmentPersistenceService alignmentPersistenceService;
    @Mock
    AlignmentValidationService validator;
    @InjectMocks
    private AlignmentBusinessService alignmentBusinessService;

    Objective objective1 = Objective.Builder.builder().withId(5L).withTitle("Objective 1").withState(DRAFT).build();
    Objective objective2 = Objective.Builder.builder().withId(8L).withTitle("Objective 2").withState(DRAFT).build();
    Objective objective3 = Objective.Builder.builder().withId(10L).withTitle("Objective 3").withState(DRAFT).build();
    AlignedEntityDto alignedEntityDtoObjective = new AlignedEntityDto(8L, "objective");
    AlignedEntityDto alignedEntityDtoKeyResult = new AlignedEntityDto(5L, "keyResult");
    Objective objectiveAlignedObjective = Objective.Builder.builder().withId(42L).withTitle("Objective 42")
            .withState(DRAFT).withAlignedEntity(alignedEntityDtoObjective).build();
    Objective keyResultAlignedObjective = Objective.Builder.builder().withId(45L).withTitle("Objective 45")
            .withState(DRAFT).withAlignedEntity(alignedEntityDtoKeyResult).build();
    Objective wrongAlignedObjective = Objective.Builder.builder().withId(48L).withTitle("Objective 48").withState(DRAFT)
            .withAlignedEntity(new AlignedEntityDto(0L, "Hello")).build();
    KeyResult metricKeyResult = KeyResultMetric.Builder.builder().withId(5L).withTitle("KR Title 1").build();
    ObjectiveAlignment objectiveALignment = ObjectiveAlignment.Builder.builder().withId(1L)
            .withAlignedObjective(objective1).withTargetObjective(objective2).build();
    ObjectiveAlignment objectiveAlignment2 = ObjectiveAlignment.Builder.builder().withId(2L)
            .withAlignedObjective(objective2).withTargetObjective(objective1).build();
    KeyResultAlignment keyResultAlignment = KeyResultAlignment.Builder.builder().withId(6L)
            .withAlignedObjective(objective3).withTargetKeyResult(metricKeyResult).build();

    @Test
    void shouldGetTargetAlignmentIdObjective() {
        // arrange
        when(alignmentPersistenceService.findByAlignedObjectiveId(5L)).thenReturn(objectiveALignment);

        // act
        AlignedEntityDto alignedEntity = alignmentBusinessService.getTargetIdByAlignedObjectiveId(5L);

        // assert
        assertEquals(alignedEntityDtoObjective, alignedEntity);
    }

    @Test
    void shouldReturnNullWhenNoAlignmentFound() {
        // arrange
        when(alignmentPersistenceService.findByAlignedObjectiveId(5L)).thenReturn(null);

        // act
        AlignedEntityDto alignedEntity = alignmentBusinessService.getTargetIdByAlignedObjectiveId(5L);

        // assert
        verify(validator, times(1)).validateOnGet(5L);
        assertNull(alignedEntity);
    }

    @Test
    void shouldGetTargetAlignmentIdKeyResult() {
        // arrange
        when(alignmentPersistenceService.findByAlignedObjectiveId(5L)).thenReturn(keyResultAlignment);

        // act
        AlignedEntityDto alignedEntity = alignmentBusinessService.getTargetIdByAlignedObjectiveId(5L);

        // assert
        assertEquals(alignedEntityDtoKeyResult, alignedEntity);
    }

    @Test
    void shouldCreateNewAlignment() {
        // arrange
        when(objectivePersistenceService.findById(8L)).thenReturn(objective1);
        Alignment returnAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objectiveAlignedObjective)
                .withTargetObjective(objective1).build();

        // act
        alignmentBusinessService.createEntity(objectiveAlignedObjective);

        // assert
        verify(alignmentPersistenceService, times(1)).save(returnAlignment);
    }

    @Test
    void shouldUpdateEntityNewAlignment() {
        // arrange
        when(alignmentPersistenceService.findByAlignedObjectiveId(8L)).thenReturn(null);
        when(objectivePersistenceService.findById(8L)).thenReturn(objective1);
        Alignment returnAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objectiveAlignedObjective)
                .withTargetObjective(objective1).build();

        // act
        alignmentBusinessService.updateEntity(8L, objectiveAlignedObjective);

        // assert
        verify(alignmentPersistenceService, times(1)).save(returnAlignment);
    }

    @Test
    void shouldUpdateEntityDeleteAlignment() {
        // arrange
        when(alignmentPersistenceService.findByAlignedObjectiveId(8L)).thenReturn(objectiveAlignment2);

        // act
        alignmentBusinessService.updateEntity(8L, objective3);

        // assert
        verify(alignmentPersistenceService, times(1)).deleteById(2L);
    }

    @Test
    void shouldUpdateEntityChangeTargetId() {
        // arrange
        when(alignmentPersistenceService.findByAlignedObjectiveId(8L)).thenReturn(objectiveAlignment2);
        when(objectivePersistenceService.findById(8L)).thenReturn(objective1);
        Alignment returnAlignment = ObjectiveAlignment.Builder.builder().withId(2L).withAlignedObjective(objectiveAlignedObjective)
                .withTargetObjective(objective1).build();

        // act
        alignmentBusinessService.updateEntity(8L, objectiveAlignedObjective);

        // assert
        verify(alignmentPersistenceService, times(1)).save(returnAlignment);
    }

    @Test
    void shouldUpdateEntityChangeObjectiveToKeyResult() {
        // arrange
        when(alignmentPersistenceService.findByAlignedObjectiveId(8L)).thenReturn(objectiveAlignment2);
        when(keyResultPersistenceService.findById(5L)).thenReturn(metricKeyResult);
        Alignment returnAlignment = KeyResultAlignment.Builder.builder().withId(2L).withAlignedObjective(keyResultAlignedObjective)
                .withTargetKeyResult(metricKeyResult).build();

        // act
        alignmentBusinessService.updateEntity(8L, keyResultAlignedObjective);

        // assert
        verify(alignmentPersistenceService, times(0)).save(returnAlignment);
        verify(alignmentPersistenceService, times(1)).recreateEntity(2L, returnAlignment);
    }

    @Test
    void shouldBuildAlignmentCorrectObjective() {
        // arrange
        when(objectivePersistenceService.findById(8L)).thenReturn(objective1);
        Alignment returnAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objectiveAlignedObjective)
                .withTargetObjective(objective1).build();

        // act
        Alignment alignment = alignmentBusinessService.buildAlignmentModel(objectiveAlignedObjective, 0);

        // assert
        assertEquals(returnAlignment, alignment);
        assertInstanceOf(ObjectiveAlignment.class, alignment);
    }

    @Test
    void shouldBuildAlignmentCorrectKeyResult() {
        // arrange
        when(keyResultPersistenceService.findById(5L)).thenReturn(metricKeyResult);
        Alignment returnAlignment = KeyResultAlignment.Builder.builder().withAlignedObjective(keyResultAlignedObjective)
                .withTargetKeyResult(metricKeyResult).build();

        // act
        Alignment alignment = alignmentBusinessService.buildAlignmentModel(keyResultAlignedObjective, 0);

        // assert
        assertEquals(returnAlignment, alignment);
        assertInstanceOf(KeyResultAlignment.class, alignment);
    }

    @Test
    void shouldThrowErrorWhenAlignedEntityIsIncorrect() {
        // arrange
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_SET",
                List.of("alignedEntity", new AlignedEntityDto(0L, "Hello").toString())));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> alignmentBusinessService.buildAlignmentModel(wrongAlignedObjective, 0));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void shouldReturnCorrectIsAlignmentTypeChange() {
        assertTrue(alignmentBusinessService.isAlignmentTypeChange(keyResultAlignment, objectiveALignment));
        assertTrue(alignmentBusinessService.isAlignmentTypeChange(objectiveALignment, keyResultAlignment));
        assertFalse(alignmentBusinessService.isAlignmentTypeChange(objectiveALignment, objectiveALignment));
        assertFalse(alignmentBusinessService.isAlignmentTypeChange(keyResultAlignment, keyResultAlignment));
    }

    @Test
    void shouldUpdateKeyResultIdOnChange() {
        // arrange
        when(alignmentPersistenceService.findByKeyResultAlignmentId(1L)).thenReturn(List.of(keyResultAlignment));

        // act
        alignmentBusinessService.updateKeyResultIdOnIdChange(1L, metricKeyResult);
        keyResultAlignment.setAlignmentTarget(metricKeyResult);

        // assert
        verify(alignmentPersistenceService, times(1)).save(keyResultAlignment);
    }

    @Test
    void shouldDeleteByObjectiveId() {
        // arrange
        when(alignmentPersistenceService.findByAlignedObjectiveId(5L)).thenReturn(objectiveALignment);
        when(alignmentPersistenceService.findByObjectiveAlignmentId(5L)).thenReturn(List.of(objectiveAlignment2));

        // act
        alignmentBusinessService.deleteAlignmentByObjectiveId(5L);

        // assert
        verify(alignmentPersistenceService, times(1)).deleteById(objectiveALignment.getId());
        verify(alignmentPersistenceService, times(1)).deleteById(objectiveAlignment2.getId());
    }

    @Test
    void shouldDeleteByKeyResultId() {
        // arrange
        when(alignmentPersistenceService.findByKeyResultAlignmentId(5L)).thenReturn(List.of(keyResultAlignment));

        // act
        alignmentBusinessService.deleteAlignmentByKeyResultId(5L);

        // assert
        verify(alignmentPersistenceService, times(1)).deleteById(keyResultAlignment.getId());
    }
}
