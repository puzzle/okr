package ch.puzzle.okr.util.quarter.generate;

import java.time.LocalDate;

public record QuarterData(String label, LocalDate startDate, LocalDate endDate) {

    public String start() {
        return isoFormat(startDate);
    }

    public String end() {
        return isoFormat(endDate);
    }

    @Override
    public String toString() {
        return "(" + "'" + label() + "', " + //
                "'" + isoFormat(startDate) + "', " + //
                "'" + isoFormat(endDate) + "'" + //
                ")";
    }

    private String isoFormat(LocalDate date) {
        int year = date.getYear();
        String month = String.format("%02d", date.getMonthValue());
        String day = String.format("%02d", date.getDayOfMonth());
        return year + "-" + month + "-" + day;
    }
}
