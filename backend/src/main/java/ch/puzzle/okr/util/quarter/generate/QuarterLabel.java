package ch.puzzle.okr.util.quarter.generate;

import java.time.LocalDate;

public class QuarterLabel {
    private final LocalDate now;

    public QuarterLabel(LocalDate now) {
        this.now = now;
    }

    public String label() {
        return "GJ " + //
                formatYearAs2Digits(firstYearOfGeschaeftsJahr()) + "/" + //
                formatYearAs2Digits(secondYearOfGeschaeftsJahr()) + "-Q" + //
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

    private int firstYearOfGeschaeftsJahr() {
        if (isNowInNewGeschaeftsJahr())
            return now.getYear();
        return now.getYear() - 1;
    }

    private boolean isNowInNewGeschaeftsJahr() {
        LocalDate startNewGJ = startOfNewGeschaeftsJahr();
        return now.equals(startNewGJ) || now.isAfter(startNewGJ);
    }

    private int secondYearOfGeschaeftsJahr() {
        return firstYearOfGeschaeftsJahr() + 1;
    }

    private int formatYearAs2Digits(int year) {
        return year % 1000;
    }

    private LocalDate startOfNewGeschaeftsJahr() {
        return LocalDate.of(now.getYear(), 7, 1);
    }
}
