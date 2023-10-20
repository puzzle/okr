package ch.puzzle.okr;

import java.util.List;

public class OverviewTestHelper {
    public static final long QUARTER_ID = 1L;
    public static final long TEAM_ID = 1L;

    public static final List<Long> teamIds = List.of(1L, 2L, 3L, 4L);

    private OverviewTestHelper() {
        throw new IllegalStateException("Utility class");
    }
}
