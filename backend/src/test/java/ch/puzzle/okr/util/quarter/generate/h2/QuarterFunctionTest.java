package ch.puzzle.okr.util.quarter.generate.h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ch.puzzle.okr.util.quarter.generate.h2.QuarterFunction.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuarterFunctionTest {

    @DisplayName("Should return correct current quarter data")
    @Test
    void shouldReturnCorrectCurrentQuarterData() {
        // arrange
        initQuarterData();

        // act
        String currentQuarter = "(2, " //
                + currentQuarterLabel() + ", " //
                + currentQuarterStartDate() + ", " //
                + currentQuarterEndDate() + ")";

        // assert
        String expectedCurrent = "(2, GJ 24/25-Q2, 2024-10-01, 2024-12-31)";
        assertEquals(expectedCurrent, currentQuarter);
    }

    @DisplayName("Should return correct next quarter data")
    @Test
    void shouldReturnCorrectNextQuarterData() {
        // arrange
        initQuarterData();

        // act
        String nextQuarter = "(3, " //
                + nextQuarterLabel() + ", " //
                + nextQuarterStartDate() + ", " //
                + nextQuarterEndDate() + ")";

        // assert
        String expectedNext = "(3, GJ 24/25-Q3, 2025-01-01, 2025-03-31)";
        assertEquals(expectedNext, nextQuarter);
    }
}
