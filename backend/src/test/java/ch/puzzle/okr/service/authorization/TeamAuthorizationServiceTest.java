package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static ch.puzzle.okr.test.TestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class TeamAuthorizationServiceTest {

    @Mock
    TeamBusinessService teamBusinessService;
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    private TeamAuthorizationService teamAuthorizationService;

    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    private final Team newTeam = Team.Builder.builder().withId(5L).withName("Team")
            .withAuthorizationOrganisation(new ArrayList<>()).build();

    @Test
    void createEntityShouldReturnTeamWhenAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(teamBusinessService.createTeam(newTeam)).thenReturn(newTeam);

        Team team = teamAuthorizationService.createEntity(newTeam);
        assertEquals(newTeam, team);
    }

    @Test
    void createEntityShouldThrowExceptionWhenNotAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(userWithoutWriteAllRole());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamAuthorizationService.createEntity(newTeam));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_WRITE", exception.getReason());
    }

    @Test
    void updateEntityShouldReturnUpdatedTeamWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(teamBusinessService.updateTeam(newTeam, id)).thenReturn(newTeam);

        Team team = teamAuthorizationService.updateEntity(newTeam, id);
        assertEquals(newTeam, team);
    }

    @Test
    void updateEntityShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(userWithoutWriteAllRole());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamAuthorizationService.updateEntity(newTeam, id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_WRITE", exception.getReason());
    }

    @Test
    void deleteEntityByIdShouldPassThroughWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        teamAuthorizationService.deleteEntity(id);
    }

    @Test
    void deleteEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(userWithoutWriteAllRole());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamAuthorizationService.deleteEntity(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_DELETE", exception.getReason());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void getAllTeamsShouldReturnAllTeams(boolean isWriteable) {
        List<Team> teamList = List.of(newTeam, newTeam);
        if (isWriteable) {
            when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        } else {
            when(authorizationService.getAuthorizationUser()).thenReturn(userWithoutWriteAllRole());
        }
        when(teamBusinessService.getAllTeams(any())).thenReturn(teamList);

        List<Team> teams = teamAuthorizationService.getAllTeams();
        assertEquals(teamList, teams);
        teams.forEach(team -> assertEquals(isWriteable, team.isWriteable()));
    }

    @DisplayName("getUserTeamIds() should return team Ids independent of the roles of the AuthorizedUser")
    @ParameterizedTest
    @MethodSource("generator")
    void getUserTeamIdsShouldReturnTeamIdsIndependentOfRolesOfAuthorizedUser(AuthorizationUser user) {
        Long teamIdAuthorizedUser = 5L;
        when(authorizationService.getAuthorizationUser()).thenReturn(user);

        List<Long> userTeamIds = teamAuthorizationService.getUserTeamIds();
        assertEquals(1, userTeamIds.size());
        assertEquals(teamIdAuthorizedUser, userTeamIds.get(0));
    }

    private static Stream<Arguments> generator() {
        return Stream.of(Arguments.of(userWithoutAnyRole()), Arguments.of(defaultAuthorizationUser()));
    }

}
