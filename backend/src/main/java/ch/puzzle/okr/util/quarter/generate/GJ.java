package ch.puzzle.okr.util.quarter.generate;

import java.time.LocalDate;

public class GJ {
    private final LocalDate now;

    public GJ(LocalDate now) {
        this.now = now;
    }

    public String getLabel() {
        return "GJ " + //
                formatYearAs2Digits(firstYearOfGJ()) + "/" + //
                formatYearAs2Digits(secondYearOfGJ()) + "-Q" + //
                getQuarterDigit();
    }

    private int getQuarterDigit() {
        int month = now.getMonthValue();
        if (month < 4)
            return 3;
        if (month < 7)
            return 4;
        if (month < 10)
            return 1;
        return 2;
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
