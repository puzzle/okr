package ch.puzzle.okr.mapper.checkin.helper;

import java.time.LocalDateTime;
import java.time.Month;

import ch.puzzle.okr.models.checkin.Zone;

public class TestDataConstants {
    public static final boolean CHECK_IN_IS_WRITEABLE = true;
    public static final long CHECK_IN_ID = 0L;
    public static final int CHECK_IN_VERSION = 1;
    public static final String CHECK_IN_CHANGE_INFO = "change info";
    public static final String CHECK_IN_INITIATIVES = "initiatives";
    public static final int CHECK_IN_CONFIDENCE = 30;
    public static final double CHECK_IN_METRIC_VALUE = 23;
    public static final Zone CHECK_IN_ORDINAL_ZONE = Zone.STRETCH;
    public static final LocalDateTime CHECK_IN_CREATE_DATE_TIME = LocalDateTime.of(2024, Month.MAY, 20, 12, 35, 0);
    public static final LocalDateTime CHECK_IN_MODIFIED_DATE_TIME = LocalDateTime.of(2024, Month.MAY, 21, 8, 0, 0);
    public static final long KEY_RESULT_ID = 20L;
}
