package ch.puzzle.okr.models.quarter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.okr.models.Quarter;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class QuarterTest {

    private static final Quarter normalQuarter = Quarter.Builder
            .builder()
            .withId(1L)
            .withLabel("GJ 24/25-Q1")
            .withStartDate(LocalDate.of(2024, 1, 1))
            .withEndDate(LocalDate.of(2024, 4, 1))
            .build();

    private static final Quarter backlogQuarter = Quarter.Builder
            .builder()
            .withId(2L)
            .withLabel("Backlog")
            .withStartDate(null)
            .withEndDate(null)
            .build();

    static Stream<Arguments> quarterProvider() {
        return Stream.of(Arguments.of(normalQuarter, false), Arguments.of(backlogQuarter, true));
    }

    @ParameterizedTest(name = "isBacklogQuarter() should return {1} for quarter: {0}")
    @MethodSource("quarterProvider")
    void isBacklogQuarterShouldReturnExpectedValue(Quarter quarter, boolean expected) {
        assertEquals(expected, quarter.isBacklogQuarter());
    }
}
