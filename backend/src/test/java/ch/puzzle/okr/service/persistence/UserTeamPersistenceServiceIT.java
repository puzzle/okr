package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// uses test date from V100_0_0__TestData.sql
@SpringIntegrationTest
public class UserTeamPersistenceServiceIT {
    private static final Long ID_OF_USER_ALICE = 11L; // user Alice is only in team Lorem
    private static final Long ID_OF_TEAM_LOREM = 6L; // team Lorem has 3 users

    private static final Long ID_OF_USER_BOB = 21L; // user Bob is only in team Cube
    private static final Long ID_OF_TEAM_CUBE = 8L; // team Cube has 2 users

    @Autowired
    private UserTeamPersistenceService userTeamPersistenceService;

    @Autowired
    private UserPersistenceService userPersistenceService;

    @Autowired
    private TeamPersistenceService teamPersistenceService;

    static {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @DisplayName("Should remove single user from team on delete()")
    @Test
    @Transactional
    void deleteShouldRemoveSingleUserFromTeam() {
        // arrange
        var user = userPersistenceService.findById(ID_OF_USER_ALICE);
        var team = teamPersistenceService.findById(ID_OF_TEAM_LOREM);

        // preconditions: user Alice is in team Lorem and team Lorem has 3 users
        assertUserIsInTeam(ID_OF_USER_ALICE, ID_OF_TEAM_LOREM, 3);

        // arrange
        var userTeamToRemove = user.getUserTeamList().get(0); // Alice is only in Team Lorem

        // act
        user.getUserTeamList().remove(userTeamToRemove);
        team.getUserTeamList().remove(userTeamToRemove);
        userTeamPersistenceService.delete(userTeamToRemove);

        // assert: user Alice is no longer in team Lorem and team Lorem has 2 users
        assertUserIsRemovedFromTeam(ID_OF_USER_ALICE, ID_OF_TEAM_LOREM, 2);
    }

    @DisplayName("Should remove list of users from team on deleteAll()")
    @Test
    @Transactional
    void deleteAllShouldRemoveListOfUsersFromTeam() {
        // arrange
        var user = userPersistenceService.findById(ID_OF_USER_BOB);
        var team = teamPersistenceService.findById(ID_OF_TEAM_CUBE);

        // preconditions: user Bob is in a team Cube and team Cube has 2 users
        assertUserIsInTeam(ID_OF_USER_BOB, ID_OF_TEAM_CUBE, 2);

        // arrange
        var userTeamToRemove = user.getUserTeamList().get(0); // Bos is only in Team Cube

        // act
        user.getUserTeamList().remove(userTeamToRemove);
        team.getUserTeamList().remove(userTeamToRemove);
        userTeamPersistenceService.deleteAll(List.of(userTeamToRemove));

        // assert: user Bob is no longer in team Cube and team Cube has 1 user
        assertUserIsRemovedFromTeam(ID_OF_USER_BOB, ID_OF_TEAM_CUBE, 1);
    }

    private void assertUserIsInTeam(Long userId, Long teamId, int expectedUsersInTeam) {
        var user = userPersistenceService.findById(userId);
        Assertions.assertEquals(1, user.getUserTeamList().size());

        var team = this.teamPersistenceService.findById(teamId);
        Assertions.assertEquals(expectedUsersInTeam, team.getUserTeamList().size());
    }

    private void assertUserIsRemovedFromTeam(Long userId, Long teamId, int expectedUsersInTeam) {
        var reloadedUser = userPersistenceService.findById(userId);
        Assertions.assertEquals(0, reloadedUser.getUserTeamList().size());

        var reloadedTeam = this.teamPersistenceService.findById(teamId);
        Assertions.assertEquals(expectedUsersInTeam, reloadedTeam.getUserTeamList().size());
    }

}
