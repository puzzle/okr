package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.TestHelper.userWithoutWriteAllRole;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        when(teamAuthorizationService.createEntity(newTeam)).thenReturn(newTeam);

        Team team = teamAuthorizationService.createEntity(newTeam);
        assertEquals(newTeam, team);
    }

    @Test
    void createEntityShouldThrowExceptionWhenNotAuthorized() {
        String reason = "not authorized to create team";
        when(authorizationService.getAuthorizationUser()).thenReturn(userWithoutWriteAllRole());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamAuthorizationService.createEntity(newTeam));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
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
        String reason = "not authorized to update team";
        when(authorizationService.getAuthorizationUser()).thenReturn(userWithoutWriteAllRole());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamAuthorizationService.updateEntity(newTeam, id));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
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
        String reason = "not authorized to delete team";
        when(authorizationService.getAuthorizationUser()).thenReturn(userWithoutWriteAllRole());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamAuthorizationService.deleteEntity(id));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getEntitiesShouldReturnTeams() {
        List<Team> teamList = List.of(newTeam, newTeam);
        when(teamBusinessService.getAllTeams()).thenReturn(teamList);

        List<Team> teams = teamAuthorizationService.getEntities();
        assertEquals(teamList, teams);
    }
}
