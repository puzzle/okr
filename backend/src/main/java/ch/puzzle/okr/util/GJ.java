package ch.puzzle.okr.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GJ {
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

    public GJ(LocalDate now) {
        this.now = now;
        calculateQuartersOfGJ();
    }

    private void calculateQuartersOfGJ() {
        quarters.add(new QuarterData(1, date(Q1_START), date(Q1_END)));
        quarters.add(new QuarterData(2, date(Q2_START), date(Q2_END)));
        quarters.add(new QuarterData(3, date(Q3_START), date(Q3_END)));
        quarters.add(new QuarterData(4, date(Q4_START), date(Q4_END)));
    }

    private LocalDate date(String monthDay) {
        return LocalDate.parse(now.getYear() + "-" + monthDay);
    }

    public QuarterData getCurrentQuarter() {
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

    public String getLabel() {
        return "GJ " + //
                formatYearAs2Digits(firstYearOfGJ()) + "/" + //
                formatYearAs2Digits(secondYearOfGJ()) + "-Q" + //
                getCurrentQuarter().quarterDigit();
    }

    private int firstYearOfGJ() {
        if (isNowInNewGJ())
            return now.getYear();
        return now.getYear() - 1;
    }

    private boolean isNowInNewGJ() {
        LocalDate startNewGJ = startOfNewGJ();
        return now.equals(startNewGJ) || now.isAfter(startNewGJ);
    }

    private int secondYearOfGJ() {
        return firstYearOfGJ() + 1;
    }

    private int formatYearAs2Digits(int year) {
        return year % 1000;
    }

    private LocalDate startOfNewGJ() {
        return LocalDate.of(now.getYear(), 7, 1);
    }
}
