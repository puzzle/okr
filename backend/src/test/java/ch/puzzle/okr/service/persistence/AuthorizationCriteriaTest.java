package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.test.TestHelper.mockAuthorizationUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AuthorizationCriteriaTest {

    @DisplayName("Should be successful on appendObjective() with default authorization user")
    @Test
    void appendObjectiveShouldBeSuccessfulWithDefaultAuthorizationUser() {
        // arrange
        var criteria = new AuthorizationCriteria<Objective>();

        // act
        var current = criteria.appendObjective(defaultAuthorizationUser());

        // assert
        var expected = " and ((o.state=:teamDraftState and o.team.id IN (:userTeamIds)) or o.state IN (:publishedStates))";
        assertEquals(expected, current);
    }

    @DisplayName("Should be successful on appendObjective() when user is okrChampion")
    @Test
    void appendObjectiveShouldBeSuccessfulWhenUserIsOkrChampion() {
        // arrange
        var user = User.Builder
                .builder() //
                .withId(23L) //
                .withFirstName("Hanna") //
                .withLastName("muster") //
                .withEmail("hanna.muster@example.com") //
                .isOkrChampion(true) //
                .build();
        var criteria = new AuthorizationCriteria<Objective>();

        // act
        var current = criteria.appendObjective(mockAuthorizationUser(user));

        // assert
        var expected = " and (o.state=:allDraftState or o.state IN (:publishedStates))";
        assertEquals(expected, current);
    }

    @ParameterizedTest(name = "should be successful on appendOverview() when team ids ({0}) or objectives query ({0}) are empty")
    @MethodSource("provideListAndString")
    void appendOverviewShouldBeSuccessfulWhenTeamIdsOrObjectiveQueryAreEmpty(List<Long> teamIds,
                                                                             String objectiveQuery) {
        // arrange
        var criteria = new AuthorizationCriteria<Objective>();

        // act
        var current = criteria.appendOverview(teamIds, objectiveQuery, defaultAuthorizationUser());

        // assert
        var expected = "\n and ((o.objectiveState=:teamDraftState and o.overviewId.teamId IN (:userTeamIds)) or o.objectiveState IN (:publishedStates) or o.overviewId.objectiveId = -1)";
        assertEquals(expected, current);
    }

    private static Stream<Arguments> provideListAndString() {
        return Stream
                .of( //
                    Arguments.of(List.of(), null), //
                    Arguments.of(List.of(), ""), //
                    Arguments.of(null, null), //
                    Arguments.of(null, ""));
    }

    @DisplayName("Should be successful on appendOverview() when team ids and objective query are not empty")
    @Test
    void appendOverviewShouldBeSuccessfulWhenTeamIdsAndObjectiveQueryAreNotEmpty() {
        // arrange
        var criteria = new AuthorizationCriteria<Objective>();
        var anyTeamIds = List.of(99L);
        var anyNonEmptyString = "OBJECTIVEQUERY";
        var startingNewLine = "\n";
        var singleSpace = " ";

        // act
        var current = criteria.appendOverview(anyTeamIds, anyNonEmptyString, defaultAuthorizationUser());

        // assert
        var expected = startingNewLine + singleSpace
                       + """
                               and o.overviewId.teamId in (:teamIds)
                                and lower(coalesce(o.objectiveTitle, '')) like lower(concat('%',:objectiveQuery,'%'))
                                and ((o.objectiveState=:teamDraftState and o.overviewId.teamId IN (:userTeamIds)) or o.objectiveState IN (:publishedStates) or o.overviewId.objectiveId = -1)""";

        assertEquals(expected, current);
        assertFalse(current.contains(anyNonEmptyString));
    }

}
