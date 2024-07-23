package ch.puzzle.okr.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Quarters {
    private record QuarterDateRange(LocalDate startDate, LocalDate endDate) {
    }

    public static final String Q1_START = "07-01";
    public static final String Q1_END = "09-30";
    public static final String Q2_START = "10-01";
    public static final String Q2_END = "12-31";
    public static final String Q3_START = "01-01";
    public static final String Q3_END = "03-31";
    public static final String Q4_START = "04-01";
    public static final String Q4_END = "06-30";

    private final List<QuarterDateRange> quarters = new ArrayList<>();

    public Quarters(int year) {
        createQuarters(year);
    }

    private void createQuarters(int year) {
        quarters.add(createQuarter(year, Q1_START, Q1_END));
        quarters.add(createQuarter(year, Q2_START, Q2_END));
        quarters.add(createQuarter(year, Q3_START, Q3_END));
        quarters.add(createQuarter(year, Q4_START, Q4_END));
    }

    private QuarterDateRange createQuarter(int year, String startMonthDay, String endMonthDay) {
        LocalDate startDate = date(year, startMonthDay);
        LocalDate endDate = date(year, endMonthDay);
        return new QuarterDateRange(startDate, endDate);
    }

    private LocalDate date(int year, String monthDay) {
        return LocalDate.parse(year + "-" + monthDay);
    }

    public QuarterData currentQuarterWithGJ(LocalDate now) {
        for (QuarterDateRange quarter : quarters) {
            if (isInQuarter(now, quarter.startDate(), quarter.endDate())) {
                String label = new GJ(now).getLabel();
                return new QuarterData(label, quarter.startDate(), quarter.endDate());
            }
        }
        return null;
    }

    private boolean isInQuarter(LocalDate now, LocalDate start, LocalDate end) {
        boolean isAfterStart = now.equals(start) || now.isAfter(start);
        boolean isBeforeEnd = now.isBefore(end) || now.equals(end);
        return isAfterStart && isBeforeEnd;
    }

}
