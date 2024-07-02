package ch.puzzle.okr.util;

import ch.puzzle.okr.models.Quarter;

import java.time.LocalDate;

public class QuarterRangeChecker {

    public static boolean nowIsInQuarter(LocalDate now, Quarter quarter) {
        if (now == null || quarter == null) {
            return false;
        }
        return nowMatchesWithStartOrEndDate(now, quarter) || nowIsBetweenStartAndEndDate(now, quarter);
    }

    private static boolean nowMatchesWithStartOrEndDate(LocalDate now, Quarter quarter) {
        return now.isEqual(quarter.getStartDate()) || now.isEqual(quarter.getEndDate());
    }

    private static boolean nowIsBetweenStartAndEndDate(LocalDate now, Quarter quarter) {
        return now.isAfter(quarter.getStartDate()) && now.isBefore(quarter.getEndDate());
    }
}
