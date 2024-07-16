package ch.puzzle.okr.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GJTest {

    @ParameterizedTest
    @MethodSource("nowAndLabels")
    void getLabelShouldReturnLabelWithYearAndQuarterInfo(LocalDate now, String expectedLabel) {
        GJ gj = new GJ(now);
        Assertions.assertEquals(expectedLabel, gj.getLabel());
    }

    private static Stream<Arguments> nowAndLabels() {
        return Stream.of(Arguments.of(LocalDate.of(2024, 7, 1), "GJ 24/25-Q1"),
                Arguments.of(LocalDate.of(2024, 7, 2), "GJ 24/25-Q1"),
                Arguments.of(LocalDate.of(2024, 7, 15), "GJ 24/25-Q1"),
                Arguments.of(LocalDate.of(2024, 9, 15), "GJ 24/25-Q1"),
                Arguments.of(LocalDate.of(2024, 9, 29), "GJ 24/25-Q1"),
                Arguments.of(LocalDate.of(2024, 9, 30), "GJ 24/25-Q1"),

                Arguments.of(LocalDate.of(2024, 10, 1), "GJ 24/25-Q2"),
                Arguments.of(LocalDate.of(2024, 10, 2), "GJ 24/25-Q2"),
                Arguments.of(LocalDate.of(2024, 10, 15), "GJ 24/25-Q2"),
                Arguments.of(LocalDate.of(2024, 12, 15), "GJ 24/25-Q2"),
                Arguments.of(LocalDate.of(2024, 12, 30), "GJ 24/25-Q2"),
                Arguments.of(LocalDate.of(2024, 12, 31), "GJ 24/25-Q2"),

                Arguments.of(LocalDate.of(2024, 1, 1), "GJ 23/24-Q3"),
                Arguments.of(LocalDate.of(2024, 1, 2), "GJ 23/24-Q3"),
                Arguments.of(LocalDate.of(2024, 1, 15), "GJ 23/24-Q3"),
                Arguments.of(LocalDate.of(2024, 3, 15), "GJ 23/24-Q3"),
                Arguments.of(LocalDate.of(2024, 3, 30), "GJ 23/24-Q3"),
                Arguments.of(LocalDate.of(2024, 3, 31), "GJ 23/24-Q3"),

                Arguments.of(LocalDate.of(2024, 4, 1), "GJ 23/24-Q4"),
                Arguments.of(LocalDate.of(2024, 4, 2), "GJ 23/24-Q4"),
                Arguments.of(LocalDate.of(2024, 4, 15), "GJ 23/24-Q4"),
                Arguments.of(LocalDate.of(2024, 6, 15), "GJ 23/24-Q4"),
                Arguments.of(LocalDate.of(2024, 6, 29), "GJ 23/24-Q4"),
                Arguments.of(LocalDate.of(2024, 6, 30), "GJ 23/24-Q4"));
    }

    @ParameterizedTest
    @MethodSource("nowAndQuarterDigit")
    void getCurrentQuarterShouldReturnCurrentQuarterAsQuarterData(LocalDate now, int expectedQuarterDigit) {
        GJ gj = new GJ(now);
        assertEquals(expectedQuarterDigit, gj.getCurrentQuarter().quarterDigit());
    }

    private static Stream<Arguments> nowAndQuarterDigit() {
        return Stream.of(Arguments.of(LocalDate.of(2024, 7, 1), 1), Arguments.of(LocalDate.of(2024, 7, 2), 1),
                Arguments.of(LocalDate.of(2024, 9, 29), 1), Arguments.of(LocalDate.of(2024, 9, 30), 1),

                Arguments.of(LocalDate.of(2024, 10, 1), 2), Arguments.of(LocalDate.of(2024, 10, 2), 2),
                Arguments.of(LocalDate.of(2024, 12, 30), 2), Arguments.of(LocalDate.of(2024, 12, 31), 2),

                Arguments.of(LocalDate.of(2024, 1, 1), 3), Arguments.of(LocalDate.of(2024, 1, 2), 3),
                Arguments.of(LocalDate.of(2024, 3, 30), 3), Arguments.of(LocalDate.of(2024, 3, 31), 3),

                Arguments.of(LocalDate.of(2024, 4, 1), 4), Arguments.of(LocalDate.of(2024, 4, 2), 4),
                Arguments.of(LocalDate.of(2024, 6, 29), 4), Arguments.of(LocalDate.of(2024, 6, 30), 4));
    }
}
