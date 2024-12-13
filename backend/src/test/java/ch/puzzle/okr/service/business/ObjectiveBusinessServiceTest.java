package ch.puzzle.okr.service.business;

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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    CompletedBusinessService completedBusinessService;
    @Mock
    ObjectiveValidationService validator = Mockito.mock(ObjectiveValidationService.class);

    private final Team team1 = Team.Builder.builder().withId(1L).withName("Team1").build();
    private final Quarter quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
    private final User user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
            .withEmail("kaufmann@puzzle.ch").build();
    private final Objective objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    private final Objective fullObjective = Objective.Builder.builder().withTitle("FullObjective1").withCreatedBy(user)
            .withTeam(team1).withQuarter(quarter).withDescription("This is our description")
            .withModifiedOn(LocalDateTime.MAX).build();
    private final KeyResult ordinalKeyResult = KeyResultOrdinal.Builder.builder().withCommitZone("Baum")
            .withStretchZone("Wald").withId(5L).withTitle("Keyresult Ordinal").withObjective(objective).build();
    private final List<KeyResult> keyResultList = List.of(ordinalKeyResult, ordinalKeyResult, ordinalKeyResult);

    @Test
    void getOneObjective() {
        when(objectivePersistenceService.findById(5L)).thenReturn(objective);

        Objective realObjective = objectiveBusinessService.getEntityById(5L);

        assertEquals("Objective 1", realObjective.getTitle());
    }

    @Test
    void getEntitiesByTeamId() {
        when(objectivePersistenceService.findObjectiveByTeamId(anyLong())).thenReturn(List.of(objective));

        List<Objective> entities = objectiveBusinessService.getEntitiesByTeamId(5L);

        assertThat(entities).hasSameElementsAs(List.of(objective));
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
        assertEquals(DRAFT, objective.getState());
        assertEquals(user, objective.getCreatedBy());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenPuttingNullId() {
        Objective objective1 = Objective.Builder.builder().withId(null).withTitle("Title")
                .withDescription("Description").withModifiedOn(LocalDateTime.now()).build();
        when(objectiveBusinessService.createEntity(objective1, authorizationUser)).thenReturn(fullObjective);

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

        boolean isImUsed = objectiveBusinessService.isImUsed(changedObjective);
        Objective updatedEntity = objectiveBusinessService.updateEntity(changedObjective.getId(), changedObjective,
                authorizationUser);

        assertEquals(hasKeyResultAnyCheckIns, isImUsed);
        assertEquals(hasKeyResultAnyCheckIns ? savedObjective.getQuarter() : changedObjective.getQuarter(),
                updatedEntity.getQuarter());
        assertEquals(changedObjective.getDescription(), updatedEntity.getDescription());
        assertEquals(changedObjective.getTitle(), updatedEntity.getTitle());
    }

    @Test
    void shouldDeleteObjectiveAndAssociatedKeyResults() {
        when(keyResultBusinessService.getAllKeyResultsByObjective(1L)).thenReturn(keyResultList);

        objectiveBusinessService.deleteEntityById(1L);

        verify(keyResultBusinessService, times(3)).deleteEntityById(5L);
        verify(objectiveBusinessService, times(1)).deleteEntityById(1L);
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

        List<KeyResult> keyResults = new ArrayList<>();
        keyResults.add(keyResultOrdinal);
        keyResults.add(keyResultMetric);

        // new Objective with no KeyResults
        Objective newObjective = Objective.Builder.builder() //
                .withId(42L) //
                .withTitle("Objective 2") //
                .build();

        when(objectivePersistenceService.save(any())).thenReturn(newObjective);

        // act
        Objective duplicatedObjective = objectiveBusinessService.duplicateObjective(sourceObjective.getId(),
                newObjective, authorizationUser, keyResults);

        // assert
        assertNotEquals(sourceObjective.getId(), duplicatedObjective.getId());
        assertEquals(newObjective.getId(), duplicatedObjective.getId());
        assertEquals(newObjective.getTitle(), duplicatedObjective.getTitle());

        // called for creating the new Objective and the new keyResults
        verify(objectiveBusinessService, times(1)).createEntity(any(), any());
        verify(keyResultBusinessService, times(2)).createEntity(any(), any());
    }

    @Test
    void testGetAllKeyResultsByObjective() {
        Long objectiveId = 5L;

        KeyResult keyResult1 = new KeyResultMetric();
        KeyResult keyResult2 = new KeyResultMetric();
        List<KeyResult> mockKeyResults = List.of(keyResult1, keyResult2);

        when(objectivePersistenceService.findById(objectiveId)).thenReturn(objective);
        when(keyResultBusinessService.getAllKeyResultsByObjective(objectiveId)).thenReturn(mockKeyResults);

        List<KeyResult> result = objectiveBusinessService.getAllKeyResultsByObjective(objectiveId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(keyResult1, result.get(0));
        assertSame(keyResult2, result.get(1));

        verify(objectivePersistenceService).findById(objectiveId);
        verify(keyResultBusinessService).getAllKeyResultsByObjective(objectiveId);
    }
}
