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
        Quarters quarters = new Quarters(now.getYear());
        QuarterData currentQuarter = quarters.currentQuarter(now);
        QUARTERS.put(CURRENT_QUARTER_DB_ID, currentQuarter);

        LocalDate in3Months = now.plusMonths(3);
        Quarters nextQuarters = new Quarters(in3Months.getYear());
        QuarterData nextQuarter = nextQuarters.currentQuarter(in3Months);
        QUARTERS.put(NEXT_QUARTER_DB_ID, nextQuarter);
    }

    public static String currentQuarterLabel() {
        return QUARTERS.get(CURRENT_QUARTER_DB_ID).label();
    }

    public static String currentQuarterStartDate() {
        return QUARTERS.get(CURRENT_QUARTER_DB_ID).start();
    }

    public static String currentQuarterEndDate() {
        return QUARTERS.get(CURRENT_QUARTER_DB_ID).end();
    }

    public static String nextQuarterLabel() {
        return QUARTERS.get(NEXT_QUARTER_DB_ID).label();
    }

    public static String nextQuarterStartDate() {
        return QUARTERS.get(NEXT_QUARTER_DB_ID).start();
    }

    public static String nextQuarterEndDate() {
        return QUARTERS.get(NEXT_QUARTER_DB_ID).end();
    }

    public static void main(String[] args) {
        QuarterFunction.initQuarterData();
        String q2 = "(2, " + currentQuarterLabel() + ", " + currentQuarterStartDate() + ", " + currentQuarterEndDate()
                + ")";
        String q3 = "(3, " + nextQuarterLabel() + ", " + nextQuarterStartDate() + ", " + nextQuarterEndDate() + ")";
        System.out.println(q2);
        System.out.println(q3);
    }
}
