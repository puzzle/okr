package ch.puzzle.okr.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class QuartersSqlGeneratorTest {

    private final String expectedSql = """
            insert into quarter (id, label, start_date, end_date)
            values
            (2, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30'),
            (3, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31'),
            (99, 'GJ ForTests', '2000-07-01', '2000-09-30'),
            (999, 'Backlog', null, null);""";

    @Test
    void generateSqlShouldReturnSqlToCreateQuarters() {
        // arrange
        LocalDate now = LocalDate.of(2024, 7, 15);
        QuartersSqlGenerator quartersSqlGenerator = new QuartersSqlGenerator(now);

        // act
        String sql = quartersSqlGenerator.generateSql();

        // assert
        Assertions.assertEquals(expectedSql, sql);
    }
}
