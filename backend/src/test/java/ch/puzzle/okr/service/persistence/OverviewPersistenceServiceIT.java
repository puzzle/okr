package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringIntegrationTest
class OverviewPersistenceServiceIT {
    @Autowired
    private OverviewPersistenceService overviewPersistenceService;

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnOverviews() {
        List<Overview> overviews = overviewPersistenceService.getOverviewByQuarterAndTeamsAndObjectiveQuery(2L,
                List.of(5L, 6L, 8L), "");

        assertEquals(13, overviews.size());
        assertIterableEquals(overviewIds_getOverviewByQuarterIdAndTeamIdsAndObjectiveQueryShouldReturnOverviews(),
                getOverviewIds(overviews));
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsAndObjectiveQueryShouldReturnOverviews() {
        List<Overview> overviews = overviewPersistenceService.getOverviewByQuarterAndTeamsAndObjectiveQuery(2L,
                List.of(5L, 6L, 8L), "kundenzufriedenheit");

        assertEquals(4, overviews.size());
        assertIterableEquals(overviewIds_getExpectedOverviewIds_getOverviewByQuarterIdAndTeamIdsShouldReturnOverviews(),
                getOverviewIds(overviews));
    }

    // Stop excluding test after query is updated so all teams are shown even if no filter matches
    // @Test
    void getOverviewByQuarterIdAndTeamIdsAndObjectiveQueryShouldReturnAllTeamsWhenQueryIsNotFound() {
        List<Overview> overviews = overviewPersistenceService.getOverviewByQuarterAndTeamsAndObjectiveQuery(2L,
                List.of(5L, 6L, 8L), "asdfasdfasdf");

        assertEquals(3, overviews.size());
    }

    private List<OverviewId> getOverviewIds(List<Overview> overviewList) {
        return overviewList.stream().map(Overview::getOverviewId).toList();
    }

    private static List<OverviewId> overviewIds_getOverviewByQuarterIdAndTeamIdsAndObjectiveQueryShouldReturnOverviews() {
        return List.of(OverviewId.of(5L, 3L, 3L, 9L), //
                OverviewId.of(5L, 3L, 4L, 8L), //
                OverviewId.of(5L, 3L, 5L, 7L), //
                OverviewId.of(5L, 4L, 6L, 5L), //
                OverviewId.of(5L, 4L, 7L, 4L), //
                OverviewId.of(5L, 4L, 8L, 2L), //
                OverviewId.of(6L, 8L, 18L, 20L), //
                OverviewId.of(6L, 8L, 19L, 19L), //
                OverviewId.of(6L, 9L, 15L, 18L), //
                OverviewId.of(6L, 9L, 16L, 17L), //
                OverviewId.of(6L, 9L, 17L, 16L), //
                OverviewId.of(6L, 10L, -1L, -1L), //
                OverviewId.of(8L, -1L, -1L, -1L));
    }

    private static List<OverviewId> overviewIds_getExpectedOverviewIds_getOverviewByQuarterIdAndTeamIdsShouldReturnOverviews() {
        return List.of(OverviewId.of(5L, 3L, 3L, 9L), //
                OverviewId.of(5L, 3L, 4L, 8L), //
                OverviewId.of(5L, 3L, 5L, 7L), //
                OverviewId.of(8L, -1L, -1L, -1L));
    }
}
