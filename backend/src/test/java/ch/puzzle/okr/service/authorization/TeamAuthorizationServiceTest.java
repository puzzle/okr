package ch.puzzle.okr.service.authorization;

import java.util.List;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.TeamBusinessService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static ch.puzzle.okr.test.TestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class TeamAuthorizationServiceTest {

    @Mock
    TeamBusinessService teamBusinessService;
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    private TeamAuthorizationService teamAuthorizationService;

    private final AuthorizationUser okrChampionUser = new AuthorizationUser(defaultOkrChampion(1L));
    private final Team teamUnderTest = Team.Builder.builder()
                                                   .withId(5L)
                                                   .withName("Team")
                                                   .build();
    private final AuthorizationUser adminUser = new AuthorizationUser(defaultUserWithTeams(1L,
                                                                                           List.of(teamUnderTest),
                                                                                           List.of()));
    private final AuthorizationUser memberUser = new AuthorizationUser(defaultUserWithTeams(1L,
                                                                                            List.of(),
                                                                                            List.of(teamUnderTest)));
    private final AuthorizationUser userWithNoTeams = new AuthorizationUser(defaultUserWithTeams(1L,
                                                                                                 List.of(),
                                                                                                 List.of()));

    @Test
    void createEntityShouldReturnTeam() {
        when(teamBusinessService.createTeam(teamUnderTest, authorizationService.updateOrAddAuthorizationUser()))
                .thenReturn(teamUnderTest);

        Team team = teamAuthorizationService.createEntity(teamUnderTest);
        assertEquals(teamUnderTest, team);
    }

    @Test
    void updateEntityShouldReturnUpdatedTeamWhenAuthorizedAsOkrChampion() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(okrChampionUser);
        when(teamBusinessService.updateTeam(teamUnderTest, id)).thenReturn(teamUnderTest);

        Team team = teamAuthorizationService.updateEntity(teamUnderTest, id);
        assertEquals(teamUnderTest, team);
    }

    @Test
    void updateEntityShouldReturnUpdatedTeamWhenAuthorizedAsAdminUser() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(adminUser);
        when(teamBusinessService.updateTeam(teamUnderTest, id)).thenReturn(teamUnderTest);

        Team team = teamAuthorizationService.updateEntity(teamUnderTest, id);
        assertEquals(teamUnderTest, team);
    }

    @Test
    void updateEntityShouldThrowExceptionWhenAuthorizedAsMemberUser() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(memberUser);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> teamAuthorizationService.updateEntity(teamUnderTest,
                                                                                                     id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_WRITE", exception.getReason());
    }

    @Test
    void updateEntityShouldThrowExceptionWhenAuthorizedAsUserWithNoTeams() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(userWithNoTeams);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> teamAuthorizationService.updateEntity(teamUnderTest,
                                                                                                     id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_WRITE", exception.getReason());
    }

    @Test
    void deleteEntityByIdShouldPassThroughWhenAuthorizedAsOkrChampion() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(okrChampionUser);
        teamAuthorizationService.deleteEntity(teamUnderTest.getId());
    }

    @Test
    void deleteEntityByIdShouldPassThroughWhenAuthorizedAsTeamAdmin() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(adminUser);
        teamAuthorizationService.deleteEntity(teamUnderTest.getId());
    }

    @Test
    void deleteEntityByIdShouldThrowExceptionWhenAuthorizedAsMemberUser() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(memberUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamAuthorizationService.deleteEntity(teamUnderTest.getId()));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_DELETE", exception.getReason());
    }

    @Test
    void deleteEntityByIdShouldThrowExceptionWhenAuthorizedAsUserWithNoTeams() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(userWithNoTeams);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> teamAuthorizationService.deleteEntity(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_DELETE", exception.getReason());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void getAllTeamsShouldReturnAllTeams(boolean isWriteable) {
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

    @Test
    void addUsersToTeam_shouldThrowExceptionIfUserNotAuthorized() {
        when(authorizationService.updateOrAddAuthorizationUser())
                .thenReturn(new AuthorizationUser(defaultUserWithTeams(1L, List.of(), List.of())));
        assertThrows(OkrResponseStatusException.class, () -> teamAuthorizationService.addUsersToTeam(1L, List.of()));
    }

    @Test
    void addUsersToTeam_shouldCallTeamBusinessService() {
        var adminTeamId = 1L;
        var adminTeam = defaultTeam(adminTeamId);
        var usersList = List.of(1L, 2L);
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(new AuthorizationUser(defaultUserWithTeams(1L,
                                                                                                                        List.of(adminTeam),
                                                                                                                        List.of())));
        teamAuthorizationService.addUsersToTeam(adminTeamId, usersList);
        verify(teamBusinessService, times(1)).addUsersToTeam(adminTeamId, usersList);

    }
}
