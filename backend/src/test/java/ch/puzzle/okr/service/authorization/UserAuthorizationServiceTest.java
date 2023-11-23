package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.TestHelper.userWithoutWriteAllRole;
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

    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    User user = User.Builder.builder().withId(5L).withFirstname("firstname").withLastname("lastname")
            .withUsername("username").withEmail("lastname@puzzle.ch").build();

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void getAllUsersShouldReturnAllUsers(boolean isWriteable) {
        List<User> userList = List.of(user, user);
        if (isWriteable) {
            when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        } else {
            when(authorizationService.getAuthorizationUser()).thenReturn(userWithoutWriteAllRole());
        }
        when(userBusinessService.getAllUsers()).thenReturn(userList);

        List<User> users = userAuthorizationService.getAllUsers();
        assertEquals(userList, users);
        users.forEach(user -> assertEquals(isWriteable, user.isWriteable()));
    }
}
