package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
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

        List<Team> teams = teamBusinessService.getAllTeams();

        assertIterableEquals(List.of(team1, team2), teams);
    }

    @Test
    void activeObjectivesAmountOfTeam_ShouldBeSuccessful() throws ResponseStatusException {
        Quarter quarter = Quarter.Builder.builder().withLabel("GJ 23/24-Q1").build();
        Mockito.when(quarterService.getQuarterById(any())).thenReturn(quarter);
        Mockito.when(objectiveBusinessService.activeObjectivesAmountOfTeam(team1, quarter)).thenReturn(69);
        assertEquals(69, teamBusinessService.activeObjectivesAmountOfTeam(team1, 1L));
    }
}
