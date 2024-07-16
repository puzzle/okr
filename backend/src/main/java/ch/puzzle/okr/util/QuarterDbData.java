package ch.puzzle.okr.util;

import java.time.LocalDate;

public record QuarterDbData(long id, LocalDate now, QuarterData quarterSqlData) {

    public String toSqlString() {
        return "(" + id + ", '" + //
                label() + "', " + //
                "'" + isoFormat(quarterSqlData.start()) + "', " + //
                "'" + isoFormat(quarterSqlData.end()) + "'" + //
                ")";
    }

    private String isoFormat(LocalDate date) {
        int year = date.getYear();
        String month = String.format("%02d", date.getMonthValue());
        String day = String.format("%02d", date.getDayOfMonth());
        return year + "-" + month + "-" + day;
    }

    private String label() {
        return new GJ(now).getLabel();
    }
}
