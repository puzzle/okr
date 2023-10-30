package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringIntegrationTest
class OverviewPersistenceServiceIT {
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @Autowired
    private OverviewPersistenceService overviewPersistenceService;

    @Test
    void getFilteredOverviewShouldReturnOverviews() {
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(2L, List.of(5L, 6L, 8L), "",
                authorizationUser);

        assertEquals(12, overviews.size());
        assertIterableEquals(getOverviewIdsForFilteredOverviewShouldReturnOverviews(), getOverviewIds(overviews));
    }

    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenObjectiveQuery() {
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(2L, List.of(5L, 6L, 8L),
                "kundenzufriedenheit", authorizationUser);

        assertEquals(3, overviews.size());
        assertIterableEquals(getOverviewIdsForFilteredOverviewShouldReturnOverviewsWhenObjectiveQuery(),
                getOverviewIds(overviews));
    }

    private List<OverviewId> getOverviewIds(List<Overview> overviewList) {
        return overviewList.stream().map(Overview::getOverviewId).toList();
    }

    private static List<OverviewId> getOverviewIdsForFilteredOverviewShouldReturnOverviews() {
        return List.of(OverviewId.of(5L, 3L, 3L, 9L), OverviewId.of(5L, 3L, 4L, 8L), OverviewId.of(5L, 3L, 5L, 7L),
                OverviewId.of(5L, 4L, 6L, 5L), OverviewId.of(5L, 4L, 7L, 4L), OverviewId.of(5L, 4L, 8L, 2L),
                OverviewId.of(6L, 8L, 18L, 20L), OverviewId.of(6L, 8L, 19L, 19L), OverviewId.of(6L, 9L, 15L, 18L),
                OverviewId.of(6L, 9L, 16L, 17L), OverviewId.of(6L, 9L, 17L, 16L), OverviewId.of(6L, 10L, -1L, -1L));
    }

    private static List<OverviewId> getOverviewIdsForFilteredOverviewShouldReturnOverviewsWhenObjectiveQuery() {
        return List.of(OverviewId.of(5L, 3L, 3L, 9L), OverviewId.of(5L, 3L, 4L, 8L), OverviewId.of(5L, 3L, 5L, 7L));
    }
}