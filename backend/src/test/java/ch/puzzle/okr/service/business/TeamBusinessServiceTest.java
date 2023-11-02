package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TeamBusinessServiceTest {
    @MockBean
    TeamPersistenceService teamPersistenceService = Mockito.mock(TeamPersistenceService.class);
    Team team1;
    Team team2;
    Team teamWithIdNull;

    Objective objective;

    List<Objective> objectiveList;

    @Mock
    ObjectiveBusinessService objectiveBusinessService;
    @Mock
    QuarterBusinessService quarterService;
    @InjectMocks
    private TeamValidationService validator = Mockito.mock(TeamValidationService.class);
    @InjectMocks
    private TeamBusinessService teamBusinessService;

    @BeforeEach
    void setUp() {
        this.team1 = Team.Builder.builder().withId(1L).withName("Team 1").build();
        this.team2 = Team.Builder.builder().withId(2L).withName("Team 2").build();
        this.teamWithIdNull = Team.Builder.builder().withName("Team with id null").build();
        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
        this.objectiveList = List.of(objective, objective, objective);
    }

    @Test
    void getTeamByIdShouldBeSuccessful() {
        Long id = team1.getId();
        Mockito.when(teamPersistenceService.findById(id)).thenReturn(team1);

        Team team = teamBusinessService.getTeamById(id);

        assertEquals(team1, team);
    }

    @Test
    void getAllTeamsShouldBeSuccessful() {
        Mockito.when(teamPersistenceService.findAll()).thenReturn(List.of(team1, team2));

        List<Team> teams = teamBusinessService.getAllTeams();

        assertThat(List.of(team1, team2)).hasSameElementsAs(teams);
    }

    @Test
    void activeObjectivesAmountOfTeamShouldBeSuccessful() throws ResponseStatusException {
        Quarter quarter = Quarter.Builder.builder().withLabel("GJ 23/24-Q1").build();
        Mockito.when(quarterService.getQuarterById(any())).thenReturn(quarter);
        Mockito.when(objectiveBusinessService.activeObjectivesAmountOfTeam(team1, quarter)).thenReturn(69);
        assertEquals(69, teamBusinessService.activeObjectivesAmountOfTeam(team1, 1L));
    }
}
