package ch.puzzle.okr.service.business;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.dto.alignment.AlignmentLists;
import ch.puzzle.okr.dto.alignment.AlignedEntityDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.AlignmentView;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import ch.puzzle.okr.service.persistence.AlignmentViewPersistenceService;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.AlignmentValidationService;
import org.assertj.core.api.Assertions;
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
    AlignmentViewPersistenceService alignmentViewPersistenceService;
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

    AlignmentView alignmentView1 = AlignmentView.Builder.builder().withUniqueId("45TkeyResultkeyResult").withId(4L)
            .withTitle("Antwortzeit für Supportanfragen um 33% verkürzen.").withTeamId(5L).withTeamName("Puzzle ITC")
            .withQuarterId(2L).withObjectType("keyResult").withConnectionItem("target").withRefId(5L)
            .withRefType("objective").build();
    AlignmentView alignmentView2 = AlignmentView.Builder.builder().withUniqueId("54SobjectivekeyResult").withId(5L)
            .withTitle("Wir wollen das leiseste Team bei Puzzle sein.").withTeamId(4L).withTeamName("/BBT")
            .withQuarterId(2L).withObjectType("objective").withConnectionItem("source").withRefId(4L)
            .withRefType("keyResult").build();
    AlignmentView alignmentView3 = AlignmentView.Builder.builder().withUniqueId("4041Tobjectiveobjective").withId(40L)
            .withTitle("Wir wollen eine gute Mitarbeiterzufriedenheit.").withTeamId(6L).withTeamName("LoremIpsum")
            .withQuarterId(2L).withObjectType("objective").withConnectionItem("target").withRefId(41L)
            .withRefType("objective").build();
    AlignmentView alignmentView4 = AlignmentView.Builder.builder().withUniqueId("4140Sobjectiveobjective").withId(41L)
            .withTitle("Das Projekt generiert 10000 CHF Umsatz").withTeamId(6L).withTeamName("LoremIpsum")
            .withQuarterId(2L).withObjectType("objective").withConnectionItem("source").withRefId(40L)
            .withRefType("objective").build();

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

    @Test
    void shouldReturnCorrectAlignmentData() {
        doNothing().when(validator).validateOnAlignmentGet(anyLong(), anyList());
        when(alignmentViewPersistenceService.getAlignmentViewListByQuarterId(2L))
                .thenReturn(List.of(alignmentView1, alignmentView2));

        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(2L, List.of(4L), "");

        verify(alignmentViewPersistenceService, times(1)).getAlignmentViewListByQuarterId(2L);
        assertEquals(1, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(2, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(5L, alignmentLists.alignmentObjectDtoList().get(0).objectId());
        assertEquals(4L, alignmentLists.alignmentObjectDtoList().get(1).objectId());
        assertEquals(5L, alignmentLists.alignmentConnectionDtoList().get(0).alignedObjectiveId());
        assertEquals(4L, alignmentLists.alignmentConnectionDtoList().get(0).targetKeyResultId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(0).targetObjectiveId());
    }

    @Test
    void shouldReturnCorrectAlignmentDataWithMultipleTeamFilter() {
        doNothing().when(validator).validateOnAlignmentGet(anyLong(), anyList());
        when(alignmentViewPersistenceService.getAlignmentViewListByQuarterId(2L))
                .thenReturn(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4));

        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(2L, List.of(4L, 6L), "");

        verify(alignmentViewPersistenceService, times(1)).getAlignmentViewListByQuarterId(2L);
        assertEquals(2, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(4, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(5L, alignmentLists.alignmentObjectDtoList().get(0).objectId());
        assertEquals(40L, alignmentLists.alignmentObjectDtoList().get(1).objectId());
        assertEquals(5L, alignmentLists.alignmentConnectionDtoList().get(0).alignedObjectiveId());
        assertEquals(4L, alignmentLists.alignmentConnectionDtoList().get(0).targetKeyResultId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(0).targetObjectiveId());
        assertEquals(41L, alignmentLists.alignmentConnectionDtoList().get(1).alignedObjectiveId());
        assertEquals(40L, alignmentLists.alignmentConnectionDtoList().get(1).targetObjectiveId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(1).targetKeyResultId());
    }

    @Test
    void shouldReturnCorrectAlignmentDataWhenTeamFilterHasLimitedMatch() {
        doNothing().when(validator).validateOnAlignmentGet(anyLong(), anyList());
        when(alignmentViewPersistenceService.getAlignmentViewListByQuarterId(2L))
                .thenReturn(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4));

        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(2L, List.of(4L), "");

        verify(alignmentViewPersistenceService, times(1)).getAlignmentViewListByQuarterId(2L);
        assertEquals(1, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(2, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(5L, alignmentLists.alignmentObjectDtoList().get(0).objectId());
        assertEquals(4L, alignmentLists.alignmentObjectDtoList().get(1).objectId());
        assertEquals(5L, alignmentLists.alignmentConnectionDtoList().get(0).alignedObjectiveId());
        assertEquals(4L, alignmentLists.alignmentConnectionDtoList().get(0).targetKeyResultId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(0).targetObjectiveId());
    }

    @Test
    void shouldReturnEmptyAlignmentDataWhenNoMatchingTeam() {
        doNothing().when(validator).validateOnAlignmentGet(anyLong(), anyList());
        when(alignmentViewPersistenceService.getAlignmentViewListByQuarterId(2L))
                .thenReturn(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4));

        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(2L, List.of(12L), "");

        verify(alignmentViewPersistenceService, times(1)).getAlignmentViewListByQuarterId(2L);
        assertEquals(0, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(0, alignmentLists.alignmentObjectDtoList().size());
    }

    @Test
    void shouldReturnCorrectAlignmentDataWithObjectiveSearch() {
        doNothing().when(validator).validateOnAlignmentGet(anyLong(), anyList());
        when(alignmentViewPersistenceService.getAlignmentViewListByQuarterId(2L))
                .thenReturn(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4));

        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(2L, List.of(4L, 5L, 6L),
                "leise");

        verify(alignmentViewPersistenceService, times(1)).getAlignmentViewListByQuarterId(2L);
        assertEquals(1, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(2, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(5L, alignmentLists.alignmentObjectDtoList().get(0).objectId());
        assertEquals(4L, alignmentLists.alignmentObjectDtoList().get(1).objectId());
        assertEquals(5L, alignmentLists.alignmentConnectionDtoList().get(0).alignedObjectiveId());
        assertEquals(4L, alignmentLists.alignmentConnectionDtoList().get(0).targetKeyResultId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(0).targetObjectiveId());
    }

    @Test
    void shouldReturnEmptyAlignmentDataWhenNoMatchingObjectiveFromObjectiveSearch() {
        doNothing().when(validator).validateOnAlignmentGet(anyLong(), anyList());
        when(alignmentViewPersistenceService.getAlignmentViewListByQuarterId(2L))
                .thenReturn(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4));

        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(2L, List.of(4L, 5L, 6L),
                "Supportanfragen");

        verify(alignmentViewPersistenceService, times(1)).getAlignmentViewListByQuarterId(2L);
        assertEquals(0, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(0, alignmentLists.alignmentObjectDtoList().size());
    }

    @Test
    void shouldReturnEmptyAlignmentDataWhenNoMatchingObjectiveSearch() {
        doNothing().when(validator).validateOnAlignmentGet(anyLong(), anyList());
        when(alignmentViewPersistenceService.getAlignmentViewListByQuarterId(2L))
                .thenReturn(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4));

        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(2L, List.of(4L, 5L, 6L),
                "wird nicht vorkommen");

        verify(alignmentViewPersistenceService, times(1)).getAlignmentViewListByQuarterId(2L);
        assertEquals(0, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(0, alignmentLists.alignmentObjectDtoList().size());
    }

    @Test
    void shouldReturnEmptyAlignmentDataWhenNoAlignmentViews() {
        doNothing().when(validator).validateOnAlignmentGet(anyLong(), anyList());
        when(alignmentViewPersistenceService.getAlignmentViewListByQuarterId(2L)).thenReturn(List.of());

        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(2L, List.of(4L, 5L, 6L), "");

        verify(alignmentViewPersistenceService, times(1)).getAlignmentViewListByQuarterId(2L);
        assertEquals(0, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(0, alignmentLists.alignmentObjectDtoList().size());
    }

    @Test
    void shouldReturnCorrectAlignmentListsWithComplexAlignments() {
        AlignmentView alignmentView1 = AlignmentView.Builder.builder().withUniqueId("36TkeyResultkeyResult").withId(3L)
                .withTitle("Steigern der URS um 25%").withTeamId(5L).withTeamName("Puzzle ITC").withQuarterId(2L)
                .withObjectType("keyResult").withConnectionItem("target").withRefId(6L).withRefType("objective")
                .build();
        AlignmentView alignmentView2 = AlignmentView.Builder.builder().withUniqueId("63SobjectivekeyResult").withId(6L)
                .withTitle("Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.")
                .withTeamId(4L).withTeamName("/BBT").withQuarterId(2L).withObjectType("objective")
                .withConnectionItem("source").withRefId(3L).withRefType("keyResult").build();
        AlignmentView alignmentView3 = AlignmentView.Builder.builder().withUniqueId("63Tobjectiveobjective").withId(6L)
                .withTitle("Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.")
                .withTeamId(4L).withTeamName("/BBT").withQuarterId(2L).withObjectType("objective")
                .withConnectionItem("target").withRefId(3L).withRefType("objective").build();
        AlignmentView alignmentView4 = AlignmentView.Builder.builder().withUniqueId("36Sobjectiveobjective").withId(3L)
                .withTitle("Wir wollen die Kundenzufriedenheit steigern").withTeamId(4L).withTeamName("/BBT")
                .withQuarterId(2L).withObjectType("objective").withConnectionItem("source").withRefId(6L)
                .withRefType("objective").build();

        doNothing().when(validator).validateOnAlignmentGet(anyLong(), anyList());
        when(alignmentViewPersistenceService.getAlignmentViewListByQuarterId(2L))
                .thenReturn(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4));

        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(2L, List.of(5L), "");

        verify(alignmentViewPersistenceService, times(1)).getAlignmentViewListByQuarterId(2L);
        assertEquals(1, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(2, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(3L, alignmentLists.alignmentObjectDtoList().get(0).objectId());
        assertEquals("keyResult", alignmentLists.alignmentObjectDtoList().get(0).objectType());
        assertEquals(6L, alignmentLists.alignmentObjectDtoList().get(1).objectId());
        assertEquals("objective", alignmentLists.alignmentObjectDtoList().get(1).objectType());
        assertEquals(6L, alignmentLists.alignmentConnectionDtoList().get(0).alignedObjectiveId());
        assertEquals(3L, alignmentLists.alignmentConnectionDtoList().get(0).targetKeyResultId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(0).targetObjectiveId());
    }

    @Test
    void shouldCorrectFilterAlignmentViewListsWithAllCorrectData() {
        AlignmentBusinessService.DividedAlignmentViewLists dividedAlignmentViewLists = alignmentBusinessService
                .filterAlignmentViews(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4),
                        List.of(4L, 6L, 5L), "");

        assertEquals(4, dividedAlignmentViewLists.correctAlignments().size());
        assertEquals(0, dividedAlignmentViewLists.wrongAlignments().size());
        assertEquals(4, dividedAlignmentViewLists.correctAlignments().get(0).getId());
        assertEquals(5, dividedAlignmentViewLists.correctAlignments().get(1).getId());
        assertEquals(40, dividedAlignmentViewLists.correctAlignments().get(2).getId());
        assertEquals(41, dividedAlignmentViewLists.correctAlignments().get(3).getId());
    }

    @Test
    void shouldCorrectFilterAlignmentViewListsWithLimitedTeamFilter() {
        AlignmentBusinessService.DividedAlignmentViewLists dividedAlignmentViewLists = alignmentBusinessService
                .filterAlignmentViews(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4),
                        List.of(6L), "");

        assertEquals(2, dividedAlignmentViewLists.correctAlignments().size());
        assertEquals(2, dividedAlignmentViewLists.wrongAlignments().size());
        assertEquals(40, dividedAlignmentViewLists.correctAlignments().get(0).getId());
        assertEquals(41, dividedAlignmentViewLists.correctAlignments().get(1).getId());
        assertEquals(4, dividedAlignmentViewLists.wrongAlignments().get(0).getId());
        assertEquals(5, dividedAlignmentViewLists.wrongAlignments().get(1).getId());
    }

    @Test
    void shouldCorrectFilterAlignmentViewListsWithObjectiveSearch() {
        AlignmentBusinessService.DividedAlignmentViewLists dividedAlignmentViewLists = alignmentBusinessService
                .filterAlignmentViews(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4),
                        List.of(4L, 6L, 5L), "leise");

        assertEquals(1, dividedAlignmentViewLists.correctAlignments().size());
        assertEquals(3, dividedAlignmentViewLists.wrongAlignments().size());
        assertEquals(5, dividedAlignmentViewLists.correctAlignments().get(0).getId());
        assertEquals(4, dividedAlignmentViewLists.wrongAlignments().get(0).getId());
        assertEquals(40, dividedAlignmentViewLists.wrongAlignments().get(1).getId());
        assertEquals(41, dividedAlignmentViewLists.wrongAlignments().get(2).getId());
    }

    @Test
    void shouldCorrectFilterWhenNoMatchingObjectiveSearch() {
        AlignmentBusinessService.DividedAlignmentViewLists dividedAlignmentViewLists = alignmentBusinessService
                .filterAlignmentViews(List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4),
                        List.of(4L, 6L, 5L), "verk");

        assertEquals(0, dividedAlignmentViewLists.correctAlignments().size());
        assertEquals(4, dividedAlignmentViewLists.wrongAlignments().size());
        assertEquals(4, dividedAlignmentViewLists.wrongAlignments().get(0).getId());
        assertEquals(5, dividedAlignmentViewLists.wrongAlignments().get(1).getId());
        assertEquals(40, dividedAlignmentViewLists.wrongAlignments().get(2).getId());
        assertEquals(41, dividedAlignmentViewLists.wrongAlignments().get(3).getId());
    }

    @Test
    void shouldThrowErrorWhenPersistenceServiceReturnsIncorrectData() {
        AlignmentView alignmentView5 = AlignmentView.Builder.builder().withUniqueId("23TkeyResultkeyResult").withId(20L)
                .withTitle("Dies hat kein Gegenstück").withTeamId(5L).withTeamName("Puzzle ITC").withQuarterId(2L)
                .withObjectType("keyResult").withConnectionItem("target").withRefId(37L).withRefType("objective")
                .build();
        List<AlignmentView> finalList = List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4,
                alignmentView5);

        doNothing().when(validator).validateOnAlignmentGet(anyLong(), anyList());
        when(alignmentViewPersistenceService.getAlignmentViewListByQuarterId(2L)).thenReturn(finalList);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> alignmentBusinessService.getAlignmentsByFilters(2L, List.of(5L), ""));

        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ALIGNMENT_DATA_FAIL", List.of("alignmentData", "2", "[5]", "")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void shouldNotThrowErrorWhenSameAmountOfSourceAndTarget() {
        List<AlignmentView> finalList = List.of(alignmentView1, alignmentView2, alignmentView3, alignmentView4);

        assertDoesNotThrow(() -> alignmentBusinessService.validateFinalList(finalList, 2L, List.of(5L), ""));
    }

    @Test
    void shouldThrowErrorWhenNotSameAmountOfSourceAndTarget() {
        List<AlignmentView> finalList = List.of(alignmentView1, alignmentView2, alignmentView3);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> alignmentBusinessService.validateFinalList(finalList, 2L, List.of(5L), ""));

        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ALIGNMENT_DATA_FAIL", List.of("alignmentData", "2", "[5]", "")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }
}
