package ch.puzzle.okr.util;

import java.time.LocalDate;

public class QuartersSqlGenerator {

    public static final int CURRENT_QUARTER_DB_ID = 2;
    public static final int NEXT_QUARTER_DB_ID = 3;

    private final LocalDate now;

    public QuartersSqlGenerator(LocalDate now) {
        this.now = now;
    }

    public String generateSql() {
        QuarterSqlGenerator currentQuarterSqlGenerator = new QuarterSqlGenerator(now);
        QuarterDbData currentQuarter = currentQuarterSqlGenerator.quarterSqlData(CURRENT_QUARTER_DB_ID);

        LocalDate in3Months = now.plusMonths(3);
        QuarterSqlGenerator nextQuarterSqlGenerator = new QuarterSqlGenerator(in3Months);
        QuarterDbData nextQuarter = nextQuarterSqlGenerator.quarterSqlData(NEXT_QUARTER_DB_ID);

        return "insert into quarter (id, label, start_date, end_date)\n" + "values\n" + currentQuarter.toSqlString()
                + "," + "\n" + nextQuarter.toSqlString() + "," + "\n"
                + "(99, 'GJ ForTests', '2000-07-01', '2000-09-30'),\n" + "(999, 'Backlog', null, null);";

    }
}
