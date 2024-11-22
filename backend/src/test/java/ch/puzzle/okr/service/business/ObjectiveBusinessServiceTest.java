package ch.puzzle.okr.service.business;

import ch.puzzle.okr.dto.AlignmentDto;
import ch.puzzle.okr.dto.AlignmentObjectDto;
import ch.puzzle.okr.dto.alignment.AlignedEntityDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.ObjectiveValidationService;
import ch.puzzle.okr.test.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static ch.puzzle.okr.test.TestConstants.TEAM_PUZZLE;
import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.models.State.DRAFT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ObjectiveBusinessServiceTest {
    private static final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    @InjectMocks
    @Spy
    ObjectiveBusinessService objectiveBusinessService;
    @Mock
    ObjectivePersistenceService objectivePersistenceService;
    @Mock
    KeyResultBusinessService keyResultBusinessService;
    @Mock
    AlignmentBusinessService alignmentBusinessService;
    @Mock
    CompletedBusinessService completedBusinessService;
    @Mock
    ObjectiveValidationService validator = Mockito.mock(ObjectiveValidationService.class);

    private static final String TEAM_1 = "Team1";
    private static final String OBJECTIVE = "objective";
    private static final String FULL_OBJECTIVE_1 = "O - FullObjective1";
    private static final String FULL_OBJECTIVE_2 = "O - FullObjective2";

    private final Team team1 = Team.Builder.builder().withId(1L).withName(TEAM_1).build();
    private final Quarter quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
    private final User user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
            .withEmail("kaufmann@puzzle.ch").build();
    private final Objective objective1 = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    private final Objective fullObjectiveCreate = Objective.Builder.builder().withTitle("FullObjective1")
            .withCreatedBy(user).withTeam(team1).withQuarter(quarter).withDescription("This is our description")
            .withModifiedOn(LocalDateTime.MAX).build();
    private final Objective fullObjective1 = Objective.Builder.builder().withId(1L).withTitle("FullObjective1")
            .withCreatedBy(user).withTeam(team1).withQuarter(quarter).withDescription("This is our description")
            .withModifiedOn(LocalDateTime.MAX).build();
    private final Objective fullObjective2 = Objective.Builder.builder().withId(2L).withTitle("FullObjective2")
            .withCreatedBy(user).withTeam(team1).withQuarter(quarter).withDescription("This is our description")
            .withModifiedOn(LocalDateTime.MAX).build();
    private final Team team2 = Team.Builder.builder().withId(3L).withName(TEAM_PUZZLE).build();
    private final Objective fullObjective3 = Objective.Builder.builder().withId(3L).withTitle("FullObjective5")
            .withCreatedBy(user).withTeam(team2).withQuarter(quarter).withDescription("This is our description")
            .withModifiedOn(LocalDateTime.MAX).build();
    private final KeyResult ordinalKeyResult2 = KeyResultOrdinal.Builder.builder().withCommitZone("Baum")
            .withStretchZone("Wald").withId(6L).withTitle("Keyresult Ordinal 6").withObjective(fullObjective3).build();
    private final KeyResult ordinalKeyResult = KeyResultOrdinal.Builder.builder().withCommitZone("Baum")
            .withStretchZone("Wald").withId(5L).withTitle("Keyresult Ordinal").withObjective(objective1).build();
    private final List<KeyResult> keyResultList = List.of(ordinalKeyResult, ordinalKeyResult);
    private final List<Objective> objectiveList = List.of(fullObjective1, fullObjective2);
    private final AlignmentObjectDto alignmentObjectDto1 = new AlignmentObjectDto(1L, FULL_OBJECTIVE_1, OBJECTIVE);
    private final AlignmentObjectDto alignmentObjectDto2 = new AlignmentObjectDto(5L, "KR - Keyresult Ordinal",
            "keyResult");
    private final AlignmentObjectDto alignmentObjectDto3 = new AlignmentObjectDto(5L, "KR - Keyresult Ordinal",
            "keyResult");
    private final AlignmentObjectDto alignmentObjectDto4 = new AlignmentObjectDto(2L, FULL_OBJECTIVE_2, OBJECTIVE);
    private final AlignmentObjectDto alignmentObjectDto5 = new AlignmentObjectDto(5L, "KR - Keyresult Ordinal",
            "keyResult");
    private final AlignmentObjectDto alignmentObjectDto6 = new AlignmentObjectDto(5L, "KR - Keyresult Ordinal",
            "keyResult");
    private final AlignmentDto alignmentDto = new AlignmentDto(1L, TEAM_1, List.of(alignmentObjectDto1,
            alignmentObjectDto2, alignmentObjectDto3, alignmentObjectDto4, alignmentObjectDto5, alignmentObjectDto6));
    AlignedEntityDto alignedEntityDtoObjective = new AlignedEntityDto(53L, OBJECTIVE);

    @Test
    void getOneObjective() {
        // arrange
        when(objectivePersistenceService.findById(5L)).thenReturn(objective1);

        // act
        Objective realObjective = objectiveBusinessService.getEntityById(5L);

        // assert
        verify(alignmentBusinessService, times(1)).getTargetIdByAlignedObjectiveId(5L);
        assertEquals("Objective 1", realObjective.getTitle());
    }

    @Test
    void getEntitiesByTeamId() {
        // arrange
        when(objectivePersistenceService.findObjectiveByTeamId(anyLong())).thenReturn(List.of(objective1));

        // act
        List<Objective> entities = objectiveBusinessService.getEntitiesByTeamId(5L);

        // assert
        verify(alignmentBusinessService, times(1)).getTargetIdByAlignedObjectiveId(5L);
        assertThat(entities).hasSameElementsAs(List.of(objective1));
    }

    @Test
    void shouldNotFindTheObjective() {
        // arrange
        when(objectivePersistenceService.findById(6L))
                .thenThrow(new ResponseStatusException(NOT_FOUND, "Objective with id 6 not found"));

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveBusinessService.getEntityById(6L));

        // assert
        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Objective with id 6 not found", exception.getReason());
    }

    @Test
    void shouldSaveANewObjective() {
        // arrange
        Objective objective = spy(Objective.Builder.builder().withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).build());
        doNothing().when(objective).setCreatedOn(any());

        // act
        objectiveBusinessService.createEntity(objective, authorizationUser);

        // assert
        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(0)).createEntity(any());
        assertEquals(DRAFT, objective.getState());
        assertEquals(user, objective.getCreatedBy());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldUpdateAnObjective() {
        // arrange
        Objective objective = spy(Objective.Builder.builder().withId(3L).withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).build());
        when(objectivePersistenceService.findById(anyLong())).thenReturn(objective);
        when(alignmentBusinessService.getTargetIdByAlignedObjectiveId(any())).thenReturn(null);
        when(objectivePersistenceService.save(any())).thenReturn(objective);
        doNothing().when(objective).setCreatedOn(any());

        // act
        Objective updatedObjective = objectiveBusinessService.updateEntity(objective.getId(), objective,
                authorizationUser);

        // assert
        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(0)).updateEntity(any(), any());
        assertEquals(objective.getTitle(), updatedObjective.getTitle());
        assertEquals(objective.getTeam(), updatedObjective.getTeam());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldUpdateAnObjectiveWithAlignment() {
        // arrange
        Objective objective = spy(Objective.Builder.builder().withId(3L).withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).withAlignedEntity(alignedEntityDtoObjective).build());
        when(objectivePersistenceService.findById(anyLong())).thenReturn(objective);
        when(alignmentBusinessService.getTargetIdByAlignedObjectiveId(any()))
                .thenReturn(new AlignedEntityDto(41L, OBJECTIVE));
        when(objectivePersistenceService.save(any())).thenReturn(objective);
        doNothing().when(objective).setCreatedOn(any());

        // act
        Objective updatedObjective = objectiveBusinessService.updateEntity(objective.getId(), objective,
                authorizationUser);
        objective.setAlignedEntity(alignedEntityDtoObjective);

        // assert
        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(1)).updateEntity(objective.getId(), objective);
        assertEquals(objective.getTitle(), updatedObjective.getTitle());
        assertEquals(objective.getTeam(), updatedObjective.getTeam());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldUpdateAnObjectiveWithANewAlignment() {
        // arrange
        Objective objective = spy(Objective.Builder.builder().withId(3L).withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).withAlignedEntity(alignedEntityDtoObjective).build());
        when(objectivePersistenceService.findById(anyLong())).thenReturn(objective);
        when(alignmentBusinessService.getTargetIdByAlignedObjectiveId(any())).thenReturn(null);
        when(objectivePersistenceService.save(any())).thenReturn(objective);
        doNothing().when(objective).setCreatedOn(any());

        // act
        Objective updatedObjective = objectiveBusinessService.updateEntity(objective.getId(), objective,
                authorizationUser);
        objective.setAlignedEntity(alignedEntityDtoObjective);

        // assert
        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(1)).updateEntity(objective.getId(), objective);
        assertEquals(objective.getTitle(), updatedObjective.getTitle());
        assertEquals(objective.getTeam(), updatedObjective.getTeam());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldUpdateAnObjectiveWithAlignmentDelete() {
        // arrange
        Objective objective = spy(Objective.Builder.builder().withId(3L).withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).build());
        when(objectivePersistenceService.findById(anyLong())).thenReturn(objective);
        when(alignmentBusinessService.getTargetIdByAlignedObjectiveId(any()))
                .thenReturn(new AlignedEntityDto(52L, "objective"));
        when(objectivePersistenceService.save(any())).thenReturn(objective);
        doNothing().when(objective).setCreatedOn(any());

        // act
        Objective updatedObjective = objectiveBusinessService.updateEntity(objective.getId(), objective,
                authorizationUser);

        // assert
        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(1)).updateEntity(objective.getId(), objective);
        assertEquals(objective.getTitle(), updatedObjective.getTitle());
        assertEquals(objective.getTeam(), updatedObjective.getTeam());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldSaveANewObjectiveWithAlignment() {
        // arrange
        Objective objective = spy(Objective.Builder.builder().withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).withAlignedEntity(new AlignedEntityDto(42L, OBJECTIVE)).build());
        doNothing().when(objective).setCreatedOn(any());

        // act
        Objective savedObjective = objectiveBusinessService.createEntity(objective, authorizationUser);

        // assert
        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(1)).createEntity(savedObjective);
        assertEquals(DRAFT, objective.getState());
        assertEquals(user, objective.getCreatedBy());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenPuttingNullId() {
        // arrange
        Objective objective1 = Objective.Builder.builder().withId(null).withTitle("Title")
                .withDescription("Description").withModifiedOn(LocalDateTime.now()).build();
        when(objectiveBusinessService.createEntity(objective1, authorizationUser)).thenReturn(fullObjectiveCreate);

        // act
        Objective savedObjective = objectiveBusinessService.createEntity(objective1, authorizationUser);

        // assert
        assertNull(savedObjective.getId());
        assertEquals("FullObjective1", savedObjective.getTitle());
        assertEquals("Bob", savedObjective.getCreatedBy().getFirstname());
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void updateEntityShouldHandleQuarterCorrectly(boolean hasKeyResultAnyCheckIns) {
        Long id = 27L;
        String title = "Received Objective";
        String description = "The description";
        Quarter changedQuarter = Quarter.Builder.builder().withId(2L).withLabel("another quarter").build();
        Objective savedObjective = Objective.Builder.builder().withId(id).withTitle(title).withTeam(team1)
                .withQuarter(quarter).withDescription(null).withModifiedOn(null).withModifiedBy(null).build();
        Objective changedObjective = Objective.Builder.builder().withId(id).withTitle(title).withTeam(team1)
                .withQuarter(changedQuarter).withDescription(description).withModifiedOn(null).withModifiedBy(null)
                .build();
        Objective updatedObjective = Objective.Builder.builder().withId(id).withTitle(title).withTeam(team1)
                .withQuarter(hasKeyResultAnyCheckIns ? quarter : changedQuarter).withDescription(description)
                .withModifiedOn(null).withModifiedBy(null).build();

        when(objectivePersistenceService.findById(any())).thenReturn(savedObjective);
        when(keyResultBusinessService.getAllKeyResultsByObjective(savedObjective.getId())).thenReturn(keyResultList);
        when(keyResultBusinessService.hasKeyResultAnyCheckIns(any())).thenReturn(hasKeyResultAnyCheckIns);
        when(objectivePersistenceService.save(changedObjective)).thenReturn(updatedObjective);
        when(alignmentBusinessService.getTargetIdByAlignedObjectiveId(any())).thenReturn(null);
        boolean isImUsed = objectiveBusinessService.isImUsed(changedObjective);
        Objective updatedEntity = objectiveBusinessService.updateEntity(changedObjective.getId(), changedObjective,
                authorizationUser);

        assertEquals(hasKeyResultAnyCheckIns, isImUsed);
        assertEquals(hasKeyResultAnyCheckIns ? savedObjective.getQuarter() : changedObjective.getQuarter(),
                updatedEntity.getQuarter());
        assertEquals(changedObjective.getDescription(), updatedEntity.getDescription());
        assertEquals(changedObjective.getTitle(), updatedEntity.getTitle());
        verify(alignmentBusinessService, times(0)).updateEntity(any(), any());
    }

    @Test
    void shouldDeleteObjectiveAndAssociatedKeyResults() {
        // arrange
        when(keyResultBusinessService.getAllKeyResultsByObjective(1L)).thenReturn(keyResultList);

        // act
        objectiveBusinessService.deleteEntityById(1L);

        // assert
        verify(keyResultBusinessService, times(2)).deleteEntityById(5L);
        verify(objectiveBusinessService, times(1)).deleteEntityById(1L);
        verify(alignmentBusinessService, times(1)).deleteAlignmentByObjectiveId(1L);
    }

    @Test
    void shouldDuplicateObjective() {
        // arrange
        Objective sourceObjective = Objective.Builder.builder() //
                .withId(23L) //
                .withTitle("Objective 1") //
                .build();
        KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder() //
                .withTitle("Ordinal 1") //
                .withObjective(sourceObjective) //
                .build();
        KeyResult keyResultMetric = KeyResultMetric.Builder.builder() //
                .withTitle("Metric 1") //
                .withObjective(sourceObjective) //
                .withUnit(Unit.FTE) //
                .build();

        // new Objective with no KeyResults
        Objective newObjective = Objective.Builder.builder() //
                .withId(42L) //
                .withTitle("Objective 2") //
                .build();

        when(objectivePersistenceService.save(any())).thenReturn(newObjective);
        when(keyResultBusinessService.getAllKeyResultsByObjective(anyLong()))
                .thenReturn(List.of(keyResultOrdinal, keyResultMetric));

        // act
        Objective duplicatedObjective = objectiveBusinessService.duplicateObjective(sourceObjective.getId(),
                newObjective, authorizationUser);

        // assert
        assertNotEquals(sourceObjective.getId(), duplicatedObjective.getId());
        assertEquals(newObjective.getId(), duplicatedObjective.getId());
        assertEquals(newObjective.getTitle(), duplicatedObjective.getTitle());

        // called for creating the new Objective
        verify(objectiveBusinessService, times(1)).createEntity(any(), any());
        // called for creating the new KeyResults
        verify(keyResultBusinessService, times(2)).createEntity(any(), any());
    }

    @Test
    void shouldReturnAlignmentPossibilities() {
        // arrange
        when(objectivePersistenceService.findObjectiveByQuarterId(anyLong())).thenReturn(objectiveList);
        when(keyResultBusinessService.getAllKeyResultsByObjective(anyLong())).thenReturn(keyResultList);

        // act
        List<AlignmentDto> alignmentsDtos = objectiveBusinessService.getAlignmentPossibilities(5L);

        // assert
        verify(objectivePersistenceService, times(1)).findObjectiveByQuarterId(5L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(1L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(2L);
        assertEquals(alignmentsDtos, List.of(alignmentDto));
    }

    @Test
    void shouldReturnAlignmentPossibilitiesWithMultipleTeams() {
        // arrange
        List<Objective> objectiveList = List.of(fullObjective1, fullObjective2, fullObjective3);
        when(objectivePersistenceService.findObjectiveByQuarterId(anyLong())).thenReturn(objectiveList);
        when(keyResultBusinessService.getAllKeyResultsByObjective(anyLong())).thenReturn(keyResultList);
        AlignmentObjectDto alignmentObjectDto1 = new AlignmentObjectDto(3L, "O - FullObjective5", OBJECTIVE);
        AlignmentObjectDto alignmentObjectDto2 = new AlignmentObjectDto(5L, "KR - Keyresult Ordinal", "keyResult");
        AlignmentObjectDto alignmentObjectDto3 = new AlignmentObjectDto(5L, "KR - Keyresult Ordinal", "keyResult");
        AlignmentDto alignmentDto = new AlignmentDto(3L, TEAM_PUZZLE,
                List.of(alignmentObjectDto1, alignmentObjectDto2, alignmentObjectDto3));

        // act
        List<AlignmentDto> alignmentsDtos = objectiveBusinessService.getAlignmentPossibilities(5L);

        // assert
        verify(objectivePersistenceService, times(1)).findObjectiveByQuarterId(5L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(1L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(2L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(3L);
        assertEquals(alignmentsDtos, List.of(alignmentDto, this.alignmentDto));
    }

    @Test
    void shouldReturnAlignmentPossibilitiesOnlyObjectives() {
        // arrange
        when(objectivePersistenceService.findObjectiveByQuarterId(anyLong())).thenReturn(objectiveList);
        when(keyResultBusinessService.getAllKeyResultsByObjective(anyLong())).thenReturn(List.of());

        // act
        List<AlignmentDto> alignmentsDtos = objectiveBusinessService.getAlignmentPossibilities(5L);

        // assert
        verify(objectivePersistenceService, times(1)).findObjectiveByQuarterId(5L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(1L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(2L);
        assertEquals(2, alignmentsDtos.get(0).alignmentObjects().size());
        assertEquals(1, alignmentsDtos.get(0).alignmentObjects().get(0).objectId());
        assertEquals(FULL_OBJECTIVE_1, alignmentsDtos.get(0).alignmentObjects().get(0).objectTitle());
        assertEquals(OBJECTIVE, alignmentsDtos.get(0).alignmentObjects().get(0).objectType());
        assertEquals(2, alignmentsDtos.get(0).alignmentObjects().get(1).objectId());
        assertEquals(FULL_OBJECTIVE_2, alignmentsDtos.get(0).alignmentObjects().get(1).objectTitle());
        assertEquals(OBJECTIVE, alignmentsDtos.get(0).alignmentObjects().get(1).objectType());
    }

    @Test
    void shouldReturnEmptyAlignmentPossibilities() {
        // arrange
        List<Objective> objectiveList = List.of();
        when(objectivePersistenceService.findObjectiveByQuarterId(anyLong())).thenReturn(objectiveList);

        // act
        List<AlignmentDto> alignmentsDtos = objectiveBusinessService.getAlignmentPossibilities(5L);

        // assert
        verify(objectivePersistenceService, times(1)).findObjectiveByQuarterId(5L);
        verify(keyResultBusinessService, times(0)).getAllKeyResultsByObjective(anyLong());
        assertEquals(0, alignmentsDtos.size());
    }

    @Test
    void shouldThrowExceptionWhenQuarterIdIsNull() {
        // arrange
        Mockito.doThrow(new OkrResponseStatusException(HttpStatus.BAD_REQUEST, "ATTRIBUTE_NULL")).when(validator)
                .validateOnGet(null);

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class, () -> {
            objectiveBusinessService.getAlignmentPossibilities(null);
        });

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
    }
}
