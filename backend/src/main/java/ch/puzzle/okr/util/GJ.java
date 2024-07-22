package ch.puzzle.okr.util;

import java.time.LocalDate;

public class GJ {
    private final LocalDate now;
    private final Quarters quarters;

    public GJ(LocalDate now) {
        this.now = now;
        this.quarters = new Quarters(now);
    }

    public String getLabel() {
        return "GJ " + //
                formatYearAs2Digits(firstYearOfGJ()) + "/" + //
                formatYearAs2Digits(secondYearOfGJ()) + "-Q" + //
                quarters.currentQuarter().quarterDigit();
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
