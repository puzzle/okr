package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringIntegrationTest
class OverviewPersistenceServiceIT {
    private static final List<OverviewId> expectedOverviewIds = List.of( // idx:
            OverviewId.of(5L, 3L, 3L, 9L), // 0
            OverviewId.of(5L, 3L, 4L, 8L), // 1
            OverviewId.of(5L, 3L, 5L, 7L), // 2
            OverviewId.of(5L, 4L, 6L, 5L), // 3
            OverviewId.of(5L, 4L, 7L, 4L), // 4
            OverviewId.of(5L, 4L, 8L, 2L), // 5
            OverviewId.of(6L, 8L, 18L, 20L), // 6
            OverviewId.of(6L, 8L, 19L, 19L), // 7
            OverviewId.of(6L, 9L, 15L, 18L), // 8
            OverviewId.of(6L, 9L, 16L, 17L), // 9
            OverviewId.of(6L, 9L, 17L, 16L), // 10
            OverviewId.of(6L, 10L, -1L, -1L), // 11
            OverviewId.of(4L, 5L, 9L, 15L), // 12
            OverviewId.of(4L, 5L, 10L, 14L), // 13
            OverviewId.of(4L, 6L, 12L, 12L), // 14
            OverviewId.of(4L, 6L, 13L, 11L), // 15
            OverviewId.of(4L, 6L, 14L, 10L)); // 16
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @Autowired
    private OverviewPersistenceService overviewPersistenceService;

    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenTeamIdsSet() {
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(2L, List.of(5L, 6L, 8L), "",
                authorizationUser);

        assertThat(expectedOverviewIds.subList(0, 12)).hasSameElementsAs(getOverviewIds(overviews));
    }

    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenTeamIdsEmpty() {
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(2L, List.of(), "", authorizationUser);

        assertThat(expectedOverviewIds).hasSameElementsAs(getOverviewIds(overviews));
    }

    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenObjectiveQuery() {
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(2L, List.of(5L, 6L, 8L),
                "kundenzufriedenheit", authorizationUser);

        assertThat(expectedOverviewIds.subList(0, 3)).hasSameElementsAs(getOverviewIds(overviews));
    }

    private List<OverviewId> getOverviewIds(List<Overview> overviewList) {
        return overviewList.stream().map(Overview::getOverviewId).toList();
    }
}