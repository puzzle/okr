package ch.puzzle.okr.service.business;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.CacheService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.service.persistence.UserTeamPersistenceService;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static ch.puzzle.okr.test.TestHelper.*;
import static ch.puzzle.okr.models.State.DRAFT;
import static ch.puzzle.okr.models.State.SUCCESSFUL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamBusinessServiceTest {
    @MockBean
    TeamPersistenceService teamPersistenceService = Mockito.mock(TeamPersistenceService.class);
    Team team1;
    Team team2;
    Team team3;
    Team teamWithIdNull;

    Objective objective;
    Objective objectiveCompleted;

    List<Objective> objectiveList;

    @Mock
    CacheService cacheService;

    @Mock
    ObjectiveBusinessService objectiveBusinessService;

    @Mock
    UserPersistenceService userPersistenceService;

    @Mock
    UserTeamPersistenceService userTeamPersistenceService;

    @InjectMocks
    private TeamValidationService validator = Mockito.mock(TeamValidationService.class);
    @InjectMocks
    private TeamBusinessService teamBusinessService;

    @BeforeEach
    void setUp() {
        this.team1 = Team.Builder.builder().withId(1L).withName("Team 1").build();
        this.team1.setUserTeamList(List.of(
                UserTeam.Builder.builder().withTeam(team1).withUser(defaultUser(2L)).isTeamAdmin(true).build(),
                UserTeam.Builder.builder().withTeam(team1).withUser(defaultUser(3L)).isTeamAdmin(false).build()));
        this.team2 = Team.Builder.builder().withId(2L).withName("Team 2").build();
        this.team2.setUserTeamList(List.of(
                UserTeam.Builder.builder().withTeam(team2).withUser(defaultUser(4L)).isTeamAdmin(true).build(),
                UserTeam.Builder.builder().withTeam(team2).withUser(defaultUser(5L)).isTeamAdmin(true).build()));
        this.team3 = Team.Builder.builder().withId(3L).withName("Team 3").build();
        this.team3.setUserTeamList(List.of());
        this.teamWithIdNull = Team.Builder.builder().withName("Team with id null").build();
        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").withState(DRAFT).build();
        this.objectiveCompleted = Objective.Builder.builder().withId(6L).withTitle("Objective 1").withState(SUCCESSFUL)
                .build();
        this.objectiveList = List.of(objective, objective, objective, objectiveCompleted);
    }

    @DisplayName("Should return correct team on getTeamById()")
    @Test
    void shouldSuccessfullyGetTeamById() {
        Long id = team1.getId();
        Mockito.when(teamPersistenceService.findById(id)).thenReturn(team1);

        Team team = teamBusinessService.getTeamById(id);

        assertEquals(team1, team);
    }

    @DisplayName("Should return list of all teams on getAllTeams()")
    @Test
    void shouldSuccessfullyGetAllTeams() {
        Mockito.when(teamPersistenceService.findAll()).thenReturn(List.of(team1, team2));

        List<Team> teams = teamBusinessService.getAllTeams(defaultAuthorizationUser());

        assertThat(List.of(team1, team2)).hasSameElementsAs(teams);
    }

    @DisplayName("Should return list of teams with teams that include the users first on getAllTeams()")
    @Test
    void shouldReturnSortedListUserTeamsFirstUsingGetAllTeams() {
        Team userTeam = Team.Builder.builder().withId(1L).withName("UserTeam").withVersion(1).build();
        Team notUserTeam = Team.Builder.builder().withId(2L).withName("NOTUserTeam").withVersion(1).build();
        Team notUserTeam2 = Team.Builder.builder().withId(3L).withName("NOTUserTeam2").withVersion(1).build();
        List<Team> teams = List.of(notUserTeam, userTeam, notUserTeam2);
        User user = defaultUser(13L);
        List<UserTeam> userTeamList = List
                .of(UserTeam.Builder.builder().withUser(user).withTeam(userTeam).withId(1L).build());
        AuthorizationUser authUser = new AuthorizationUser(User.Builder.builder().withId(user.getId())
                .withFirstName(user.getFirstName()).withLastName(user.getLastName()).withEmail(user.getEmail())
                .withUserTeamList(userTeamList).build());
        when(teamPersistenceService.findAll()).thenReturn(teams);

        List<Team> sortedList = teamBusinessService.getAllTeams(authUser);
        assertEquals(List.of(userTeam, notUserTeam, notUserTeam2), sortedList);
    }

    @DisplayName("Should create new team with user as admin on createTeam()")
    @Test
    void shouldCreateANewTeamAndSetCurrentUserAsAdmin() {
        Team team = Team.Builder.builder().withName("OKR-TEAM").withId(2L).build();
        var user = defaultUserWithTeams(1L, List.of(), List.of());

        when(teamPersistenceService.save(team)).thenReturn(team);
        when(teamPersistenceService.findById(team.getId())).thenReturn(team);

        teamBusinessService.createTeam(team, new AuthorizationUser(user));

        verify(teamPersistenceService, times(1)).save(team);
        verify(cacheService, times(1)).emptyAuthorizationUsersCache();
        assertEquals(user.getUserTeamList().size(), 1);
        assertEquals(user.getUserTeamList().get(0).getTeam().getId(), team.getId());
        assertTrue(user.getUserTeamList().get(0).isTeamAdmin());
    }

    @DisplayName("Should update team on updateTeam()")
    @Test
    void shouldUpdateTeam() {
        Team team = Team.Builder.builder().withId(1L).withName("OKR-TEAM").build();
        teamBusinessService.updateTeam(team, team.getId());
        verify(teamPersistenceService, times(1)).save(team);
        verify(cacheService, times(1)).emptyAuthorizationUsersCache();
    }

    @DisplayName("Should delete team and all its objectives on deleteTeam()")
    @Test
    void shouldDeleteTeamAndItsObjectives() {
        var team = defaultTeam(1L);
        team.setUserTeamList(
                List.of(UserTeam.Builder.builder().withTeam(team).withUser(new User()).withId(1L).build()));
        when(objectiveBusinessService.getEntitiesByTeamId(team.getId())).thenReturn(objectiveList);
        when(teamPersistenceService.findById(team.getId())).thenReturn(team);

        teamBusinessService.deleteTeam(team.getId());

        verify(teamPersistenceService, times(1)).deleteById(team.getId());
        verify(objectiveBusinessService, times(objectiveList.size())).deleteEntityById(anyLong());
        verify(cacheService, times(2)).emptyAuthorizationUsersCache();
    }

    @DisplayName("Should add user to teams on addUsersToTeam()")
    @Test
    void shouldSuccessfullyAddUserToTeam() {
        var teamId = 1L;
        var userIds = List.of(1L, 2L);

        var user1 = defaultUserWithTeams(1L, new ArrayList<>(), new ArrayList<>());
        var user2 = defaultUserWithTeams(2L, new ArrayList<>(), new ArrayList<>());

        when(teamPersistenceService.findById(teamId)).thenReturn(defaultTeam(teamId));
        when(userPersistenceService.findById(userIds.get(0))).thenReturn(user1);
        when(userPersistenceService.findById(userIds.get(1))).thenReturn(user2);

        teamBusinessService.addUsersToTeam(teamId, userIds);

        var user1TeamIds = user1.getUserTeamList().stream().map(ut -> ut.getTeam().getId()).toList();
        assertTrue(user1TeamIds.contains(teamId));

        var user2TeamIds = user2.getUserTeamList().stream().map(ut -> ut.getTeam().getId()).toList();
        assertTrue(user2TeamIds.contains(teamId));

        assertFalse(user1TeamIds.contains(3L));
        assertFalse(user2TeamIds.contains(3L));
    }

    @DisplayName("Should remove user from team on removeUserFromTeam()")
    @Test
    void shouldSuccessfullyRemoveUserFromTeam() {
        var user = defaultUserWithTeams(1L, List.of(team1), List.of(team2, team3));
        when(userPersistenceService.findById(user.getId())).thenReturn(user);
        when(teamPersistenceService.findById(team2.getId())).thenReturn(team2);
        team2.setUserTeamList(new ArrayList<>(team2.getUserTeamList()));

        teamBusinessService.removeUserFromTeam(team2.getId(), user.getId());
        assertEquals(2, user.getUserTeamList().size());
        assertEquals(user.getUserTeamList().stream().map(ut -> ut.getTeam().getId()).toList(),
                List.of(team1.getId(), team3.getId()));
        verify(cacheService, times(1)).emptyAuthorizationUsersCache();
    }

    @DisplayName("Should throw exception on removeUserFromTeam() when team does not exist")
    @Test
    void shouldThrowExceptionWhenNoTeamFoundUsingRemoveUserFromTeam() {
        var user = defaultUserWithTeams(1L, List.of(team1), List.of(team3));
        when(userPersistenceService.findById(user.getId())).thenReturn(user);
        when(teamPersistenceService.findById(team1.getId())).thenReturn(team1);

        assertThrows(RuntimeException.class, () -> teamBusinessService.removeUserFromTeam(team2.getId(), user.getId()));
    }

    @DisplayName("Should throw exception on removeUserFromTeam() when user is the last admin")
    @Test
    void shouldThrowExceptionWhenLastAdminGetsRemoved() {
        var user = defaultUserWithTeams(2L, List.of(team1), List.of(team3));
        when(userPersistenceService.findById(user.getId())).thenReturn(user);
        when(teamPersistenceService.findById(team1.getId())).thenReturn(team1);

        assertThrows(OkrResponseStatusException.class,
                () -> teamBusinessService.removeUserFromTeam(team1.getId(), user.getId()),
                ErrorKey.TRIED_TO_DELETE_LAST_ADMIN.toString());
    }

    @DisplayName("Should update team membership on updateOrAddTeamMembership() when the user is already in the team")
    @Test
    void shouldUpdateIfTeamFound() {
        var user = defaultUserWithTeams(1L, List.of(team1), List.of(team2, team3));
        when(userPersistenceService.findById(user.getId())).thenReturn(user);
        teamBusinessService.updateOrAddTeamMembership(team2.getId(), user.getId(), true);
        assertTrue(user.getUserTeamList().get(0).isTeamAdmin());
        assertTrue(user.getUserTeamList().get(1).isTeamAdmin());
        assertFalse(user.getUserTeamList().get(2).isTeamAdmin());
        verify(cacheService, times(1)).emptyAuthorizationUsersCache();
    }

    @DisplayName("Should throw exception on updateOrAddTeamMembership() when the last admin gets his role removed")
    @Test
    void shouldThrowExceptionIfLastAdminRoleIsRemoved() {
        var user = defaultUserWithTeams(1L, List.of(team1), List.of());
        team1.setUserTeamList(new ArrayList<>(user.getUserTeamList()));

        when(userPersistenceService.findById(user.getId())).thenReturn(user);

        assertThrows(OkrResponseStatusException.class,
                () -> teamBusinessService.updateOrAddTeamMembership(team1.getId(), user.getId(), false));
    }

    @DisplayName("Should add user to team on updateOrAddTeamMembership() when user is not already in the team")
    @Test
    void shouldAddTeamIfNoTeamFoundUsingUpdateOrAddTeamMembership() {
        var user = defaultUserWithTeams(1L, List.of(), List.of(team2));
        when(userPersistenceService.findById(user.getId())).thenReturn(user);
        when(teamPersistenceService.findById(team1.getId())).thenReturn(team1);
        when(teamPersistenceService.findById(team3.getId())).thenReturn(team3);

        teamBusinessService.updateOrAddTeamMembership(team1.getId(), user.getId(), true);
        teamBusinessService.updateOrAddTeamMembership(team3.getId(), user.getId(), false);

        assertFalse(user.getUserTeamList().get(0).isTeamAdmin());
        assertEquals(user.getUserTeamList().get(0).getTeam().getId(), team2.getId());

        assertTrue(user.getUserTeamList().get(1).isTeamAdmin());
        assertEquals(user.getUserTeamList().get(1).getTeam().getId(), team1.getId());

        assertFalse(user.getUserTeamList().get(2).isTeamAdmin());
        assertEquals(user.getUserTeamList().get(2).getTeam().getId(), team3.getId());

        assertEquals(user.getUserTeamList().size(), 3);
        verify(cacheService, times(2)).emptyAuthorizationUsersCache();
    }
}
