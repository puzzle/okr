package ch.puzzle.okr.util.quarter.generate;

import java.time.LocalDate;
import org.springframework.context.annotation.Profile;

/**
 * Quarter label for H2 database.</br>
 * </br>
 * This class is used for testing purposes only. Do NOT use this class in
 * production mode.
 */
@Profile("integration-test")
public class QuarterLabel {
    private final LocalDate date;

    public QuarterLabel(LocalDate date) {
        this.date = date;
    }

    public String label() {
        return "GJ " + //
               formatYearAs2Digits(firstYearOfFiscalYear()) + "/" + //
               formatYearAs2Digits(secondYearOfFiscalYear()) + "-Q" + //
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

    private int firstYearOfFiscalYear() {
        if (isNowInNewFiscalYear()) {
            return date.getYear();
        }
        return date.getYear() - 1;
    }

    private boolean isNowInNewFiscalYear() {
        LocalDate startNewGJ = startOfNewFiscalYear();
        return date.equals(startNewGJ) || date.isAfter(startNewGJ);
    }

    private int secondYearOfFiscalYear() {
        return firstYearOfFiscalYear() + 1;
    }

    private int formatYearAs2Digits(int year) {
        return year % 1000;
    }

    private LocalDate startOfNewFiscalYear() {
        return LocalDate.of(date.getYear(), 7, 1);
    }
}
