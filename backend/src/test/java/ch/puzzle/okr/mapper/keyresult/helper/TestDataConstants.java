package ch.puzzle.okr.mapper.keyresult.helper;

import ch.puzzle.okr.models.State;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.test.TestHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class TestDataConstants {

    public static final long CHECK_IN_ID = 100L;
    public static final int CHECK_IN_VERSION = 1;
    public static final double CHECK_IN_METRIC_VALUE = 23;
    public static final Zone CHECK_IN_ORDINAL_ZONE = Zone.STRETCH;
    public static final int CHECK_IN_CONFIDENCE = 5;
    public static final String CHECK_IN_CHANGE_INFO = "checkin changeinfo";
    public static final String CHECK_IN_INITIATIVES = "checkin initiaitves";
    public static final LocalDateTime CHECK_IN_CREATED_ON = LocalDateTime.of(2024, Month.MAY, 20, 0, 0, 0);
    public static final LocalDateTime CHECK_IN_MODIFIED_ON = LocalDateTime.of(2024, Month.MAY, 20, 12, 0, 0);
    public static final boolean CHECK_IN_IS_WRITABLE = true;

    public static final long USER_ID = 101L;
    public static final String USER_FIRST_NAME = "owner firstname";
    public static final String USER_LAST_NAME = "owner lastname";

    public static final long QUARTER_ID = 102L;
    public static final String QUARTER_LABEL = "GJ 2024 Q1";
    public static final LocalDate QUARTER_START_DATE = LocalDate.of(2024, Month.MAY, 20);
    public static final LocalDate QUARTER_END_DATE = LocalDate.of(2024, Month.MAY, 21);

    public static final long OBJECTIVE_ID = 103L;
    public static final State OBJECTIVE_STATE = State.ONGOING;

    public static final long KEY_RESULT_ID = 23L;
    public static final int KEY_RESULT_VERSION = 1;
    public static final String KEY_RESULT_TITLE = "keyresult title";
    public static final String KEY_RESULT_DESCRIPTION = "keyresult description";
    public static final double KEY_RESULT_BASELINE = 55D;
    public static final double KEY_RESULT_STRETCH_GOAL = 80D;
    public static final Unit KEY_RESULT_UNIT = TestHelper.NUMBER_UNIT;

    public static final String KEY_RESULT_COMMIT_ZONE = "commit zone";
    public static final String KEY_RESULT_TARGET_ZONE = "target zone";
    public static final String KEY_RESULT_STRETCH_ZONE = "stretch zone";

    public static final boolean KEY_RESULT_IS_WRITABLE = true;

    public static final long ACTION_ID = 104L;
    public static final int ACTION_VERSION = 1;
    public static final String ACTION_ACTION = "action";
    public static final int ACTION_PRIORITY = 2;
    public static final boolean ACTION_IS_CHECKED = true;

}
