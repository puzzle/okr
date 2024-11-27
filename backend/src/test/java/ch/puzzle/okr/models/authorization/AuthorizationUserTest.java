package ch.puzzle.okr.models.authorization;

import java.util.List;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;
import ch.puzzle.okr.test.TestHelper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationUserTest {

    private final List<UserTeam> userTeamList = List.of(UserTeam.Builder.builder()
                                                                        .withTeam(TestHelper.defaultTeam(1L))
                                                                        .withTeamAdmin(true)
                                                                        .build(),
                                                        UserTeam.Builder.builder()
                                                                        .withTeam(TestHelper.defaultTeam(2L))
                                                                        .withTeamAdmin(false)
                                                                        .build(),
                                                        UserTeam.Builder.builder()
                                                                        .withTeam(TestHelper.defaultTeam(3L))
                                                                        .withTeamAdmin(true)
                                                                        .build(),
                                                        UserTeam.Builder.builder()
                                                                        .withTeam(TestHelper.defaultTeam(4L))
                                                                        .withTeamAdmin(false)
                                                                        .build(),
                                                        UserTeam.Builder.builder()
                                                                        .withTeam(TestHelper.defaultTeam(5L))
                                                                        .withTeamAdmin(false)
                                                                        .build());
    private final User user = User.Builder.builder()
                                          .withUserTeamList(userTeamList)
                                          .build();
    private final AuthorizationUser authorizationUser = new AuthorizationUser(user);

    @Test
    public void extractTeamIds_shouldReturnAllIds() {
        assertEquals(List.of(1L, 2L, 3L, 4L, 5L), authorizationUser.extractTeamIds());
    }

    @Test
    public void isUserMemberInTeam_shouldContainAllUsers() {
        assertTrue(authorizationUser.isUserMemberInTeam(1L));
        assertTrue(authorizationUser.isUserMemberInTeam(2L));
        assertTrue(authorizationUser.isUserMemberInTeam(3L));
        assertTrue(authorizationUser.isUserMemberInTeam(4L));
        assertTrue(authorizationUser.isUserMemberInTeam(5L));
        assertFalse(authorizationUser.isUserMemberInTeam(6L));
    }

    @Test
    public void isUserAdminInTeam_shouldContainAllAdminUsers() {
        assertTrue(authorizationUser.isUserAdminInTeam(1L));
        assertTrue(authorizationUser.isUserAdminInTeam(3L));
        assertFalse(authorizationUser.isUserAdminInTeam(2L));
        assertFalse(authorizationUser.isUserAdminInTeam(4L));
        assertFalse(authorizationUser.isUserAdminInTeam(5L));
        assertFalse(authorizationUser.isUserAdminInTeam(6L));
    }

}