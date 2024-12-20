package ch.puzzle.okr.util.quarter.generate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class QuartersTest {

    @DisplayName("currentQuarter() should find current quarter for now and 12 months in future")
    @ParameterizedTest
    @MethodSource("futureDatesAndLabels")
    void currentQuarterShouldFindCurrentQuarterForNowAnd12MonthsInFuture(LocalDate date, String expectedLabelFirstYear,
                                                                         String expectedLabelSecondYear) {

        // arrange
        Quarters nowQuarters = new Quarters(date.getYear());

        LocalDate in3Months = date.plusMonths(3);
        Quarters in3MonthQuarters = new Quarters(in3Months.getYear());

        // act
        QuarterData currentQuarter = nowQuarters.currentQuarter(date);
        QuarterData nextQuarter = in3MonthQuarters.currentQuarter(in3Months);

        // assert
        assertEquals(expectedLabelFirstYear, currentQuarter.toString());
        assertEquals(expectedLabelSecondYear, nextQuarter.toString());
    }

    private static Stream<Arguments> futureDatesAndLabels() {
        return Stream
                .of( //
                    Arguments
                            .of( //
                                LocalDate.of(2024, 7, 15), //
                                "('GJ 24/25-Q1', '2024-07-01', '2024-09-30')", //
                                "('GJ 24/25-Q2', '2024-10-01', '2024-12-31')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 8, 15), //
                                "('GJ 24/25-Q1', '2024-07-01', '2024-09-30')", //
                                "('GJ 24/25-Q2', '2024-10-01', '2024-12-31')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 9, 15), //
                                "('GJ 24/25-Q1', '2024-07-01', '2024-09-30')", //
                                "('GJ 24/25-Q2', '2024-10-01', '2024-12-31')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 10, 15), //
                                "('GJ 24/25-Q2', '2024-10-01', '2024-12-31')", //
                                "('GJ 24/25-Q3', '2025-01-01', '2025-03-31')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 11, 15), //
                                "('GJ 24/25-Q2', '2024-10-01', '2024-12-31')", //
                                "('GJ 24/25-Q3', '2025-01-01', '2025-03-31')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 12, 15), //
                                "('GJ 24/25-Q2', '2024-10-01', '2024-12-31')", //
                                "('GJ 24/25-Q3', '2025-01-01', '2025-03-31')"),
                    Arguments
                            .of( //
                                LocalDate.of(2025, 1, 15), //
                                "('GJ 24/25-Q3', '2025-01-01', '2025-03-31')", //
                                "('GJ 24/25-Q4', '2025-04-01', '2025-06-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2025, 2, 15), //
                                "('GJ 24/25-Q3', '2025-01-01', '2025-03-31')", //
                                "('GJ 24/25-Q4', '2025-04-01', '2025-06-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2025, 3, 15), //
                                "('GJ 24/25-Q3', '2025-01-01', '2025-03-31')", //
                                "('GJ 24/25-Q4', '2025-04-01', '2025-06-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2025, 4, 15), //
                                "('GJ 24/25-Q4', '2025-04-01', '2025-06-30')", //
                                "('GJ 25/26-Q1', '2025-07-01', '2025-09-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2025, 5, 15), //
                                "('GJ 24/25-Q4', '2025-04-01', '2025-06-30')", //
                                "('GJ 25/26-Q1', '2025-07-01', '2025-09-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2025, 6, 15), //
                                "('GJ 24/25-Q4', '2025-04-01', '2025-06-30')", //
                                "('GJ 25/26-Q1', '2025-07-01', '2025-09-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2025, 7, 15), //
                                "('GJ 25/26-Q1', '2025-07-01', '2025-09-30')", //
                                "('GJ 25/26-Q2', '2025-10-01', '2025-12-31')"));
    }

    @DisplayName("currentQuarter() should find current quarter for now and 7 months in past")
    @ParameterizedTest
    @MethodSource("pastDatesAndLabels")
    void currentQuarterShouldFindCurrentQuarterForNowAnd7MonthsInPast(LocalDate date, String expectedLabelFirstYear,
                                                                      String expectedLabelSecondYear) {

        // arrange
        Quarters nowQuarters = new Quarters(date.getYear());

        LocalDate in3Months = date.plusMonths(3);
        Quarters in3MonthsQuarters = new Quarters(in3Months.getYear());

        // act
        QuarterData currentQuarter = nowQuarters.currentQuarter(date);
        QuarterData nextQuarter = in3MonthsQuarters.currentQuarter(in3Months);

        // assert
        assertEquals(expectedLabelFirstYear, currentQuarter.toString());
        assertEquals(expectedLabelSecondYear, nextQuarter.toString());
    }

    private static Stream<Arguments> pastDatesAndLabels() {
        return Stream
                .of( //
                    Arguments
                            .of( //
                                LocalDate.of(2023, 12, 15), //
                                "('GJ 23/24-Q2', '2023-10-01', '2023-12-31')", //
                                "('GJ 23/24-Q3', '2024-01-01', '2024-03-31')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 1, 15), //
                                "('GJ 23/24-Q3', '2024-01-01', '2024-03-31')", //
                                "('GJ 23/24-Q4', '2024-04-01', '2024-06-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 2, 15), //
                                "('GJ 23/24-Q3', '2024-01-01', '2024-03-31')", //
                                "('GJ 23/24-Q4', '2024-04-01', '2024-06-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 3, 15), //
                                "('GJ 23/24-Q3', '2024-01-01', '2024-03-31')", //
                                "('GJ 23/24-Q4', '2024-04-01', '2024-06-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 4, 15), //
                                "('GJ 23/24-Q4', '2024-04-01', '2024-06-30')", //
                                "('GJ 24/25-Q1', '2024-07-01', '2024-09-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 5, 15), //
                                "('GJ 23/24-Q4', '2024-04-01', '2024-06-30')", //
                                "('GJ 24/25-Q1', '2024-07-01', '2024-09-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 6, 15), //
                                "('GJ 23/24-Q4', '2024-04-01', '2024-06-30')", //
                                "('GJ 24/25-Q1', '2024-07-01', '2024-09-30')"),
                    Arguments
                            .of( //
                                LocalDate.of(2024, 7, 15), //
                                "('GJ 24/25-Q1', '2024-07-01', '2024-09-30')", //
                                "('GJ 24/25-Q2', '2024-10-01', '2024-12-31')"));
    }

    @DisplayName("currentQuarter() should throw exception if no matching quarter is found for now")
    @Test
    void currentQuarterShouldThrowExceptionIfNoMatchingQuarterIsFoundForNow() {
        // arrange
        LocalDate now = LocalDate.of(2024, 7, 15);
        Quarters allQuartersOfCurrentYear = new Quarters(now.getYear());

        LocalDate in2Years = now.plusYears(2);

        // act
        RuntimeException exception = assertThrows(RuntimeException.class,
                                                  () -> allQuartersOfCurrentYear.currentQuarter(in2Years));

        // assert
        assertEquals(RuntimeException.class, exception.getClass());
        assertEquals("No current quarter found for 2026-07-15", exception.getMessage());
    }

    @DisplayName("currentQuarter() should find current quarter for boundary dates")
    @ParameterizedTest
    @MethodSource("boundaryDatesAndLabels")
    void currentQuarterShouldFindCurrentQuarterForBoundaryDates(LocalDate date, String expectedLabel) {
        // arrange
        Quarters allQuartersForYearOfNow = new Quarters(date.getYear());

        // act
        QuarterData currentQuarter = allQuartersForYearOfNow.currentQuarter(date);

        // assert
        assertEquals(expectedLabel, currentQuarter.toString());
    }

    private static Stream<Arguments> boundaryDatesAndLabels() {
        return Stream
                .of( //
                    Arguments
                            .of( //
                                LocalDate.of(2024, 7, 1), //
                                "('GJ 24/25-Q1', '2024-07-01', '2024-09-30')"), //
                    Arguments
                            .of( //
                                LocalDate.of(2024, 9, 30), //
                                "('GJ 24/25-Q1', '2024-07-01', '2024-09-30')") //
                );
    }
}
