package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ch.puzzle.okr.test.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAuthorizationServiceTest {
    @Mock
    UserBusinessService userBusinessService;
    @Mock
    AuthorizationService authorizationService;
    @Mock
    TeamAuthorizationService teamAuthorizationService;
    @InjectMocks
    private UserAuthorizationService userAuthorizationService;

    private final long adminTeamId = 5L;
    private final long memberTeamId = 6L;
    User user = defaultUserWithTeams(1L, List.of(defaultTeam(adminTeamId), defaultTeam(memberTeamId)), List.of());
    User user2 = defaultUserWithTeams(2L, List.of(), List.of(defaultTeam(adminTeamId), defaultTeam(memberTeamId)));

    @Test
    void getAllUsersShouldReturnAllUsers() {
        List<User> userList = List.of(user, user2);
        when(userBusinessService.getAllUsers()).thenReturn(userList);
        when(teamAuthorizationService.isUserWriteAllowed(adminTeamId)).thenReturn(true);
        when(teamAuthorizationService.isUserWriteAllowed(memberTeamId)).thenReturn(false);

        List<User> users = userAuthorizationService.getAllUsers();
        assertEquals(userList, users);
    }

    @Test
    void getAllUsers_shouldSetTeamWritableCorrectly() {
        List<User> userList = List.of(user, user2);
        when(userBusinessService.getAllUsers()).thenReturn(userList);
        when(teamAuthorizationService.isUserWriteAllowed(adminTeamId)).thenReturn(true);
        when(teamAuthorizationService.isUserWriteAllowed(memberTeamId)).thenReturn(false);

        List<User> users = userAuthorizationService.getAllUsers();
        assertTrue(users.get(0).getUserTeamList().get(0).getTeam().isWriteable());
        assertFalse(users.get(0).getUserTeamList().get(1).getTeam().isWriteable());
        assertTrue(users.get(1).getUserTeamList().get(0).getTeam().isWriteable());
        assertFalse(users.get(1).getUserTeamList().get(1).getTeam().isWriteable());
    }

    @Test
    void setOkrChampion_shouldCallBusinessService() {
        var loggedInUser = defaultUser(1L);
        loggedInUser.setOkrChampion(true);

        when(userBusinessService.getUserById(user.getId())).thenReturn(user);
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(new AuthorizationUser(loggedInUser));

        userAuthorizationService.setIsOkrChampion(user.getId(), true);

        verify(userBusinessService, times(1)).setIsOkrChampion(user, true);
    }

    @Test
    void setOkrChampion_shouldThrowErrorIfLoggedInUserIsNotOkrChampion() {
        var loggedInUser = defaultUser(1L);
        loggedInUser.setOkrChampion(false);

        when(userBusinessService.getUserById(user.getId())).thenReturn(user);
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(new AuthorizationUser(loggedInUser));

        assertThrows(OkrResponseStatusException.class,
                () -> userAuthorizationService.setIsOkrChampion(user.getId(), true));
    }

    @Test
    void createUsers_shouldCallBusinessService() {
        var loggedInUser = defaultUser(1L);
        loggedInUser.setOkrChampion(true);

        List<User> users = List.of(user, user2);
        when(userBusinessService.createUsers(users)).thenReturn(users);
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(new AuthorizationUser(loggedInUser));

        userAuthorizationService.createUsers(users);

        verify(userBusinessService, times(1)).createUsers(users);
    }

    @Test
    void createUsers_shouldThrowErrorIfLoggedInUserIsNotOkrChampion() {
        var loggedInUser = defaultUser(1L);
        loggedInUser.setOkrChampion(false);

        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(new AuthorizationUser(loggedInUser));

        assertThrows(OkrResponseStatusException.class,
                () -> userAuthorizationService.createUsers(List.of(user, user2)));
    }
}
