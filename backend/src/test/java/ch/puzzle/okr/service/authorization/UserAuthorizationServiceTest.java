package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultTeam;
import static ch.puzzle.okr.TestHelper.defaultUserWithTeams;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAuthorizationServiceTest {
    @Mock
    UserBusinessService userBusinessService;
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    private UserAuthorizationService userAuthorizationService;

    private final long adminTeamId = 5L;
    private final long memberTeamId = 6L;

    private final AuthorizationUser authorizationUser = new AuthorizationUser(
            defaultUserWithTeams(1L, List.of(defaultTeam(adminTeamId)), List.of(defaultTeam(memberTeamId))));
    User user = User.Builder.builder().withId(5L).withFirstname("firstname").withLastname("lastname")
            .withEmail("lastname@puzzle.ch").build();

    @Test
    void getAllUsersShouldReturnAllUsers() {
        List<User> userList = List.of(user, user);
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(userBusinessService.getAllUsers()).thenReturn(userList);

        List<User> users = userAuthorizationService.getAllUsers();
        assertEquals(userList, users);
    }
}
