package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.keyResult.KeyResult;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.ObjectiveValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static ch.puzzle.okr.TestHelper.mockJwtToken;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectiveBusinessServiceTest {
    @InjectMocks
    @Spy
    ObjectiveBusinessService objectiveBusinessService;
    @MockBean
    ObjectivePersistenceService objectivePersistenceService = Mockito.mock(ObjectivePersistenceService.class);
    @MockBean
    KeyResultPersistenceService keyResultPersistenceService = Mockito.mock(KeyResultPersistenceService.class);
    @MockBean
    KeyResultBusinessService keyResultBusinessService = Mockito.mock(KeyResultBusinessService.class);
    @MockBean
    ObjectiveValidationService validator = Mockito.mock(ObjectiveValidationService.class);
    @MockBean
    UserBusinessService userBusinessService = Mockito.mock(UserBusinessService.class);

    Objective objective;
    Objective fullObjective1;
    KeyResult keyResult;
    User user;
    Quarter quarter;
    List<KeyResult> keyResultList;
    Team team1;
    Jwt jwtToken;

    @BeforeEach
    void setUp() {
        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
        this.keyResult = KeyResult.Builder.builder().withId(5L).withTitle("Keyresult 1").withObjective(objective)
                .build();
        this.keyResultList = List.of(keyResult, keyResult, keyResult);

        user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann").withUsername("bkaufmann")
                .withEmail("kaufmann@puzzle.ch").build();
        this.team1 = Team.Builder.builder().withId(1L).withName("Team1").build();
        quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
        this.fullObjective1 = Objective.Builder.builder().withTitle("FullObjective1").withCreatedBy(user)
                .withTeam(team1).withQuarter(quarter).withDescription("This is our description")
                .withModifiedOn(LocalDateTime.MAX).build();

        this.jwtToken = mockJwtToken("johnny", "Johnny", "Appleseed", "test@test.ch");
    }

    @Test
    void getOneObjective() {
        Mockito.when(objectivePersistenceService.findById(5L)).thenReturn(this.objective);
        Objective realObjective = objectiveBusinessService.getObjectiveById(5L);

        assertEquals("Objective 1", realObjective.getTitle());
    }

    @Test
    void shouldNotFindTheObjective() {
        Mockito.when(objectivePersistenceService.findById(6L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective with id 6 not found"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveBusinessService.getObjectiveById(6L));
        assertEquals(404, exception.getRawStatusCode());
        assertEquals("Objective with id 6 not found", exception.getReason());
    }

    @Test
    void shouldSaveANewObjective() {
        Objective objective = spy(
                Objective.Builder.builder().withTitle("Received Objective").withTeam(team1).withQuarter(quarter)
                        .withDescription("The description").withModifiedOn(null).withModifiedBy(null).build());

        doNothing().when(objective).setCreatedOn(any());
        Mockito.when(userBusinessService.getUserByAuthorisationToken(any())).thenReturn(user);

        objectiveBusinessService.createObjective(objective, jwtToken);

        verify(objectivePersistenceService, times(1)).save(objective);
        assertEquals(State.DRAFT, objective.getState());
        assertEquals(user, objective.getCreatedBy());
        assertNull(objective.getCreatedOn());
    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenPuttingNullId() {
        Objective objective1 = Objective.Builder.builder().withId(null).withTitle("Title")
                .withDescription("Description").withModifiedOn(LocalDateTime.now()).build();
        Mockito.when(objectiveBusinessService.createObjective(objective1, jwtToken)).thenReturn(this.fullObjective1);
        Mockito.when(userBusinessService.getUserByAuthorisationToken(any())).thenReturn(user);

        Objective savedObjective = objectiveBusinessService.createObjective(objective1, jwtToken);
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
        Mockito.when(userBusinessService.getUserByAuthorisationToken(any())).thenReturn(user);

        objectiveBusinessService.updateObjective(objective.getId(), objective, jwtToken);

        verify(objectivePersistenceService).save(objective);
        assertEquals(user, objective.getModifiedBy());
        assertNull(objective.getModifiedOn());
    }

    @Test
    void shouldDeleteObjectiveAndAssociatedKeyResults() {
        when(this.objectivePersistenceService.findById(anyLong())).thenReturn(objective);
        when(this.keyResultPersistenceService.getKeyResultsByObjective(objective)).thenReturn(keyResultList);
        when(keyResultBusinessService.getAllKeyResultsByObjective(1L)).thenReturn(this.keyResultList);

        this.objectiveBusinessService.deleteObjectiveById(1L);

        verify(this.keyResultBusinessService, times(3)).deleteKeyResultById(5L);
        verify(this.objectiveBusinessService, times(1)).deleteObjectiveById(1L);
    }

    @Test
    void verifyActiveObjectivesAmountOfTeam() {
        this.objectiveBusinessService.activeObjectivesAmountOfTeam(team1, quarter);

        verify(this.objectiveBusinessService, times(1)).activeObjectivesAmountOfTeam(team1, quarter);
    }
}
