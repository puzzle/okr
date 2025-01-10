package ch.puzzle.okr.util.quarter.generate.h2;

import static ch.puzzle.okr.util.quarter.generate.h2.QuarterFunction.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuarterFunctionTest {

    @DisplayName("QuarterFunction should return correct current quarter data")
    @Test
    void quarterFunctionShouldReturnCorrectCurrentQuarterData() {
        // arrange
        initQuarterData(LocalDate.of(2024, 12, 24));

        // act
        String currentQuarter = "(" //
                                + currentQuarterLabel() + ", " //
                                + currentQuarterStartDate() + ", " //
                                + currentQuarterEndDate() + ")";

        // assert
        String expectedCurrent = "(GJ 24/25-Q2, 2024-10-01, 2024-12-31)";
        assertEquals(expectedCurrent, currentQuarter);
    }

    @DisplayName("QuarterFunction should return correct next quarter data")
    @Test
    void quarterFunctionShouldReturnCorrectNextQuarterData() {
        // arrange
        initQuarterData(LocalDate.of(2024, 12, 24));

        // act
        String nextQuarter = "(" //
                             + nextQuarterLabel() + ", " //
                             + nextQuarterStartDate() + ", " //
                             + nextQuarterEndDate() + ")";

        // assert
        String expectedNext = "(GJ 24/25-Q3, 2025-01-01, 2025-03-31)";
        assertEquals(expectedNext, nextQuarter);
    }
}
