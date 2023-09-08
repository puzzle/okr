package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.Overview;
import ch.puzzle.okr.models.OverviewId;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringIntegrationTest
public class OverviewPersistenceServiceIT {
    @Autowired
    private OverviewPersistenceService overviewPersistenceService;
    @Autowired
    private OverviewMapper overviewMapper;

    @Test
    @Disabled
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnOverviews() {
        List<Overview> overviews = overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(2L, List.of(5L, 6L, 8L));

        assertEquals(15, overviews.size());
        overviews.forEach(overview -> assertTrue(matchOverviewId(overview.getOverviewId())));
    }

    private boolean matchOverviewId(OverviewId overviewId) {
        return getExpectedOverviewIds().anyMatch(id -> id.equals(overviewId));
    }

    private static Stream<OverviewId> getExpectedOverviewIds() {
        return Stream.of(OverviewId.of(5L, 3L, 3L, 9L), //
                OverviewId.of(5L, 3L, 3L, 9L), //
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
                OverviewId.of(6L, 10L, -1L, -1L));
    }
}
