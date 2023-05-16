package ch.puzzle.okr.service;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectiveServiceTest {
    @MockBean
    ObjectiveRepository objectiveRepository = Mockito.mock(ObjectiveRepository.class);
    @MockBean
    KeyResultRepository keyResultRepository = Mockito.mock(KeyResultRepository.class);
    @MockBean
    KeyResultService keyResultService = Mockito.mock(KeyResultService.class);
    @MockBean
    TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
    @MockBean
    MeasureRepository measureRepository = Mockito.mock(MeasureRepository.class);

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
    private ObjectiveService objectiveService;

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
        this.fullObjective1 = Objective.Builder.builder().withTitle("FullObjective1").withOwner(user).withTeam(team1)
                .withQuarter(quarter).withDescription("This is our description").withProgress(null)
                .withModifiedOn(LocalDateTime.MAX).build();
        this.fullObjective2 = Objective.Builder.builder().withTitle("FullObjective2").withOwner(user).withTeam(team1)
                .withQuarter(quarter).withDescription("This is our description").withProgress(33L)
                .withModifiedOn(LocalDateTime.MAX).build();
        this.fullObjective3 = Objective.Builder.builder().withId(5L).withTitle("FullObjective1").withOwner(user)
                .withTeam(team1).withQuarter(quarter).withDescription("This is our description").withProgress(null)
                .withModifiedOn(LocalDateTime.MAX).build();
        this.fullObjectiveInTeam1List = List.of(fullObjective1, fullObjective2);

    }

    @Test
    void shouldGetAllObjectives() {
        when(objectiveRepository.findAll()).thenReturn(objectiveList);

        List<Objective> objectives = objectiveService.getAllObjectives();

        assertEquals(3, objectives.size());
        assertEquals("Objective 1", objectives.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNoObjective() {
        when(objectiveRepository.findAll()).thenReturn(Collections.emptyList());

        List<Objective> objectives = objectiveService.getAllObjectives();

        assertEquals(0, objectives.size());
    }

    @Test
    void getOneObjective() {
        Mockito.when(objectiveRepository.findById(5L)).thenReturn(Optional.ofNullable(this.objective));
        Objective realObjective = objectiveService.getObjective(5L);

        assertEquals("Objective 1", realObjective.getTitle());
    }

    @Test
    void shouldNotFindTheObjective() {
        Mockito.when(objectiveRepository.findById(6L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveService.getObjective(6L));
        assertEquals(404, exception.getRawStatusCode());
        assertEquals("Objective with id 6 not found", exception.getReason());
    }

    @Test
    void shouldSaveANewObjective() {
        Mockito.when(objectiveRepository.save(any())).thenReturn(fullObjective1);

        Objective savedObjective = objectiveService.saveObjective(fullObjective1);
        assertNull(savedObjective.getId());
        assertEquals("FullObjective1", savedObjective.getTitle());
        assertEquals("This is our description", savedObjective.getDescription());
        assertNull(savedObjective.getProgress());
        assertEquals("Team1", savedObjective.getTeam().getName());
        assertEquals("Bob", savedObjective.getOwner().getFirstname());
        assertEquals("GJ 22/23-Q2", savedObjective.getQuarter().getLabel());
        assertEquals(LocalDateTime.MAX, savedObjective.getModifiedOn());
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenPuttingIdCreatingObjective() {
        Objective objective1 = Objective.Builder.builder().withId(9L).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveService.saveObjective(objective1));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Not allowed to give an id", exception.getReason());

    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenPuttingNullId() {
        Objective objective1 = Objective.Builder.builder().withId(null).withTitle("Title")
                .withDescription("Description").withProgress(null).withModifiedOn(LocalDateTime.now()).build();
        Mockito.when(objectiveRepository.save(any())).thenReturn(this.fullObjective1);

        Objective savedObjective = objectiveService.saveObjective(objective1);
        assertNull(savedObjective.getId());
        assertEquals("FullObjective1", savedObjective.getTitle());
        assertNull(savedObjective.getProgress());
        assertEquals("Bob", savedObjective.getOwner().getFirstname());
    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenCreatingObjectiveWithEmptyDescription() {
        this.fullObjective1.setDescription(null);
        Objective objective1 = Objective.Builder.builder().withId(null).withTitle("Title").withProgress(null)
                .withModifiedOn(LocalDateTime.now()).build();
        Mockito.when(objectiveRepository.save(any())).thenReturn(this.fullObjective1);

        Objective savedObjective = objectiveService.saveObjective(objective1);
        assertEquals("FullObjective1", savedObjective.getTitle());
        assertNull(savedObjective.getDescription());
        assertEquals("Bob", savedObjective.getOwner().getFirstname());
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenCreatingObjectiveWithEmptyCreatedOn() {
        this.fullObjective1.setModifiedOn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveService.saveObjective(this.fullObjective1));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(("Failed to generate attribute createdOn when creating objective"), exception.getReason());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " ", "  " })
    void shouldNotCreateObjectiveWithEmptyTitle(String passedName) {
        Objective objective1 = Objective.Builder.builder().withTitle(passedName).build();
        Mockito.when(objectiveRepository.save(any())).thenReturn(objective1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveService.saveObjective(objective1));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(("Missing attribute title when creating objective"), exception.getReason());
    }

    @Test
    void shouldReturnObjectiveProperly() {
        Objective newObjective = Objective.Builder.builder().withTitle("Hello World").withId(1L)
                .withDescription("This is a cool objective")
                .withOwner(User.Builder.builder().withUsername("rudi").build()).withProgress(5L).withQuarter(null)
                .withModifiedOn(LocalDateTime.now()).withQuarter(this.fullObjective1.getQuarter())
                .withTeam(Team.Builder.builder().withId(1L).withName("Best Team").build()).build();
        Mockito.when(objectiveRepository.findById(anyLong())).thenReturn(Optional.of(newObjective));
        Mockito.when(objectiveRepository.save(any())).thenReturn(newObjective);

        Objective returnedObjective = objectiveService.updateObjective(newObjective);
        assertEquals("Hello World", returnedObjective.getTitle());
        assertEquals("Best Team", returnedObjective.getTeam().getName());
        assertEquals("rudi", returnedObjective.getOwner().getUsername());
        assertEquals("This is a cool objective", returnedObjective.getDescription());
    }

    @Test
    void shouldSetQuarterIsImmutable() {
        Objective objective = this.fullObjective3;
        this.keyResult.setObjective(objective);
        keyResultService.updateKeyResult(this.keyResult);
        when(objectiveRepository.findById(objective.getId())).thenReturn(Optional.of(objective));
        assertFalse(objectiveService.isQuarterImmutable(objective));

        Measure measure = Measure.Builder.builder().withId(5L).withKeyResult(keyResult).withValue(0.0)
                .withMeasureDate(Instant.MAX).withInitiatives("Initiatives").withCreatedBy(user)
                .withChangeInfo("changeInfo").withCreatedOn(LocalDateTime.MAX).build();
        List<Measure> measures = new ArrayList<>();
        measures.add(measure);
        when(keyResultService.getLastMeasures(objective.getId())).thenReturn(measures);

        // quarter needs to be different only checking the measures is not sufficient
        // because we only want to restrict a possible change of the quarter and not for every change on the objective
        assertFalse(objectiveService.isQuarterImmutable(objective));

        Objective newObjective = Objective.Builder.builder().withId(objective.getId())
                .withQuarter(Quarter.Builder.builder().withId(8L).withLabel("GJ 22/23-Q4").build())
                .withTitle(objective.getTitle()).withOwner(objective.getOwner()).withProgress(objective.getProgress())
                .withTeam(objective.getTeam()).withModifiedOn(objective.getModifiedOn()).build();
        when(objectiveRepository.findById(newObjective.getId())).thenReturn(Optional.of(objective));

        assertTrue(objectiveService.isQuarterImmutable(newObjective));

    }

    @Test
    void shouldThrowNotFoundException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> this.objectiveService.getObjectivesByTeam(13L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(("Could not find team with id 13"), exception.getReason());
    }

    @Test
    void shouldReturnObjectiveByTeamId() {
        Mockito.when(teamRepository.findById(1L)).thenReturn(Optional.ofNullable(this.team1));
        Mockito.when(objectiveRepository.findByTeamIdOrderByTitleAsc(1L)).thenReturn(this.fullObjectiveInTeam1List);

        List<Objective> realObjectiveList = objectiveService.getObjectivesByTeam(1L);

        assertEquals(2, realObjectiveList.size());
        assertEquals("FullObjective1", realObjectiveList.get(0).getTitle());
        assertEquals("FullObjective2", realObjectiveList.get(1).getTitle());
    }

    private static Stream<Arguments> shouldReturnObjectives() {
        return Stream.of(Arguments.of(1L, 1L, 0, 1), Arguments.of(1L, null, 1, 0));
    }

    @ParameterizedTest
    @MethodSource
    void shouldReturnObjectives(Long teamId, Long quarterId, int invocationsByTeam, int invocationsByTeamAndQuarter) {
        objectiveService.getObjectiveByTeamIdAndQuarterId(teamId, quarterId);
        verify(objectiveRepository, times(invocationsByTeam)).findByTeamIdOrderByTitleAsc(teamId);
        verify(objectiveRepository, times(invocationsByTeamAndQuarter))
                .findByQuarterIdAndTeamIdOrderByModifiedOnDesc(teamId, quarterId);
    }

    @Test
    void shouldDeleteObjectiveAndAssociatedKeyResults() {
        when(this.objectiveRepository.findById(anyLong())).thenReturn(Optional.of(objective));
        when(this.keyResultRepository.findByObjectiveOrderByModifiedOnDesc(objective)).thenReturn(keyResults);

        this.objectiveService.deleteObjectiveById(1L);

        verify(this.keyResultService, times(3)).deleteKeyResultById(5L);
        verify(this.objectiveRepository, times(1)).deleteById(anyLong());
    }
}
