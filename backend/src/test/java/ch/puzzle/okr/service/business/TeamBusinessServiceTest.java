package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.CacheService;
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

import java.util.ArrayList;
import java.util.List;

import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.test.TestHelper.defaultUser;
import static ch.puzzle.okr.models.State.DRAFT;
import static ch.puzzle.okr.models.State.SUCCESSFUL;
import static ch.puzzle.okr.models.authorization.AuthorizationRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamBusinessServiceTest {
    @MockBean
    TeamPersistenceService teamPersistenceService = Mockito.mock(TeamPersistenceService.class);
    Team team1;
    Team team2;
    Team teamWithIdNull;

    Objective objective;
    Objective objectiveCompleted;

    List<Objective> objectiveList;

    @Mock
    CacheService cacheService;

    @Mock
    ObjectiveBusinessService objectiveBusinessService;
    @InjectMocks
    private TeamValidationService validator = Mockito.mock(TeamValidationService.class);
    @InjectMocks
    private TeamBusinessService teamBusinessService;

    @BeforeEach
    void setUp() {
        this.team1 = Team.Builder.builder().withId(1L).withName("Team 1").build();
        this.team2 = Team.Builder.builder().withId(2L).withName("Team 2").build();
        this.teamWithIdNull = Team.Builder.builder().withName("Team with id null").build();
        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").withState(DRAFT).build();
        this.objectiveCompleted = Objective.Builder.builder().withId(6L).withTitle("Objective 1").withState(SUCCESSFUL)
                .build();
        this.objectiveList = List.of(objective, objective, objective, objectiveCompleted);

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

        List<Team> teams = teamBusinessService.getAllTeams(defaultAuthorizationUser());

        assertThat(List.of(team1, team2)).hasSameElementsAs(teams);
    }

    @Test
    void getAllTeamsSortedShouldReturnSortedListFirstLevelTeamsFirst() {
        Team firstLevelTeam1 = Team.Builder.builder().withId(9L).withName("GLTEAM1").withVersion(1).build();
        Team firstLevelTeam2 = Team.Builder.builder().withId(5L).withName("GLTEAM2").withVersion(1).build();
        Team userTeam = Team.Builder.builder().withId(1L).withName("UserTeam").withVersion(1).build();
        Team notUserTeam = Team.Builder.builder().withId(2L).withName("NOTUserTeam").withVersion(1).build();
        List<Team> teams = List.of(firstLevelTeam1, firstLevelTeam2, userTeam, notUserTeam);
        List<Long> userTeamIds = List.of(1L);
        List<Long> firstLevelTeamIds = List.of(9L, 5L);
        User user = defaultUser(13L);
        AuthorizationUser authUser = new AuthorizationUser(
                User.Builder.builder().withId(user.getId()).withUsername(user.getUsername())
                        .withFirstname(user.getFirstname()).withLastname(user.getLastname()).withEmail(user.getEmail())
                        .build(),
                userTeamIds, firstLevelTeamIds, List.of(READ_ALL_PUBLISHED, READ_ALL_DRAFT, WRITE_ALL));
        when(teamPersistenceService.findAll()).thenReturn(teams);

        List<Team> sortedList = teamBusinessService.getAllTeams(authUser);
        assertEquals(List.of(userTeam, firstLevelTeam1, firstLevelTeam2, notUserTeam), sortedList);
    }

    @Test
    void shouldSaveANewTeam() {
        Team team = Team.Builder.builder().withName("OKR-TEAM").withAuthorizationOrganisation(new ArrayList<>())
                .build();
        teamBusinessService.createTeam(team);
        verify(teamPersistenceService, times(1)).save(team);
        verify(cacheService, times(1)).emptyAuthorizationUsersCache();
    }

    @Test
    void shouldUpdateTeam() {
        Team team = Team.Builder.builder().withId(1L).withName("OKR-TEAM")
                .withAuthorizationOrganisation(new ArrayList<>()).build();
        teamBusinessService.updateTeam(team, team.getId());
        verify(teamPersistenceService, times(1)).save(team);
        verify(cacheService, times(1)).emptyAuthorizationUsersCache();
    }

    @Test
    void shouldDeleteTeamAndItsObjectives() {
        when(objectiveBusinessService.getEntitiesByTeamId(1L)).thenReturn(objectiveList);
        teamBusinessService.deleteTeam(1L);

        verify(teamPersistenceService, times(1)).deleteById(1L);
        verify(objectiveBusinessService, times(objectiveList.size())).deleteEntityById(anyLong());
        verify(cacheService, times(1)).emptyAuthorizationUsersCache();
    }
}
