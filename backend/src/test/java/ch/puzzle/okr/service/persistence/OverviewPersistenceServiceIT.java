package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringIntegrationTest
class OverviewPersistenceServiceIT {
    private static final List<OverviewId> expectedOverviewIds = List
            .of( // index:
                    OverviewId.of(4L, 5L, 9L, 15L), // 0
                    OverviewId.of(4L, 5L, 10L, 14L), // 1
                    OverviewId.of(4L, 6L, 12L, 12L), // 2
                    OverviewId.of(4L, 6L, 13L, 11L), // 3
                    OverviewId.of(4L, 6L, 14L, 10L), // 4
                    OverviewId.of(5L, 3L, 3L, 9L), // 5
                    OverviewId.of(5L, 3L, 4L, 8L), // 6
                    OverviewId.of(5L, 3L, 5L, 7L), // 7
                    OverviewId.of(5L, 4L, 6L, 5L), // 8
                    OverviewId.of(5L, 4L, 7L, 4L), // 9
                    OverviewId.of(5L, 4L, 8L, 2L), // 10
                    OverviewId.of(6L, 8L, 18L, 20L), // 11
                    OverviewId.of(6L, 8L, 19L, 19L), // 12
                    OverviewId.of(6L, 9L, 15L, 18L), // 13
                    OverviewId.of(6L, 9L, 16L, 17L), // 14
                    OverviewId.of(6L, 9L, 17L, 16L), // 15
                    OverviewId.of(6L, 10L, -1L, -1L), // 16
                    OverviewId.of(8L, -1L, -1L, -1L), // 17
                    OverviewId.of(5L, -1L, -1L, -1L), // 18
                    OverviewId.of(6L, -1L, -1L, -1L), // 19
                    OverviewId.of(4L, -1L, -1L, -1L)); // 20

    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @Autowired
    private OverviewPersistenceService overviewPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @AfterEach
    void tearDown() {
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Should return correct overview on getFilteredOverview() when team ids are set")
    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenTeamIdsSet() {
        List<Overview> overviews = overviewPersistenceService
                .getFilteredOverview(2L, List.of(5L, 6L, 8L), "", authorizationUser);

        List<OverviewId> expected = filterOutArchivedTeam(expectedOverviewIds.subList(5, 18), 6L);
        assertThat(expected).hasSameElementsAs(getOverviewIds(overviews));
    }

    @DisplayName("Should return correct overview on getFilteredOverview() when team ids are not set")
    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenTeamIdsEmpty() {
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(2L, List.of(), "", authorizationUser);

        List<OverviewId> expected = filterOutArchivedTeam(expectedOverviewIds.subList(0, 18), 6L);
        assertThat(expected).hasSameElementsAs(getOverviewIds(overviews));
    }

    @DisplayName("Should return correct overview on getFilteredOverview() when query is set")
    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenObjectiveQuery() {
        List<Overview> overviews = overviewPersistenceService
                .getFilteredOverview(2L, List.of(5L, 6L, 8L), "kundenzufriedenheit", authorizationUser);

        List<OverviewId> expected = filterOutArchivedTeam(expectedOverviewIds.subList(5, 8), 6L);
        assertThat(expected).hasSameElementsAs(getOverviewIds(overviews));
    }

    @DisplayName("Should return correct overview on getFilteredOverview() when quarter has no objectives")
    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenQuarterWithoutObjectives() {
        List<Overview> overviews = overviewPersistenceService
                .getFilteredOverview(3L, List.of(5L, 6L, 8L), null, authorizationUser);

        List<OverviewId> expected = filterOutArchivedTeam(expectedOverviewIds.subList(17, 20), 6L);
        assertThat(expected).hasSameElementsAs(getOverviewIds(overviews));
    }

    @DisplayName("Should return empty overview on getFilteredOverview() when query is set and no objectives are present")
    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenQuarterWithoutObjectivesAndObjectiveQuery() {
        List<Overview> overviews = overviewPersistenceService
                .getFilteredOverview(3L, List.of(5L, 6L, 8L), "kundenzufriedenheit", authorizationUser);

        assertTrue(overviews.isEmpty());
    }


    @DisplayName("Should not return archived team if quarter starts AFTER team was archived")
    @Test
    void shouldExcludeArchivedTeamWhenQuarterStartsAfterArchiveDate() {
        List<Overview> overviews = overviewPersistenceService
                .getFilteredOverview(3L, List.of(6L), "", authorizationUser);

        assertTrue(overviews.isEmpty(), "Team 6 should be filtered out because it is archived");
    }

    @DisplayName("Should return archived team if quarter starts BEFORE team was archived")
    @Test
    void shouldIncludeArchivedTeamWhenQuarterStartsBeforeArchiveDate() {
        List<Overview> overviews = overviewPersistenceService
                .getFilteredOverview(998L, List.of(6L), "", authorizationUser);

        assertThat(overviews).isNotEmpty();
        assertTrue(overviews.stream().allMatch(o -> Objects.equals(o.getOverviewId().getTeamId(), 6L)));
    }


    private List<OverviewId> getOverviewIds(List<Overview> overviewList) {
        return overviewList.stream().map(Overview::getOverviewId).toList();
    }

    /**
     * Helper to dynamically strip out an archived team's IDs from the expected data
     * so we don't have to break the hardcoded subList indices.
     */
    private List<OverviewId> filterOutArchivedTeam(List<OverviewId> baseList, Long teamIdToExclude) {
        return baseList.stream()
                .filter(id -> !Objects.equals(id.getTeamId(), teamIdToExclude))
                .collect(Collectors.toList());
    }
}