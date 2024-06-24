package ch.puzzle.okr.service.business;

import ch.puzzle.okr.dto.AlignmentDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.ObjectiveValidationService;
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

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
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

    private final Team team1 = Team.Builder.builder().withId(1L).withName("Team1").build();
    private final Quarter quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
    private final User user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
            .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
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
    private final Team team2 = Team.Builder.builder().withId(3L).withName("Puzzle ITC").build();
    private final Objective fullObjective3 = Objective.Builder.builder().withId(3L).withTitle("FullObjective5")
            .withCreatedBy(user).withTeam(team2).withQuarter(quarter).withDescription("This is our description")
            .withModifiedOn(LocalDateTime.MAX).build();
    private final KeyResult ordinalKeyResult2 = KeyResultOrdinal.Builder.builder().withCommitZone("Baum")
            .withStretchZone("Wald").withId(6L).withTitle("Keyresult Ordinal 6").withObjective(fullObjective3).build();
    private final KeyResult ordinalKeyResult = KeyResultOrdinal.Builder.builder().withCommitZone("Baum")
            .withStretchZone("Wald").withId(5L).withTitle("Keyresult Ordinal").withObjective(objective1).build();
    private final List<KeyResult> keyResultList = List.of(ordinalKeyResult, ordinalKeyResult);
    private final List<Objective> objectiveList = List.of(fullObjective1, fullObjective2);

    @Test
    void getOneObjective() {
        when(objectivePersistenceService.findById(5L)).thenReturn(objective1);

        Objective realObjective = objectiveBusinessService.getEntityById(5L);

        verify(alignmentBusinessService, times(1)).getTargetIdByAlignedObjectiveId(5L);
        assertEquals("Objective 1", realObjective.getTitle());
    }

    @Test
    void getEntitiesByTeamId() {
        when(objectivePersistenceService.findObjectiveByTeamId(anyLong())).thenReturn(List.of(objective1));

        List<Objective> entities = objectiveBusinessService.getEntitiesByTeamId(5L);

        verify(alignmentBusinessService, times(1)).getTargetIdByAlignedObjectiveId(5L);
        assertThat(entities).hasSameElementsAs(List.of(objective1));
    }

    @Test
    void shouldNotFindTheObjective() {
        when(objectivePersistenceService.findById(6L))
                .thenThrow(new ResponseStatusException(NOT_FOUND, "Objective with id 6 not found"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveBusinessService.getEntityById(6L));
        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Objective with id 6 not found", exception.getReason());
    }

    @Test
    void shouldSaveANewObjective() {
        Objective objective = spy(Objective.Builder.builder().withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).build());

        doNothing().when(objective).setCreatedOn(any());

        objectiveBusinessService.createEntity(objective, authorizationUser);

        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(0)).createEntity(any());
        assertEquals(DRAFT, objective.getState());
        assertEquals(user, objective.getCreatedBy());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldUpdateAnObjective() {
        Objective objective = spy(Objective.Builder.builder().withId(3L).withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).build());

        when(objectivePersistenceService.findById(anyLong())).thenReturn(objective);
        when(alignmentBusinessService.getTargetIdByAlignedObjectiveId(any())).thenReturn(null);
        when(objectivePersistenceService.save(any())).thenReturn(objective);
        doNothing().when(objective).setCreatedOn(any());

        Objective updatedObjective = objectiveBusinessService.updateEntity(objective.getId(), objective,
                authorizationUser);

        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(0)).updateEntity(any(), any());
        assertEquals(objective.getTitle(), updatedObjective.getTitle());
        assertEquals(objective.getTeam(), updatedObjective.getTeam());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldUpdateAnObjectiveWithAlignment() {
        Objective objective = spy(Objective.Builder.builder().withId(3L).withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).withAlignedEntityId("O53").build());

        when(objectivePersistenceService.findById(anyLong())).thenReturn(objective);
        when(alignmentBusinessService.getTargetIdByAlignedObjectiveId(any())).thenReturn("O41");
        when(objectivePersistenceService.save(any())).thenReturn(objective);
        doNothing().when(objective).setCreatedOn(any());

        Objective updatedObjective = objectiveBusinessService.updateEntity(objective.getId(), objective,
                authorizationUser);

        objective.setAlignedEntityId("O53");

        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(1)).updateEntity(objective.getId(), objective);
        assertEquals(objective.getTitle(), updatedObjective.getTitle());
        assertEquals(objective.getTeam(), updatedObjective.getTeam());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldUpdateAnObjectiveWithANewAlignment() {
        Objective objective = spy(Objective.Builder.builder().withId(3L).withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).withAlignedEntityId("O53").build());

        when(objectivePersistenceService.findById(anyLong())).thenReturn(objective);
        when(alignmentBusinessService.getTargetIdByAlignedObjectiveId(any())).thenReturn(null);
        when(objectivePersistenceService.save(any())).thenReturn(objective);
        doNothing().when(objective).setCreatedOn(any());

        Objective updatedObjective = objectiveBusinessService.updateEntity(objective.getId(), objective,
                authorizationUser);

        objective.setAlignedEntityId("O53");

        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(1)).updateEntity(objective.getId(), objective);
        assertEquals(objective.getTitle(), updatedObjective.getTitle());
        assertEquals(objective.getTeam(), updatedObjective.getTeam());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldUpdateAnObjectiveWithAlignmentDelete() {
        Objective objective = spy(Objective.Builder.builder().withId(3L).withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).build());

        when(objectivePersistenceService.findById(anyLong())).thenReturn(objective);
        when(alignmentBusinessService.getTargetIdByAlignedObjectiveId(any())).thenReturn("O52");
        when(objectivePersistenceService.save(any())).thenReturn(objective);
        doNothing().when(objective).setCreatedOn(any());

        Objective updatedObjective = objectiveBusinessService.updateEntity(objective.getId(), objective,
                authorizationUser);

        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(1)).updateEntity(objective.getId(), objective);
        assertEquals(objective.getTitle(), updatedObjective.getTitle());
        assertEquals(objective.getTeam(), updatedObjective.getTeam());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldSaveANewObjectiveWithAlignment() {
        Objective objective = spy(Objective.Builder.builder().withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(DRAFT).withAlignedEntityId("O42").build());

        doNothing().when(objective).setCreatedOn(any());

        Objective savedObjective = objectiveBusinessService.createEntity(objective, authorizationUser);

        verify(objectivePersistenceService, times(1)).save(objective);
        verify(alignmentBusinessService, times(1)).createEntity(savedObjective);
        assertEquals(DRAFT, objective.getState());
        assertEquals(user, objective.getCreatedBy());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenPuttingNullId() {
        Objective objective1 = Objective.Builder.builder().withId(null).withTitle("Title")
                .withDescription("Description").withModifiedOn(LocalDateTime.now()).build();
        when(objectiveBusinessService.createEntity(objective1, authorizationUser)).thenReturn(fullObjectiveCreate);

        Objective savedObjective = objectiveBusinessService.createEntity(objective1, authorizationUser);
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
        when(keyResultBusinessService.getAllKeyResultsByObjective(1L)).thenReturn(keyResultList);

        objectiveBusinessService.deleteEntityById(1L);

        verify(keyResultBusinessService, times(2)).deleteEntityById(5L);
        verify(objectiveBusinessService, times(1)).deleteEntityById(1L);
        verify(alignmentBusinessService, times(1)).deleteAlignmentByObjectiveId(1L);
    }

    @Test
    void shouldDuplicateObjective() {
        KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder().withTitle("Ordinal").build();
        KeyResult keyResultOrdinal2 = KeyResultOrdinal.Builder.builder().withTitle("Ordinal2").build();
        KeyResult keyResultMetric = KeyResultMetric.Builder.builder().withTitle("Metric").withUnit(Unit.FTE).build();
        KeyResult keyResultMetric2 = KeyResultMetric.Builder.builder().withTitle("Metric2").withUnit(Unit.CHF).build();
        List<KeyResult> keyResults = List.of(keyResultOrdinal, keyResultOrdinal2, keyResultMetric, keyResultMetric2);

        when(objectivePersistenceService.save(any())).thenReturn(objective1);
        when(keyResultBusinessService.getAllKeyResultsByObjective(anyLong())).thenReturn(keyResults);

        objectiveBusinessService.duplicateObjective(objective1.getId(), objective1, authorizationUser);
        verify(keyResultBusinessService, times(4)).createEntity(any(), any());
        verify(objectiveBusinessService, times(1)).createEntity(any(), any());
    }

    @Test
    void shouldReturnAlignmentPossibilities() {
        when(objectivePersistenceService.findObjectiveByQuarterId(anyLong())).thenReturn(objectiveList);
        when(keyResultBusinessService.getAllKeyResultsByObjective(anyLong())).thenReturn(keyResultList);

        List<AlignmentDto> alignmentsDtos = objectiveBusinessService.getAlignmentPossibilities(5L);

        verify(objectivePersistenceService, times(1)).findObjectiveByQuarterId(5L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(1L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(2L);
        assertEquals("Team1", alignmentsDtos.get(0).teamName());
        assertEquals(1, alignmentsDtos.get(0).teamId());
        assertEquals(6, alignmentsDtos.get(0).alignmentObjectDtos().size());
        assertEquals(1, alignmentsDtos.get(0).alignmentObjectDtos().get(0).objectId());
        assertEquals("O - FullObjective1", alignmentsDtos.get(0).alignmentObjectDtos().get(0).objectTitle());
        assertEquals("objective", alignmentsDtos.get(0).alignmentObjectDtos().get(0).objectType());
        assertEquals(5, alignmentsDtos.get(0).alignmentObjectDtos().get(1).objectId());
        assertEquals("KR - Keyresult Ordinal", alignmentsDtos.get(0).alignmentObjectDtos().get(1).objectTitle());
        assertEquals("keyResult", alignmentsDtos.get(0).alignmentObjectDtos().get(1).objectType());
        assertEquals(5, alignmentsDtos.get(0).alignmentObjectDtos().get(2).objectId());
        assertEquals("KR - Keyresult Ordinal", alignmentsDtos.get(0).alignmentObjectDtos().get(2).objectTitle());
        assertEquals("keyResult", alignmentsDtos.get(0).alignmentObjectDtos().get(2).objectType());
        assertEquals(2, alignmentsDtos.get(0).alignmentObjectDtos().get(3).objectId());
        assertEquals("O - FullObjective2", alignmentsDtos.get(0).alignmentObjectDtos().get(3).objectTitle());
        assertEquals("objective", alignmentsDtos.get(0).alignmentObjectDtos().get(3).objectType());
        assertEquals(5, alignmentsDtos.get(0).alignmentObjectDtos().get(4).objectId());
        assertEquals("KR - Keyresult Ordinal", alignmentsDtos.get(0).alignmentObjectDtos().get(4).objectTitle());
        assertEquals("keyResult", alignmentsDtos.get(0).alignmentObjectDtos().get(4).objectType());
        assertEquals(5, alignmentsDtos.get(0).alignmentObjectDtos().get(5).objectId());
        assertEquals("KR - Keyresult Ordinal", alignmentsDtos.get(0).alignmentObjectDtos().get(5).objectTitle());
        assertEquals("keyResult", alignmentsDtos.get(0).alignmentObjectDtos().get(5).objectType());
    }

    @Test
    void shouldReturnAlignmentPossibilitiesWithMultipleTeams() {
        List<Objective> objectiveList = List.of(fullObjective1, fullObjective2, fullObjective3);
        List<KeyResult> keyResultList = List.of(ordinalKeyResult, ordinalKeyResult2);

        when(objectivePersistenceService.findObjectiveByQuarterId(anyLong())).thenReturn(objectiveList);
        when(keyResultBusinessService.getAllKeyResultsByObjective(anyLong())).thenReturn(keyResultList);

        List<AlignmentDto> alignmentsDtos = objectiveBusinessService.getAlignmentPossibilities(5L);

        verify(objectivePersistenceService, times(1)).findObjectiveByQuarterId(5L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(1L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(2L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(3L);
        assertEquals(2, alignmentsDtos.size());
        assertEquals("Puzzle ITC", alignmentsDtos.get(0).teamName());
        assertEquals(3, alignmentsDtos.get(0).teamId());
        assertEquals(3, alignmentsDtos.get(0).alignmentObjectDtos().size());
        assertEquals("Team1", alignmentsDtos.get(1).teamName());
        assertEquals(1, alignmentsDtos.get(1).teamId());
        assertEquals(6, alignmentsDtos.get(1).alignmentObjectDtos().size());
        assertEquals(3, alignmentsDtos.get(0).alignmentObjectDtos().get(0).objectId());
        assertEquals("O - FullObjective5", alignmentsDtos.get(0).alignmentObjectDtos().get(0).objectTitle());
        assertEquals("objective", alignmentsDtos.get(0).alignmentObjectDtos().get(0).objectType());
        assertEquals(1, alignmentsDtos.get(1).alignmentObjectDtos().get(0).objectId());
        assertEquals("O - FullObjective1", alignmentsDtos.get(1).alignmentObjectDtos().get(0).objectTitle());
        assertEquals("objective", alignmentsDtos.get(1).alignmentObjectDtos().get(0).objectType());
    }

    @Test
    void shouldReturnAlignmentPossibilitiesOnlyObjectives() {
        when(objectivePersistenceService.findObjectiveByQuarterId(anyLong())).thenReturn(objectiveList);
        when(keyResultBusinessService.getAllKeyResultsByObjective(anyLong())).thenReturn(List.of());

        List<AlignmentDto> alignmentsDtos = objectiveBusinessService.getAlignmentPossibilities(5L);

        verify(objectivePersistenceService, times(1)).findObjectiveByQuarterId(5L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(1L);
        verify(keyResultBusinessService, times(1)).getAllKeyResultsByObjective(2L);
        assertEquals(2, alignmentsDtos.get(0).alignmentObjectDtos().size());
        assertEquals(1, alignmentsDtos.get(0).alignmentObjectDtos().get(0).objectId());
        assertEquals("O - FullObjective1", alignmentsDtos.get(0).alignmentObjectDtos().get(0).objectTitle());
        assertEquals("objective", alignmentsDtos.get(0).alignmentObjectDtos().get(0).objectType());
        assertEquals(2, alignmentsDtos.get(0).alignmentObjectDtos().get(1).objectId());
        assertEquals("O - FullObjective2", alignmentsDtos.get(0).alignmentObjectDtos().get(1).objectTitle());
        assertEquals("objective", alignmentsDtos.get(0).alignmentObjectDtos().get(1).objectType());
    }

    @Test
    void shouldReturnEmptyAlignmentPossibilities() {
        List<Objective> objectiveList = List.of();

        when(objectivePersistenceService.findObjectiveByQuarterId(anyLong())).thenReturn(objectiveList);

        List<AlignmentDto> alignmentsDtos = objectiveBusinessService.getAlignmentPossibilities(5L);

        verify(objectivePersistenceService, times(1)).findObjectiveByQuarterId(5L);
        verify(keyResultBusinessService, times(0)).getAllKeyResultsByObjective(anyLong());
        assertEquals(0, alignmentsDtos.size());
    }

    @Test
    void shouldThrowExceptionWhenQuarterIdIsNull() {
        Mockito.doThrow(new OkrResponseStatusException(HttpStatus.BAD_REQUEST, "ATTRIBUTE_NULL")).when(validator)
                .validateOnGet(null);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class, () -> {
            objectiveBusinessService.getAlignmentPossibilities(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
    }
}
