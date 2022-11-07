package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @MockBean
    TeamRepository teamRepository = Mockito.mock(TeamRepository.class);

    @InjectMocks
    private TeamService teamService;

    Team teamPuzzle;
    List<Team> teamsPuzzle;

    @BeforeEach
    void setUp() {
        this.teamPuzzle = Team.Builder.builder()
                .withId(5L).
                withName("Puzzle")
                .build();
        this.teamsPuzzle = List.of(teamPuzzle, teamPuzzle, teamPuzzle);
    }

    @Test
    void shouldGetAllTeams() throws ResponseStatusException {
        Mockito.when(teamRepository.findAll()).thenReturn(teamsPuzzle);

        List<Team> teams = teamService.getAllTeams();

        assertEquals(3 ,teams.size());
        assertEquals("Puzzle", teams.get(0).getName());
    }

    @Test
    void shouldReturnEmptyListWhenNoTeam() {
        Mockito.when(teamRepository.findAll()).thenReturn(Collections.emptyList());

        List<Team> teams = teamService.getAllTeams();

        assertEquals(0 ,teams.size());
    }

    @Test
    void shouldGetTheTeam() throws ResponseStatusException {
        Mockito.when(teamRepository.findById(5L)).thenReturn(Optional.of(teamPuzzle));

        Team team = teamService.getTeamById(5);
        Assertions.assertThat(team.getName()).isEqualTo("Puzzle");
    }

    @Test
    void shouldNotFindTheTeam() {
        Mockito.when(teamRepository.findById(6L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> teamService.getTeamById(6));
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

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            teamService.saveTeam(team);
        });
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

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            teamService.saveTeam(team);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(("Missing attribute name when creating team"), exception.getReason());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void shouldNotCreateTeamWithEmptyName(String passedName) {
        Team team = Team.Builder.builder().withName(passedName).build();
        Mockito.when(teamRepository.save(any())).thenReturn(team);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            teamService.saveTeam(team);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(("Missing attribute name when creating team"), exception.getReason());
    }
}