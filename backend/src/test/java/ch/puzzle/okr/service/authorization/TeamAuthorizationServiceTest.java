package ch.puzzle.okr.service.authorization;

import static ch.puzzle.okr.test.TestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.TeamBusinessService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class TeamAuthorizationServiceTest {

    @Mock
    TeamBusinessService teamBusinessService;
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    private TeamAuthorizationService teamAuthorizationService;

    private final AuthorizationUser okrChampionUser = new AuthorizationUser(defaultOkrChampion(1L));
    private final Team teamUnderTest = Team.Builder.builder().withId(5L).withName("Team").build();
    private final AuthorizationUser adminUser = new AuthorizationUser(defaultUserWithTeams(1L,
                                                                                           List.of(teamUnderTest),
                                                                                           List.of()));
    private final AuthorizationUser memberUser = new AuthorizationUser(defaultUserWithTeams(1L,
                                                                                            List.of(),
                                                                                            List.of(teamUnderTest)));
    private final AuthorizationUser userWithNoTeams = new AuthorizationUser(defaultUserWithTeams(1L,
                                                                                                 List.of(),
                                                                                                 List.of()));

    @DisplayName("Should return the created team")
    @Test
    void shouldReturnCreatedTeam() {
        when(teamBusinessService.createTeam(teamUnderTest, authorizationService.updateOrAddAuthorizationUser()))
                .thenReturn(teamUnderTest);

        Team team = teamAuthorizationService.createEntity(teamUnderTest);
        assertEquals(teamUnderTest, team);
    }

    @DisplayName("Should return the updated team when authorized as okr-champion")
    @Test
    void shouldReturnUpdatedTeamWhenAuthorizedAsOkrChampion() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(okrChampionUser);
        when(teamBusinessService.updateTeam(teamUnderTest, id)).thenReturn(teamUnderTest);

        Team team = teamAuthorizationService.updateEntity(teamUnderTest, id);
        assertEquals(teamUnderTest, team);
    }

    @DisplayName("Should return the updated team when authorized as admin-user")
    @Test
    void shouldReturnUpdatedTeamWhenAuthorizedAsAdminUser() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(adminUser);
        when(teamBusinessService.updateTeam(teamUnderTest, id)).thenReturn(teamUnderTest);

        Team team = teamAuthorizationService.updateEntity(teamUnderTest, id);
        assertEquals(teamUnderTest, team);
    }

    @DisplayName("Should throw an exception when the user is authorized as a member")
    @Test
    void shouldThrowExceptionWhenAuthorizedAsMemberUser() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(memberUser);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> teamAuthorizationService
                                                                 .updateEntity(teamUnderTest, id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_WRITE", exception.getReason());
    }

    @DisplayName("Should throw an exception when the user is authorized as a user with no teams")
    @Test
    void shouldThrowExceptionWhenAuthorizedAsUserWithNoTeams() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(userWithNoTeams);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> teamAuthorizationService
                                                                 .updateEntity(teamUnderTest, id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_WRITE", exception.getReason());
    }

    @DisplayName("Should successfully delete when authorized as an okr-champion")
    @Test
    void shouldDeleteSuccessfullyWhenAuthorizedAsOkrChampion() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(okrChampionUser);
        teamAuthorizationService.deleteEntity(teamUnderTest.getId());
    }

    @DisplayName("Should successfully delete when authorized as a team-admin")
    @Test
    void shouldDeleteSuccessfullyWhenAuthorizedAsTeamAdmin() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(adminUser);
        teamAuthorizationService.deleteEntity(teamUnderTest.getId());
    }

    @DisplayName("Should throw an exception when the user is authorized as a member")
    @Test
    void shouldThrowExceptionWhenAuthorizedAsMemberUserForDeletion() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(memberUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamAuthorizationService.deleteEntity(teamUnderTest.getId()));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_DELETE", exception.getReason());
    }

    @DisplayName("Should throw an exception when the user is authorized as a user with no teams for deletion")
    @Test
    void shouldThrowExceptionWhenAuthorizedAsUserWithNoTeamsForDeletion() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(userWithNoTeams);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> teamAuthorizationService.deleteEntity(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_DELETE", exception.getReason());
    }

    @ParameterizedTest(name = "Should return all teams with writable={0}")
    @ValueSource(booleans = { true, false })
    void shouldReturnAllTeamsWithWritableFlag(boolean isWriteable) {
        List<Team> teamList = List.of(teamUnderTest, teamUnderTest);
        if (isWriteable) {
            when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(okrChampionUser);
        } else {
            when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(defaultAuthorizationUser());
        }
        when(teamBusinessService.getAllTeams(any())).thenReturn(teamList);

        List<Team> teams = teamAuthorizationService.getAllTeams();
        assertEquals(teamList, teams);
        teams.forEach(team -> assertEquals(isWriteable, team.isWriteable()));
    }

    @DisplayName("Should throw an exception if the user is not authorized to add users to a team")
    @Test
    void shouldThrowExceptionIfUserNotAuthorizedToAddUsersToTeam() {
        when(authorizationService.updateOrAddAuthorizationUser())
                .thenReturn(new AuthorizationUser(defaultUserWithTeams(1L, List.of(), List.of())));
        assertThrows(OkrResponseStatusException.class, () -> teamAuthorizationService.addUsersToTeam(1L, List.of()));
    }

    @DisplayName("Should call the teamBusinessService when adding users to a team")
    @Test
    void shouldCallTeamBusinessServiceWhenAddingUsersToTeam() {
        var adminTeamId = 1L;
        var adminTeam = defaultTeam(adminTeamId);
        var usersList = List.of(1L, 2L);
        when(authorizationService.updateOrAddAuthorizationUser())
                .thenReturn(new AuthorizationUser(defaultUserWithTeams(1L, List.of(adminTeam), List.of())));
        teamAuthorizationService.addUsersToTeam(adminTeamId, usersList);
        verify(teamBusinessService, times(1)).addUsersToTeam(adminTeamId, usersList);
    }
}
