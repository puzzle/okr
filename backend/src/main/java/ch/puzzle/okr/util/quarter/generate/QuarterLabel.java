package ch.puzzle.okr.util.quarter.generate;

import java.time.LocalDate;

public class QuarterLabel {
    private final LocalDate date;

    public QuarterLabel(LocalDate date) {
        this.date = date;
    }

    public String label() {
        return "GJ " + //
                formatYearAs2Digits(firstYearOfGeschaeftsJahr()) + "/" + //
                formatYearAs2Digits(secondYearOfGeschaeftsJahr()) + "-Q" + //
                getQuarterDigit();
    }

    private int getQuarterDigit() {
        int month = date.getMonthValue();
        if (month < 4)
            return 3;
        if (month < 7)
            return 4;
        if (month < 10)
            return 1;
        return 2;
    }

    private int firstYearOfGeschaeftsJahr() {
        if (isNowInNewGeschaeftsJahr()) {
            return date.getYear();
        }
        return date.getYear() - 1;
    }

    private boolean isNowInNewGeschaeftsJahr() {
        LocalDate startNewGJ = startOfNewGeschaeftsJahr();
        return date.equals(startNewGJ) || date.isAfter(startNewGJ);
    }

    private int secondYearOfGeschaeftsJahr() {
        return firstYearOfGeschaeftsJahr() + 1;
    }

    private int formatYearAs2Digits(int year) {
        return year % 1000;
    }

    private LocalDate startOfNewGeschaeftsJahr() {
        return LocalDate.of(date.getYear(), 7, 1);
    }
}
