package ch.puzzle.okr.util.quarter.generate.h2;

import ch.puzzle.okr.util.quarter.generate.QuarterData;
import ch.puzzle.okr.util.quarter.generate.Quarters;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class QuarterFunction {
    public static final int CURRENT_QUARTER_DB_ID = 2;
    public static final int NEXT_QUARTER_DB_ID = 3;

    private static final Map<Integer, QuarterData> QUARTERS = new HashMap<>();

    public static void initQuarterData() {
        LocalDate now = LocalDate.now();
        registerCurrentQuarterForDate(now, CURRENT_QUARTER_DB_ID);

        LocalDate in3Months = now.plusMonths(3);
        registerCurrentQuarterForDate(in3Months, NEXT_QUARTER_DB_ID);
    }

    private static void registerCurrentQuarterForDate(LocalDate date, int dbId) {
        Quarters quarters = new Quarters(date.getYear());
        QuarterData currentQuarter = quarters.currentQuarter(date);
        QUARTERS.put(dbId, currentQuarter);
    }

    public static String currentQuarterLabel() {
        return QUARTERS.get(CURRENT_QUARTER_DB_ID).label();
    }

    public static String currentQuarterStartDate() {
        return QUARTERS.get(CURRENT_QUARTER_DB_ID).startDateAsIsoString();
    }

    public static String currentQuarterEndDate() {
        return QUARTERS.get(CURRENT_QUARTER_DB_ID).endDateAsIsoString();
    }

    public static String nextQuarterLabel() {
        return QUARTERS.get(NEXT_QUARTER_DB_ID).label();
    }

    public static String nextQuarterStartDate() {
        return QUARTERS.get(NEXT_QUARTER_DB_ID).startDateAsIsoString();
    }

    public static String nextQuarterEndDate() {
        return QUARTERS.get(NEXT_QUARTER_DB_ID).endDateAsIsoString();
    }

}
