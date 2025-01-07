package ch.puzzle.okr.service.authorization;

import static ch.puzzle.okr.test.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UserBusinessService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserAuthorizationServiceTest {
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

    @DisplayName("Should return all users")
    @Test
    void shouldReturnAllUsers() {
        List<User> userList = List.of(user, user2);
        when(userBusinessService.getAllUsers()).thenReturn(userList);
        when(teamAuthorizationService.isUserWriteAllowed(adminTeamId)).thenReturn(true);
        when(teamAuthorizationService.isUserWriteAllowed(memberTeamId)).thenReturn(false);

        List<User> users = userAuthorizationService.getAllUsers();
        assertEquals(userList, users);
    }

    @DisplayName("Should set team writable property correctly")
    @Test
    void shouldSetTeamWritableCorrectly() {
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

    @DisplayName("Should call the businessService when setting OKR champion")
    @Test
    void shouldCallBusinessServiceWhenSettingOkrChampion() {
        var loggedInUser = defaultUser(1L);
        loggedInUser.setOkrChampion(true);

        when(userBusinessService.getUserById(user.getId())).thenReturn(user);
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(new AuthorizationUser(loggedInUser));

        userAuthorizationService.setIsOkrChampion(user.getId(), true);

        verify(userBusinessService, times(1)).setIsOkrChampion(user, true);
    }

    @DisplayName("Should throw an error if logged-in user is not OKR champion")
    @Test
    void shouldThrowErrorIfLoggedInUserIsNotOkrChampion() {
        var loggedInUser = defaultUser(1L);
        Long userId = user.getId();
        loggedInUser.setOkrChampion(false);

        when(userBusinessService.getUserById(user.getId())).thenReturn(user);
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(new AuthorizationUser(loggedInUser));

        assertThrows(OkrResponseStatusException.class,
                     () -> userAuthorizationService.setIsOkrChampion(userId, true));
    }

    @DisplayName("Should call the businessService when creating users")
    @Test
    void shouldCallBusinessServiceWhenCreatingUsers() {
        var loggedInUser = defaultUser(1L);
        loggedInUser.setOkrChampion(true);

        List<User> users = List.of(user, user2);
        when(userBusinessService.createUsers(users)).thenReturn(users);
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(new AuthorizationUser(loggedInUser));

        userAuthorizationService.createUsers(users);

        verify(userBusinessService, times(1)).createUsers(users);
    }

    @DisplayName("Should throw an error if logged-in user is not OKR champion when creating users")
    @Test
    void shouldThrowErrorIfLoggedInUserIsNotOkrChampionWhenCreatingUsers() {
        var loggedInUser = defaultUser(1L);
        loggedInUser.setOkrChampion(false);
        List<User> userList = List.of(user, user2);

        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(new AuthorizationUser(loggedInUser));

        assertThrows(OkrResponseStatusException.class,
                     () -> userAuthorizationService.createUsers(userList));
    }

    @DisplayName("Should return false if user is not a member of teams")
    @Test
    void shouldReturnFalseIfUserIsNotMemberOfTeams() {
        // arrange
        Long userId = 1L;
        User userWithoutTeams = defaultUser(userId);
        when(userBusinessService.getUserById(userId)).thenReturn(userWithoutTeams);

        // act
        boolean isUserMemberOfTeams = userAuthorizationService.isUserMemberOfTeams(1L);

        // assert
        assertFalse(isUserMemberOfTeams);
    }

    @DisplayName("Should return true if user is a member of teams")
    @Test
    void shouldReturnTrueIfUserIsMemberOfTeams() {
        // arrange
        User userWithTeams = user2;
        Long userId = user2.getId();
        when(userBusinessService.getUserById(userId)).thenReturn(userWithTeams);

        // act
        boolean isUserMemberOfTeams = userAuthorizationService.isUserMemberOfTeams(userId);

        // assert
        assertTrue(isUserMemberOfTeams);
    }
}
