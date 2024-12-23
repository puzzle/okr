package ch.puzzle.okr.models.authorization;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;
import ch.puzzle.okr.test.TestHelper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorizationUserTest {

    private final List<UserTeam> userTeamList = List
            .of(UserTeam.Builder.builder().withTeam(TestHelper.defaultTeam(1L)).isTeamAdmin(true).build(),
                UserTeam.Builder.builder().withTeam(TestHelper.defaultTeam(2L)).isTeamAdmin(false).build(),
                UserTeam.Builder.builder().withTeam(TestHelper.defaultTeam(3L)).isTeamAdmin(true).build(),
                UserTeam.Builder.builder().withTeam(TestHelper.defaultTeam(4L)).isTeamAdmin(false).build(),
                UserTeam.Builder.builder().withTeam(TestHelper.defaultTeam(5L)).isTeamAdmin(false).build());
    private final User user = User.Builder.builder().withUserTeamList(userTeamList).build();
    private final AuthorizationUser authorizationUser = new AuthorizationUser(user);

    @DisplayName("Should return all team-ids when extracting team-ids of a user")
    @Test
    public void shouldReturnAllTeamIdsWhenExtractingTeamIdsOfUser() {
        assertEquals(List.of(1L, 2L, 3L, 4L, 5L), authorizationUser.extractTeamIds());
    }

    @DisplayName("Should return correctly if a user is a member in a team")
    @Test
    public void shouldCorrectlyCheckIfUserIsMemberInTeam() {
        assertTrue(authorizationUser.isUserMemberInTeam(1L));
        assertTrue(authorizationUser.isUserMemberInTeam(2L));
        assertTrue(authorizationUser.isUserMemberInTeam(3L));
        assertTrue(authorizationUser.isUserMemberInTeam(4L));
        assertTrue(authorizationUser.isUserMemberInTeam(5L));
        assertFalse(authorizationUser.isUserMemberInTeam(6L));
    }

    @DisplayName("Should return correctly if a user is an admin in a team")
    @Test
    public void shouldCorrectlyCheckIfUserIsAdminInTeam() {
        assertTrue(authorizationUser.isUserAdminInTeam(1L));
        assertTrue(authorizationUser.isUserAdminInTeam(3L));
        assertFalse(authorizationUser.isUserAdminInTeam(2L));
        assertFalse(authorizationUser.isUserAdminInTeam(4L));
        assertFalse(authorizationUser.isUserAdminInTeam(5L));
        assertFalse(authorizationUser.isUserAdminInTeam(6L));
    }

}