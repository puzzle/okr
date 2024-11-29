package ch.puzzle.okr.util.quarter.generate;

import java.time.LocalDate;

import org.springframework.context.annotation.Profile;

/**
 * Quarter data for H2 database.</br>
 * </br>
 * This class is used for testing purposes only. Do NOT use this class in production mode.
 */
@Profile("integration-test")
public record QuarterData(String label, LocalDate startDate, LocalDate endDate) {

    public String startDateAsIsoString() {
        return isoFormat(startDate);
    }

    public String endDateAsIsoString() {
        return isoFormat(endDate);
    }

    private String isoFormat(LocalDate date) {
        int year = date.getYear();
        String month = String.format("%02d", date.getMonthValue());
        String day = String.format("%02d", date.getDayOfMonth());
        return year + "-" + month + "-" + day;
    }

    @Override
    public String toString() {
        return "(" + "'" + label() + "', " + //
                "'" + startDateAsIsoString() + "', " + //
                "'" + endDateAsIsoString() + "'" + //
                ")";
    }

}
