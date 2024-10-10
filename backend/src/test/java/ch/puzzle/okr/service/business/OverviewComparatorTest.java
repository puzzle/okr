package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OverviewComparatorTest {

    private static final Long NOT_USED = Long.MAX_VALUE;

    @DisplayName("compare() should return negative one when user is only member of team1")
    @Test
    void compareShouldReturnNegativeOneWhenUserIsOnlyMemberOfTeam1() {
        // arrange
        var overview1 = Overview.Builder.builder().withOverviewId(OverviewId.of(1L, NOT_USED, NOT_USED, NOT_USED))
                .build();

        var overview2 = Overview.Builder.builder().withOverviewId(OverviewId.of(2L, NOT_USED, NOT_USED, NOT_USED))
                .build();

        var user = mock(AuthorizationUser.class);
        when(user.isUserMemberInTeam(overview1.getOverviewId().getTeamId())).thenReturn(true);
        when(user.isUserMemberInTeam(overview2.getOverviewId().getTeamId())).thenReturn(false);

        // act
        var comparator = new OverviewBusinessService.OverviewComparator(user);
        var result = comparator.compare(overview1, overview2);

        // assert
        assertEquals(-1, result);
    }

    @DisplayName("compare() should return one when user is only member of team2")
    @Test
    void compareShouldReturnOneWhenUserIsOnlyMemberOfTeam2() {
        // arrange
        var overview1 = Overview.Builder.builder().withOverviewId(OverviewId.of(1L, NOT_USED, NOT_USED, NOT_USED))
                .build();

        var overview2 = Overview.Builder.builder().withOverviewId(OverviewId.of(2L, NOT_USED, NOT_USED, NOT_USED))
                .build();

        var user = mock(AuthorizationUser.class);
        when(user.isUserMemberInTeam(overview1.getOverviewId().getTeamId())).thenReturn(false);
        when(user.isUserMemberInTeam(overview2.getOverviewId().getTeamId())).thenReturn(true);

        // act
        var comparator = new OverviewBusinessService.OverviewComparator(user);
        var result = comparator.compare(overview1, overview2);

        // assert
        assertEquals(1, result);
    }

    @DisplayName("compare() should compare by team names when user is member of both teams and overviews have different team names")
    @ParameterizedTest
    @CsvSource({ "tiger-team1,tiger-team2,-1", "tiger-team2,tiger-team1,1" })
    void compareShouldCompareByTeamNamesWhenUserIsMemberOfBothTeamsAndOverviewsHaveDifferentTeamNames(String team1Name,
            String team2Name, int expectedResult) {

        // arrange
        var overview1 = Overview.Builder.builder().withOverviewId(OverviewId.of(1L, NOT_USED, NOT_USED, NOT_USED))
                .withTeamName(team1Name).build();

        var overview2 = Overview.Builder.builder().withOverviewId(OverviewId.of(2L, NOT_USED, NOT_USED, NOT_USED))
                .withTeamName(team2Name).build();

        var user = mock(AuthorizationUser.class);
        when(user.isUserMemberInTeam(overview1.getOverviewId().getTeamId())).thenReturn(true);
        when(user.isUserMemberInTeam(overview2.getOverviewId().getTeamId())).thenReturn(true);

        // act
        var comparator = new OverviewBusinessService.OverviewComparator(user);
        var result = comparator.compare(overview1, overview2);

        // assert
        assertEquals(expectedResult, result);
    }

    @DisplayName("compare() should compare by overview Ids when user is member of both teams and teams have same names and overviews have same creation date")
    @ParameterizedTest
    @CsvSource({ "1,2,-1", "2,1,1" })
    void compareShouldCompareByOverviewIdsWhenUserIsMemberOfBothTeamsAndTeamsHaveSameNamesAndOverviewsHaveSameCreationDate(
            Long overviewId1, Long overviewId2, int expectedResult) {

        // arrange
        var objectiveCreatedOn = LocalDateTime.of(2024, 10, 10, 6, 30);

        var overview1 = Overview.Builder.builder()
                .withOverviewId(OverviewId.of(overviewId1, NOT_USED, NOT_USED, NOT_USED)).withTeamName("tiger-team")
                .withObjectiveCreatedOn(objectiveCreatedOn).build();

        var overview2 = Overview.Builder.builder()
                .withOverviewId(OverviewId.of(overviewId2, NOT_USED, NOT_USED, NOT_USED)).withTeamName("tiger-team")
                .withObjectiveCreatedOn(objectiveCreatedOn).build();

        var user = mock(AuthorizationUser.class);
        when(user.isUserMemberInTeam(overview1.getOverviewId().getTeamId())).thenReturn(true);
        when(user.isUserMemberInTeam(overview2.getOverviewId().getTeamId())).thenReturn(true);

        // act
        var comparator = new OverviewBusinessService.OverviewComparator(user);
        var result = comparator.compare(overview1, overview2);

        // assert
        assertEquals(expectedResult, result);
    }

    @DisplayName("compare() should compare by overview creation dates when user is member of both teams and teams have same names and overviews have different creation dates")
    @ParameterizedTest
    @CsvSource({ "2023-10-10T06:30,2024-10-10T06:30,-1", "2024-10-10T06:30,2023-10-10T06:30,1" })
    void compareShouldCompareByOverviewCreationDatesWhenUserIsMemberOfBothTeamsAndTeamsHaveSameNamesAndOverviewsHaveDifferentCreationDates(
            LocalDateTime overviewCreatedOn1, LocalDateTime overviewCreatedOn2, int expectedResult) {

        // arrange
        var overview1 = Overview.Builder.builder().withOverviewId(OverviewId.of(1L, NOT_USED, NOT_USED, NOT_USED))
                .withTeamName("tiger-team").withObjectiveCreatedOn(overviewCreatedOn1).build();

        var overview2 = Overview.Builder.builder().withOverviewId(OverviewId.of(2L, NOT_USED, NOT_USED, NOT_USED))
                .withTeamName("tiger-team").withObjectiveCreatedOn(overviewCreatedOn2).build();

        var user = mock(AuthorizationUser.class);
        when(user.isUserMemberInTeam(overview1.getOverviewId().getTeamId())).thenReturn(true);
        when(user.isUserMemberInTeam(overview2.getOverviewId().getTeamId())).thenReturn(true);

        // act
        var comparator = new OverviewBusinessService.OverviewComparator(user);
        var result = comparator.compare(overview1, overview2);

        // assert
        assertEquals(expectedResult, result);
    }

}
