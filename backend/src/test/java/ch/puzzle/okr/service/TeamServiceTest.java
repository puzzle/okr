package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.service.persistance.TeamPersistenceService;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @MockBean
    TeamPersistenceService teamPersistenceService = Mockito.mock(TeamPersistenceService.class);
    Team team1;
    Team team2;
    Team teamWithIdNull;

    Objective objective;

    List<Objective> objectiveList;

    @MockBean
    ObjectiveRepository objectiveRepository = Mockito.mock(ObjectiveRepository.class);
    @Mock
    ObjectiveService objectiveService;
    @Mock
    QuarterService quarterService;
    @InjectMocks
    private TeamValidationService validator = Mockito.mock(TeamValidationService.class);;
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
        this.team1 = Team.Builder.builder().withId(1L).withName("Team 1").build();
        this.team2 = Team.Builder.builder().withId(2L).withName("Team 2").build();
        this.teamWithIdNull = Team.Builder.builder().withName("Team with id null").build();
        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
        this.objectiveList = List.of(objective, objective, objective);
    }

    @Test
    void getAllTeams_ShouldBeSuccessful() {
        Mockito.when(teamPersistenceService.findAll()).thenReturn(List.of(team1, team2));

        List<Team> allTeams = teamService.getAllTeams();

        Assertions.assertIterableEquals(List.of(team1, team2), allTeams);
    }

    @Test
    void updateTeam_ShouldBeSuccessful() throws ResponseStatusException {
        Mockito.when(teamPersistenceService.save(team1)).thenReturn(team1);
        Mockito.doNothing().when(validator).validateOnUpdate(1L, teamWithIdNull);

        teamService.updateTeam(1L, team1);
        Mockito.verify(validator, Mockito.times(1)).validateOnUpdate(1L, team1);
        Mockito.verify(teamPersistenceService, Mockito.times(1)).save(team1);
    }

    @Test
    void createTeam_ShouldBeSuccessful() throws ResponseStatusException {
        Mockito.when(teamPersistenceService.save(teamWithIdNull)).thenReturn(team1);
        Mockito.doNothing().when(validator).validateOnCreate(teamWithIdNull);

        Team team = teamService.createTeam(teamWithIdNull);
        Mockito.verify(validator, Mockito.times(1)).validateOnCreate(teamWithIdNull);
        Mockito.verify(teamPersistenceService, Mockito.times(1)).save(teamWithIdNull);
        Assertions.assertEquals(1L, team.getId());
    }

    @Test
    void createTeam_ShouldThrowExceptionIfTeamHasId() throws ResponseStatusException {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Model Team cannot have id while create. Found id 1")).when(validator).validateOnCreate(team1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamService.createTeam(team1));

        assertEquals("Model Team cannot have id while create. Found id 1", exception.getReason());
    }

    @Test
    void deleteTeamWithId_ShouldBeSuccessful() throws ResponseStatusException {
        Mockito.doNothing().when(teamPersistenceService).deleteById(1L);
        teamService.deleteTeamById(1L);
        Mockito.verify(validator, Mockito.times(1)).validateOnDelete(1L);
        Mockito.verify(teamPersistenceService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void activeObjectivesAmountOfTeam_ShouldBeSuccessful() throws ResponseStatusException {
        Quarter quarter = Quarter.Builder.builder().withLabel("GJ 23/24-Q1").build();
        Mockito.when(quarterService.getQuarter(ArgumentMatchers.any())).thenReturn("GJ 23/24-Q1");
        Mockito.when(quarterService.getOrCreateQuarter("GJ 23/24-Q1")).thenReturn(quarter);
        Mockito.when(objectiveService.activeObjectivesAmountOfTeam(team1, quarter)).thenReturn(69);
        assertEquals(69, teamService.activeObjectivesAmountOfTeam(team1));
    }
}