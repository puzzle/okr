package ch.puzzle.okr.util;

import java.time.LocalDate;

public record QuarterDataWithGJ(GJ gj, QuarterData quarterData) {

    @Override
    public String toString() {
        return "(" +
                "'" + gj.getLabel() + "', " + //
                "'" + isoFormat(quarterData.start()) + "', " + //
                "'" + isoFormat(quarterData.end()) + "'" + //
                ")";
    }

    private String isoFormat(LocalDate date) {
        int year = date.getYear();
        String month = String.format("%02d", date.getMonthValue());
        String day = String.format("%02d", date.getDayOfMonth());
        return year + "-" + month + "-" + day;
    }
}
