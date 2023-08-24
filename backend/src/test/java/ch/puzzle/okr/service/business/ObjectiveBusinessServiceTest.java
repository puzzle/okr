package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectiveBusinessServiceTest {
    @MockBean
    ObjectivePersistenceService objectivePersistenceService = Mockito.mock(ObjectivePersistenceService.class);
    @MockBean
    KeyResultPersistenceService keyResultPersistenceService = Mockito.mock(KeyResultPersistenceService.class);
    @MockBean
    KeyResultBusinessService keyResultBusinessService = Mockito.mock(KeyResultBusinessService.class);
    @MockBean
    TeamPersistenceService teamPersistenceService = Mockito.mock(TeamPersistenceService.class);
    @MockBean
    ProgressBusinessService progressBusinessService = Mockito.mock(ProgressBusinessService.class);
    Objective objective;
    Objective fullObjective1;
    Objective fullObjective2;
    Objective fullObjective3;
    KeyResult keyResult;
    @Mock
    User user;
    List<Objective> objectiveList;
    List<KeyResult> keyResults;
    List<Objective> fullObjectiveInTeam1List;
    Team team1;
    @InjectMocks
    @Spy
    private ObjectiveBusinessService objectiveBusinessService;

    @BeforeEach
    void setUp() {
        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
        this.objectiveList = List.of(objective, objective, objective);
        this.keyResult = KeyResult.Builder.builder().withId(5L).withTitle("Keyresult 1").withObjective(objective)
                .build();
        this.keyResults = List.of(keyResult, keyResult, keyResult);

        User user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
        this.team1 = Team.Builder.builder().withId(1L).withName("Team1").build();
        Quarter quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
        this.fullObjective1 = Objective.Builder.builder().withTitle("FullObjective1").withCreatedBy(user)
                .withTeam(team1).withQuarter(quarter).withDescription("This is our description").withProgress(null)
                .withModifiedOn(LocalDateTime.MAX).build();
        this.fullObjective2 = Objective.Builder.builder().withTitle("FullObjective2").withCreatedBy(user)
                .withTeam(team1).withQuarter(quarter).withDescription("This is our description").withProgress(33L)
                .withModifiedOn(LocalDateTime.MAX).build();
        this.fullObjective3 = Objective.Builder.builder().withId(5L).withTitle("FullObjective1").withCreatedBy(user)
                .withTeam(team1).withQuarter(quarter).withDescription("This is our description").withProgress(null)
                .withModifiedOn(LocalDateTime.MAX).build();
        this.fullObjectiveInTeam1List = List.of(fullObjective1, fullObjective2);

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
        Mockito.when(objectiveBusinessService.createObjective(any(), any())).thenReturn(fullObjective1);

        Objective savedObjective = objectiveBusinessService.createObjective(fullObjective1);
        assertNull(savedObjective.getId());
        assertEquals("FullObjective1", savedObjective.getTitle());
        assertEquals("This is our description", savedObjective.getDescription());
        assertNull(savedObjective.getProgress());
        assertEquals("Team1", savedObjective.getTeam().getName());
        assertEquals("Bob", savedObjective.getCreatedBy().getFirstname());
        assertEquals("GJ 22/23-Q2", savedObjective.getQuarter().getLabel());
        assertEquals(LocalDateTime.MAX, savedObjective.getModifiedOn());
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenPuttingIdCreatingObjective() {
        Objective objective1 = Objective.Builder.builder().withId(9L).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveBusinessService.createObjective(objective1));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Not allowed to give an id", exception.getReason());

    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenPuttingNullId() {
        Objective objective1 = Objective.Builder.builder().withId(null).withTitle("Title")
                .withDescription("Description").withProgress(null).withModifiedOn(LocalDateTime.now()).build();
        Mockito.when(objectiveBusinessService.createObjective(any(), any())).thenReturn(this.fullObjective1);

        Objective savedObjective = objectiveBusinessService.createObjective(objective1);
        assertNull(savedObjective.getId());
        assertEquals("FullObjective1", savedObjective.getTitle());
        assertNull(savedObjective.getProgress());
        assertEquals("Bob", savedObjective.getCreatedBy().getFirstname());
    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenCreatingObjectiveWithEmptyDescription() {
        this.fullObjective1.setDescription(null);
        Objective objective1 = Objective.Builder.builder().withId(null).withTitle("Title").withProgress(null)
                .withModifiedOn(LocalDateTime.now()).build();
        Mockito.when(objectiveBusinessService.createObjective(any(), any())).thenReturn(this.fullObjective1);

        Objective savedObjective = objectiveBusinessService.createObjective(objective1);
        assertEquals("FullObjective1", savedObjective.getTitle());
        assertNull(savedObjective.getDescription());
        assertEquals("Bob", savedObjective.getCreatedBy().getFirstname());
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenCreatingObjectiveWithEmptyCreatedOn() {
        this.fullObjective1.setModifiedOn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveBusinessService.createObjective(this.fullObjective1));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(("Failed to generate attribute modifiedOn when creating objective"), exception.getReason());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " ", "  " })
    void shouldNotCreateObjectiveWithEmptyTitle(String passedName) {
        Objective objective1 = Objective.Builder.builder().withTitle(passedName).build();
        Mockito.when(objectiveBusinessService.createObjective(any(), any())).thenReturn(objective1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveBusinessService.createObjective(objective1));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(("Missing attribute title when creating objective"), exception.getReason());
    }

    @Test
    void shouldReturnObjectiveProperly() {
        Objective newObjective = Objective.Builder.builder().withTitle("Hello World").withId(1L)
                .withDescription("This is a cool objective")
                .withCreatedBy(User.Builder.builder().withUsername("rudi").build()).withProgress(5L).withQuarter(null)
                .withModifiedOn(LocalDateTime.now()).withQuarter(this.fullObjective1.getQuarter())
                .withTeam(Team.Builder.builder().withId(1L).withName("Best Team").build()).build();
        Mockito.when(objectivePersistenceService.findById(anyLong())).thenReturn(newObjective);
        Mockito.when(objectiveBusinessService.updateObjective(any())).thenReturn(newObjective);

        Objective returnedObjective = objectiveBusinessService.updateObjective(newObjective);
        assertEquals("Hello World", returnedObjective.getTitle());
        assertEquals("Best Team", returnedObjective.getTeam().getName());
        assertEquals("rudi", returnedObjective.getCreatedBy().getUsername());
        assertEquals("This is a cool objective", returnedObjective.getDescription());
    }

    @Test
    void shouldDeleteObjectiveAndAssociatedKeyResults() {
        when(this.objectivePersistenceService.findById(anyLong())).thenReturn(objective);
        when(this.keyResultPersistenceService.getKeyResultsByObjective(objective)).thenReturn(keyResults);

        this.objectiveBusinessService.deleteObjectiveById(1L);

        verify(this.keyResultBusinessService, times(3)).deleteKeyResultById(5L);
        verify(this.objectiveBusinessService, times(1)).deleteObjectiveById(anyLong());
    }
}
