package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.ObjectiveValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    ObjectiveValidationService validator = Mockito.mock(ObjectiveValidationService.class);

    Objective objective;
    Objective fullObjective1;
    KeyResult ordinalKeyResult;
    User user;
    Quarter quarter;
    List<KeyResult> keyResultList;
    Team team1;

    @BeforeEach
    void setUp() {
        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
        this.ordinalKeyResult = KeyResultOrdinal.Builder.builder().withCommitZone("Baum").withStretchZone("Wald")
                .withId(5L).withTitle("Keyresult Ordinal").withObjective(this.objective).build();
        this.keyResultList = List.of(ordinalKeyResult, ordinalKeyResult, ordinalKeyResult);

        user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann").withUsername("bkaufmann")
                .withEmail("kaufmann@puzzle.ch").build();
        this.team1 = Team.Builder.builder().withId(1L).withName("Team1").build();
        quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
        this.fullObjective1 = Objective.Builder.builder().withTitle("FullObjective1").withCreatedBy(user)
                .withTeam(team1).withQuarter(quarter).withDescription("This is our description")
                .withModifiedOn(LocalDateTime.MAX).build();
    }

    @Test
    void getOneObjective() {
        Mockito.when(objectivePersistenceService.findById(5L)).thenReturn(this.objective);
        Objective realObjective = objectiveBusinessService.getEntityById(5L);

        assertEquals("Objective 1", realObjective.getTitle());
    }

    @Test
    void shouldNotFindTheObjective() {
        Mockito.when(objectivePersistenceService.findById(6L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective with id 6 not found"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveBusinessService.getEntityById(6L));
        assertEquals(404, exception.getRawStatusCode());
        assertEquals("Objective with id 6 not found", exception.getReason());
    }

    @Test
    void shouldSaveANewObjective() {
        Objective objective = spy(Objective.Builder.builder().withTitle("Received Objective").withTeam(team1)
                .withQuarter(quarter).withDescription("The description").withModifiedOn(null).withModifiedBy(null)
                .withState(State.DRAFT).build());

        doNothing().when(objective).setCreatedOn(any());

        objectiveBusinessService.createEntity(objective, authorizationUser);

        verify(objectivePersistenceService, times(1)).save(objective);
        assertEquals(State.DRAFT, objective.getState());
        assertEquals(user, objective.getCreatedBy());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenPuttingNullId() {
        Objective objective1 = Objective.Builder.builder().withId(null).withTitle("Title")
                .withDescription("Description").withModifiedOn(LocalDateTime.now()).build();
        Mockito.when(objectiveBusinessService.createEntity(objective1, authorizationUser))
                .thenReturn(this.fullObjective1);

        Objective savedObjective = objectiveBusinessService.createEntity(objective1, authorizationUser);
        assertNull(savedObjective.getId());
        assertEquals("FullObjective1", savedObjective.getTitle());
        assertEquals("Bob", savedObjective.getCreatedBy().getFirstname());
    }

    @Test
    void shouldUpdateObjective() {
        Objective objective = spy(
                Objective.Builder.builder().withTitle("Received Objective").withTeam(team1).withQuarter(quarter)
                        .withDescription("The description").withModifiedOn(null).withModifiedBy(null).build());

        doNothing().when(objective).setModifiedOn(any());
        Mockito.when(objectivePersistenceService.findById(any())).thenReturn(objective);

        objectiveBusinessService.updateEntity(objective.getId(), objective, authorizationUser);

        verify(objectivePersistenceService).save(objective);
        assertEquals(user, objective.getModifiedBy());
        assertNull(objective.getModifiedOn());
    }

    @Test
    void shouldDeleteObjectiveAndAssociatedKeyResults() {
        when(keyResultBusinessService.getAllKeyResultsByObjective(1L)).thenReturn(this.keyResultList);

        this.objectiveBusinessService.deleteEntityById(1L);

        verify(this.keyResultBusinessService, times(3)).deleteEntityById(5L);
        verify(this.objectiveBusinessService, times(1)).deleteEntityById(1L);
    }

    @Test
    void verifyActiveObjectivesAmountOfTeam() {
        this.objectiveBusinessService.activeObjectivesAmountOfTeam(team1, quarter);
        verify(this.objectiveBusinessService, times(1)).activeObjectivesAmountOfTeam(team1, quarter);
    }

    @Test
    void shouldDuplicateObjective() {
        KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder().withTitle("Ordinal").build();
        KeyResult keyResultOrdinal2 = KeyResultOrdinal.Builder.builder().withTitle("Ordinal2").build();
        KeyResult keyResultMetric = KeyResultMetric.Builder.builder().withTitle("Metric").withUnit(Unit.FTE).build();
        KeyResult keyResultMetric2 = KeyResultMetric.Builder.builder().withTitle("Metric2").withUnit(Unit.CHF).build();
        List<KeyResult> keyResults = List.of(keyResultOrdinal, keyResultOrdinal2, keyResultMetric, keyResultMetric2);

        Mockito.when(objectivePersistenceService.save(any())).thenReturn(objective);
        Mockito.when(keyResultBusinessService.getAllKeyResultsByObjective(anyLong())).thenReturn(keyResults);

        this.objectiveBusinessService.duplicateObjective(objective.getId(), objective, authorizationUser);
        verify(this.keyResultBusinessService, times(4)).createEntity(any(), any());
        verify(this.objectiveBusinessService, times(1)).createEntity(any(), any());
    }
}
