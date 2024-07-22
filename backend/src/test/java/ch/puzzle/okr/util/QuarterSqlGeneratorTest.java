package ch.puzzle.okr.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuarterSqlGeneratorTest {

    private static final LocalDate START_DATE = LocalDate.of(2024, 7, 15);
    private static final LocalDate START_DATE_7_MONTHS_BEFORE = START_DATE.minusMonths(7);

    @ParameterizedTest
    @MethodSource("indexAndFutureLabels")
    void nowAnd12MonthsInFuture(int index, String expectedLabelFirstYear, String expectedLabelSecondYear) {
        LocalDate now = START_DATE.plusMonths(index);
        QuarterSqlGenerator nowSqlGenerator = new QuarterSqlGenerator(now);
        String sqlNow = nowSqlGenerator.quarterSqlData(index).toSqlString();

        assertEquals(expectedLabelFirstYear, sqlNow);

        LocalDate in3Months = now.plusMonths(3);
        QuarterSqlGenerator in3MonthsSqlGenerator = new QuarterSqlGenerator(in3Months);
        String sqlIn3Months = in3MonthsSqlGenerator.quarterSqlData(index).toSqlString();

        assertEquals(expectedLabelSecondYear, sqlIn3Months);
    }

    public static Stream<Arguments> indexAndFutureLabels() {
        return Stream.of(
                // 2024-07-15
                Arguments.of(0, //
                        "(0, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30')",
                        "(0, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31')"),
                // 2024-08-15
                Arguments.of(1, //
                        "(1, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30')",
                        "(1, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31')"),
                // 2024-09-15
                Arguments.of(2, //
                        "(2, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30')",
                        "(2, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31')"),
                // 2024-10-15
                Arguments.of(3, //
                        "(3, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31')",
                        "(3, 'GJ 24/25-Q3', '2025-01-01', '2025-03-31')"),
                // 2024-11-15
                Arguments.of(4, //
                        "(4, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31')",
                        "(4, 'GJ 24/25-Q3', '2025-01-01', '2025-03-31')"),
                // 2024-12-15
                Arguments.of(5, //
                        "(5, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31')",
                        "(5, 'GJ 24/25-Q3', '2025-01-01', '2025-03-31')"),
                // 2025-01-15
                Arguments.of(6, //
                        "(6, 'GJ 24/25-Q3', '2025-01-01', '2025-03-31')",
                        "(6, 'GJ 24/25-Q4', '2025-04-01', '2025-06-30')"),
                // 2025-02-15
                Arguments.of(7, //
                        "(7, 'GJ 24/25-Q3', '2025-01-01', '2025-03-31')",
                        "(7, 'GJ 24/25-Q4', '2025-04-01', '2025-06-30')"),
                // 2025-03-15
                Arguments.of(8, //
                        "(8, 'GJ 24/25-Q3', '2025-01-01', '2025-03-31')",
                        "(8, 'GJ 24/25-Q4', '2025-04-01', '2025-06-30')"),
                // 2025-04-15
                Arguments.of(9, //
                        "(9, 'GJ 24/25-Q4', '2025-04-01', '2025-06-30')",
                        "(9, 'GJ 25/26-Q1', '2025-07-01', '2025-09-30')"),
                // 2025-05-15
                Arguments.of(10, //
                        "(10, 'GJ 24/25-Q4', '2025-04-01', '2025-06-30')",
                        "(10, 'GJ 25/26-Q1', '2025-07-01', '2025-09-30')"),
                // 2025-06-15
                Arguments.of(11, //
                        "(11, 'GJ 24/25-Q4', '2025-04-01', '2025-06-30')",
                        "(11, 'GJ 25/26-Q1', '2025-07-01', '2025-09-30')"),
                // 2025-07-15
                Arguments.of(12, //
                        "(12, 'GJ 25/26-Q1', '2025-07-01', '2025-09-30')",
                        "(12, 'GJ 25/26-Q2', '2025-10-01', '2025-12-31')"));

    }

    @ParameterizedTest
    @MethodSource("indexAndPastLabels")
    void nowAnd7MonthsInPast(int index, String expectedLabelFirstYear, String expectedLabelSecondYear) {
        LocalDate now = START_DATE_7_MONTHS_BEFORE.plusMonths(index);
        QuarterSqlGenerator nowSqlGenerator = new QuarterSqlGenerator(now);
        String sqlNow = nowSqlGenerator.quarterSqlData(index).toSqlString();

        assertEquals(expectedLabelFirstYear, sqlNow);

        LocalDate in3Months = now.plusMonths(3);
        QuarterSqlGenerator in3MonthsSqlGenerator = new QuarterSqlGenerator(in3Months);
        String sqlIn3Months = in3MonthsSqlGenerator.quarterSqlData(index).toSqlString();

        assertEquals(expectedLabelSecondYear, sqlIn3Months);
    }

    public static Stream<Arguments> indexAndPastLabels() {
        return Stream.of(
                // 2023-12-15
                Arguments.of(0, //
                        "(0, 'GJ 23/24-Q2', '2023-10-01', '2023-12-31')", //
                        "(0, 'GJ 23/24-Q3', '2024-01-01', '2024-03-31')" //
                ),
                // 2024-01-15
                Arguments.of(1, //
                        "(1, 'GJ 23/24-Q3', '2024-01-01', '2024-03-31')", //
                        "(1, 'GJ 23/24-Q4', '2024-04-01', '2024-06-30')" //
                ),
                // 2024-02-15
                Arguments.of(2, //
                        "(2, 'GJ 23/24-Q3', '2024-01-01', '2024-03-31')", //
                        "(2, 'GJ 23/24-Q4', '2024-04-01', '2024-06-30')" //
                ),
                // 2024-03-15
                Arguments.of(3, //
                        "(3, 'GJ 23/24-Q3', '2024-01-01', '2024-03-31')", //
                        "(3, 'GJ 23/24-Q4', '2024-04-01', '2024-06-30')" //
                ),
                // 2024-04-15
                Arguments.of(4, //
                        "(4, 'GJ 23/24-Q4', '2024-04-01', '2024-06-30')", //
                        "(4, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30')" //
                ),
                // 2024-05-15
                Arguments.of(5, //
                        "(5, 'GJ 23/24-Q4', '2024-04-01', '2024-06-30')", //
                        "(5, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30')" //
                ),
                // 2024-06-15
                Arguments.of(6, //
                        "(6, 'GJ 23/24-Q4', '2024-04-01', '2024-06-30')", //
                        "(6, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30')" //
                ),
                // 2024-07-15
                Arguments.of(7, //
                        "(7, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30')", //
                        "(7, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31')" //
                ));
    }

}
