package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeamComparatorTest {

    @DisplayName("compare() should return -1 when user is only member of team1")
    @Test
    void compareShouldReturnNegativeOneWhenUserIsOnlyMemberOfTeam1() {
        // arrange
        var team1 = Team.Builder.builder().withId(1L).build();
        var team2 = Team.Builder.builder().withId(2L).build();

        var user = mock(AuthorizationUser.class);
        when(user.isUserMemberInTeam(team1.getId())).thenReturn(true);
        when(user.isUserMemberInTeam(team2.getId())).thenReturn(false);

        // act
        var comparator = new TeamBusinessService.TeamComparator(user);
        var result = comparator.compare(team1, team2);

        // assert
        assertEquals(-1, result);
    }

    @DisplayName("compare() should return 1 when user is only member of team2")
    @Test
    void compareShouldReturnOneWhenUserIsOnlyMemberOfTeam2() {
        // arrange
        var team1 = Team.Builder.builder().withId(1L).build();
        var team2 = Team.Builder.builder().withId(2L).build();

        var user = mock(AuthorizationUser.class);
        when(user.isUserMemberInTeam(team1.getId())).thenReturn(false);
        when(user.isUserMemberInTeam(team2.getId())).thenReturn(true);

        // act
        var comparator = new TeamBusinessService.TeamComparator(user);
        var result = comparator.compare(team1, team2);

        // assert
        assertEquals(1, result);
    }

    @DisplayName("compare() should compare by team Ids when user is member of both teams and teams have same name")
    @ParameterizedTest
    @CsvSource({ "1,2,-1", "2,1,1" })
    void compareShouldCompareByTeamIdsWhenUserIsMemberOfBothTeamsAndTeamsHaveSameName(Long team1Id, Long team2Id,
            int expectedResult) {

        // arrange
        var team1 = Team.Builder.builder().withId(team1Id).withName("tiger-team").build();
        var team2 = Team.Builder.builder().withId(team2Id).withName("tiger-team").build();

        var authorizationUser = mock(AuthorizationUser.class);
        when(authorizationUser.isUserMemberInTeam(team1.getId())).thenReturn(true);
        when(authorizationUser.isUserMemberInTeam(team2.getId())).thenReturn(true);

        // act
        var comparator = new TeamBusinessService.TeamComparator(authorizationUser);
        var result = comparator.compare(team1, team2);

        // assert
        assertEquals(expectedResult, result);
    }

    @DisplayName("compare() should compare by team names when user is member of both teams and teams have different names")
    @ParameterizedTest
    @CsvSource({ "tiger-team1,tiger-team2,-1", "tiger-team2,tiger-team1,1" })
    void compareShouldCompareByTeamNamesWhenUserIsMemberOfBothTeamsAndTeamsHaveDifferentNames(String team1Name,
            String team2Name, int expectedResult) {

        // arrange
        var team1 = Team.Builder.builder().withName(team1Name).build();
        var team2 = Team.Builder.builder().withName(team2Name).build();

        var user = mock(AuthorizationUser.class);
        when(user.isUserMemberInTeam(team1.getId())).thenReturn(true);
        when(user.isUserMemberInTeam(team2.getId())).thenReturn(true);

        // act
        var comparator = new TeamBusinessService.TeamComparator(user);
        var result = comparator.compare(team1, team2);

        // assert
        assertEquals(expectedResult, result);
    }

}
