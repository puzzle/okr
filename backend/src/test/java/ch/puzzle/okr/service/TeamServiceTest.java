package ch.puzzle.okr.service;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @MockBean
    TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
    Team teamPuzzle;
    Team team1;
    Team team2;

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
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenPuttingId() {
        Team team = Team.Builder.builder().withId(2L).withName("TestTeam").build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamService.saveTeam(team));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Not allowed to give an id", exception.getReason());
    }

    @Test
    void shouldNotThrowResponseStatusExceptionWhenPuttingNullId() {
        Team team = Team.Builder.builder().withId(null).withName("TestTeam").build();
        Mockito.when(teamRepository.save(any())).thenReturn(team);

        Team savedTeam = teamService.saveTeam(team);
        assertNull(savedTeam.getId());
        assertEquals("TestTeam", savedTeam.getName());
    }

    @Test
    void shouldNotCreateTeamWithNoName() {
        Team team = Team.Builder.builder().build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamService.saveTeam(team));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(("Missing attribute name when creating team"), exception.getReason());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " ", "  " })
    void shouldNotCreateTeamWithEmptyName(String passedName) {
        Team team = Team.Builder.builder().withName(passedName).build();
        Mockito.when(teamRepository.save(any())).thenReturn(team);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamService.saveTeam(team));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(("Missing attribute name when creating team"), exception.getReason());
    }

    @Test
    void shouldUpdateTeamProperly() {
        Team team = Team.Builder.builder().withId(1L).withName("New Team").build();
        Mockito.when(teamRepository.save(any())).thenReturn(team);
        Mockito.when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        Team returnedTeam = teamService.updateTeam(1L, team);
        assertEquals("New Team", returnedTeam.getName());
        assertEquals(1L, returnedTeam.getId());
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
}