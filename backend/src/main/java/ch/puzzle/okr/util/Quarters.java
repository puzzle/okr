package ch.puzzle.okr.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Quarters {
    public static final String Q1_START = "07-01";
    public static final String Q1_END = "09-30";
    public static final String Q2_START = "10-01";
    public static final String Q2_END = "12-31";
    public static final String Q3_START = "01-01";
    public static final String Q3_END = "03-31";
    public static final String Q4_START = "04-01";
    public static final String Q4_END = "06-30";

    private final LocalDate now;
    private final List<QuarterData> quarters = new ArrayList<>();

    public Quarters(LocalDate now) {
        this.now = now;
        calculateQuarters();
    }

    private void calculateQuarters() {
        quarters.add(new QuarterData(date(Q1_START), date(Q1_END)));
        quarters.add(new QuarterData(date(Q2_START), date(Q2_END)));
        quarters.add(new QuarterData(date(Q3_START), date(Q3_END)));
        quarters.add(new QuarterData(date(Q4_START), date(Q4_END)));
    }

    private LocalDate date(String monthDay) {
        return LocalDate.parse(now.getYear() + "-" + monthDay);
    }

    public QuarterDataWithGJ currentQuarterWithGJ() {
        for (QuarterData quarter : quarters) {
            if (isInQuarter(quarter.start(), quarter.end())) {
                return new QuarterDataWithGJ(new GJ(now), quarter);
            }
        }
        return null;
    }

    public QuarterData currentQuarter() {
        for (QuarterData quarter : quarters) {
            if (isInQuarter(quarter.start(), quarter.end())) {
                return quarter;
            }
        }
        return null;
    }

    private boolean isInQuarter(LocalDate start, LocalDate end) {
        boolean isAfterStart = now.equals(start) || now.isAfter(start);
        boolean isBeforeEnd = now.isBefore(end) || now.equals(end);
        return isAfterStart && isBeforeEnd;
    }

}
