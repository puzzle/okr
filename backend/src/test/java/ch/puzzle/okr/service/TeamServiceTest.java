package ch.puzzle.okr.service;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.TeamRepository;
import org.assertj.core.api.Assertions;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @MockBean
    TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
    Team teamPuzzle;
    Team team1;
    Team team2;

    Objective objective;

    List<Objective> objectiveList;

    @MockBean
    ObjectiveRepository objectiveRepository = Mockito.mock(ObjectiveRepository.class);
    @Mock
    ObjectiveService objectiveService;
    @Spy
    private ValidationService validationService;
    @InjectMocks
    private TeamService teamService;

    private static Stream<Arguments> shouldGetTeamsByIds() {
        return Stream.of(Arguments.of(List.of(1L, 2L),
                List.of(Team.Builder.builder().withId(1L).build(), Team.Builder.builder().withId(2L).build()), 2

        ), Arguments.of(List.of(1L, 5L), List.of(Team.Builder.builder().withId(1L).build()), 2

        ), Arguments.of(List.of(5L), Collections.emptyList(), 1));
    }

    @BeforeEach
    void setUp() {
        this.teamPuzzle = Team.Builder.builder().withId(5L).withName(Constants.TEAM_PUZZLE).build();
        this.team1 = Team.Builder.builder().withName("Team 1").build();
        this.team2 = Team.Builder.builder().withName("Team 2").build();
        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
        this.objectiveList = List.of(objective, objective, objective);
    }

    @ParameterizedTest
    @MethodSource
    void shouldGetTeamsByIds(List<Long> teamIds, List<Team> teamList, int expectedTeamsAmount) {
        Mockito.when(teamRepository.findByName(Constants.TEAM_PUZZLE)).thenReturn(Optional.of(teamPuzzle));
        Mockito.when(teamRepository.findAllByIdInAndNameNotOrderByNameAsc(teamIds, Constants.TEAM_PUZZLE))
                .thenReturn(teamList);
        assertEquals(expectedTeamsAmount, teamService.getAllTeams(teamIds).size());
    }

    @Test
    void shouldGetAllTeams() throws ResponseStatusException {
        Mockito.when(teamRepository.findAllByNameNotOrderByNameAsc(Constants.TEAM_PUZZLE))
                .thenReturn(List.of(team1, team2));
        Mockito.when(teamRepository.findByName(Constants.TEAM_PUZZLE)).thenReturn(Optional.of(teamPuzzle));

        List<Team> teams = teamService.getAllTeams();

        assertEquals(3, teams.size());
        assertEquals(Constants.TEAM_PUZZLE, teams.get(0).getName());
    }

    @Test
    void shouldReturnEmptyListWhenNoTeam() {
        Mockito.when(teamRepository.findAll()).thenReturn(Collections.emptyList());

        List<Team> teams = teamService.getAllTeams();

        assertEquals(0, teams.size());
    }

    @Test
    void shouldGetTheTeam() throws ResponseStatusException {
        Mockito.when(teamRepository.findById(5L)).thenReturn(Optional.of(teamPuzzle));
        Team team = teamService.getTeamById(5);
        Assertions.assertThat(team.getName()).isEqualTo(Constants.TEAM_PUZZLE);
    }

    @Test
    void shouldNotFindTheTeam() {
        Mockito.when(teamRepository.findById(6L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamService.getTeamById(6));
        assertEquals(404, exception.getRawStatusCode());
        assertEquals("Team with id 6 not found", exception.getReason());
    }

    @Test
    void shouldSaveANewTeam() {
        Team team = Team.Builder.builder().withName("TestTeam").build();
        Mockito.when(teamRepository.save(any())).thenReturn(team);

        Team savedTeam = teamService.saveTeam(team);
        assertNull(savedTeam.getId());
        assertEquals("TestTeam", savedTeam.getName());
        verify(validationService, times(1)).validateOnSave(team);
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenPuttingId() {
        Team team = Team.Builder.builder().withId(2L).withName("TestTeam").build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamService.saveTeam(team));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Not allowed to give an id", exception.getReason());
        verify(validationService, times(1)).validateOnSave(team);
    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenPuttingNullId() {
        Team team = Team.Builder.builder().withId(null).withName("TestTeam").build();
        Mockito.when(teamRepository.save(any())).thenReturn(team);

        Team savedTeam = teamService.saveTeam(team);
        assertNull(savedTeam.getId());
        assertEquals("TestTeam", savedTeam.getName());
        verify(validationService, times(1)).validateOnSave(team);
    }

    @Test
    void shouldNotCreateTeamWithNoName() {
        Team team = Team.Builder.builder().build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamService.saveTeam(team));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getReason().contains("Missing attribute name when saving team."));
        assertTrue(exception.getReason().contains("Attribute name can not be null when saving team."));
        verify(validationService, times(1)).validateOnSave(team);
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " ", "  " })
    void shouldNotCreateTeamWithEmptyName(String passedName) {
        Team team = Team.Builder.builder().withName(passedName).build();
        Mockito.when(teamRepository.save(any())).thenReturn(team);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamService.saveTeam(team));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getReason().contains("Missing attribute name when saving team."));
        verify(validationService, times(1)).validateOnSave(team);
    }

    @Test
    void shouldUpdateTeamProperly() {
        Team team = Team.Builder.builder().withId(1L).withName("New Team").build();
        Mockito.when(teamRepository.save(any())).thenReturn(team);
        Mockito.when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        Team returnedTeam = teamService.updateTeam(1L, team);
        assertEquals("New Team", returnedTeam.getName());
        assertEquals(1L, returnedTeam.getId());
        verify(validationService, times(1)).validateOnUpdate(team);
    }

    @Test
    void shouldThrowNotFoundException() {
        Mockito.when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());
        Team team = Team.Builder.builder().withId(1L).withName("New Team").build();
        assertThrows(ResponseStatusException.class, () -> teamService.updateTeam(1L, team));
    }

    @Test
    void shouldNotUpdateTeamWithEmptyName() {
        Team team = Team.Builder.builder().withId(1L).withName("").build();
        assertThrows(ResponseStatusException.class, () -> teamService.updateTeam(1L, team));
        verify(validationService, times(1)).validateOnUpdate(team);
    }

    @Test
    void shouldReturnSingleTeamWhenFindingByValidId() {
        Optional<Team> team = Optional.of(Team.Builder.builder().withId(1L).withName("Team1").build());
        Mockito.when(teamRepository.findById(any())).thenReturn(team);

        Team returnedTeam = teamService.getTeamById(1L);

        assertEquals(1L, returnedTeam.getId());
        assertEquals("Team1", returnedTeam.getName());
    }

    @Test
    void shouldThrowReponseStatusExceptionWhenFindingTeamNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamService.getTeamById(422L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Team with id 422 not found", exception.getReason());
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenGetTeamWithNullId() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamService.getTeamById(null));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing attribute team id", exception.getReason());
    }

    @Test
    void shouldDeleteObjectiveAndAssociatedKeyResults() {
        when(this.teamRepository.findById(anyLong())).thenReturn(Optional.of(team1));
        when(this.objectiveRepository.findByTeamIdOrderByTitleAsc(anyLong())).thenReturn(objectiveList);

        this.teamService.deleteTeamById(1L);

        verify(this.objectiveService, times(3)).deleteObjectiveById(5L);
        verify(this.teamRepository, times(1)).deleteById(anyLong());
    }
}