package ch.puzzle.okr.util.quarter.check;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.puzzle.okr.models.Quarter;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuarterRangeCheckerTest {

    @DisplayName("Should return false if Quarter is null when nowIsInQuarter() is called")
    @Test
    void shouldReturnFalseIfQuarterIsNullWhenNowIsInQuarterIsCalled() {
        // arrange
        Quarter quarter = null;
        LocalDate now = LocalDate.now();

        // act + assert
        assertFalse(QuarterRangeChecker.nowIsInQuarter(now, quarter));
    }

    @DisplayName("Should return false if now is null when nowIsInQuarter() is called")
    @Test
    void shouldReturnFalseIfNowIsNullWhenNowIsInQuarterIsCalled() {
        // arrange
        Quarter quarter = Quarter.Builder.builder().build();
        LocalDate now = null;

        // act + assert
        assertFalse(QuarterRangeChecker.nowIsInQuarter(now, quarter));
    }

    @DisplayName("Should return true if now matches Quarter StartDate when nowIsInQuarter() is called")
    @Test
    void shouldReturnTrueIfNowMatchesQuarterStartDateWhenNowIsInQuarterIsCalled() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        Quarter quarter = quarter(startDate, null);

        LocalDate nowMatchingQuarterStartDate = LocalDate.of(2024, 7, 1);

        // act + assert
        assertTrue(QuarterRangeChecker.nowIsInQuarter(nowMatchingQuarterStartDate, quarter));
    }

    @DisplayName("Should return true if now matches Quarter EndDate when nowIsInQuarter() is called")
    @Test
    void shouldReturnTrueIfNowMatchesQuarterEndDateWhenNowIsInQuarterIsCalled() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 30);
        Quarter quarter = quarter(startDate, endDate);

        LocalDate nowMatchingQuarterEndDate = LocalDate.of(2024, 9, 30);

        // act + assert
        assertTrue(QuarterRangeChecker.nowIsInQuarter(nowMatchingQuarterEndDate, quarter));
    }

    @DisplayName("Should return true if now is between Quarter StartDate and EndDate when nowIsInQuarter() is called")
    @Test
    void shouldReturnTrueIfNowIsBetweenQuarterStartDateAndEndDateWhenNowIsInQuarterIsCalled() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 30);
        Quarter quarter = quarter(startDate, endDate);

        LocalDate nowBetweenQuarterStartEndDate = LocalDate.of(2024, 7, 2);

        // act + assert
        assertTrue(QuarterRangeChecker.nowIsInQuarter(nowBetweenQuarterStartEndDate, quarter));
    }

    @DisplayName("Should return false if now is before Quarter StartDate when nowIsInQuarter() is called")
    @Test
    void shouldReturnFalseIfNowIsBeforeQuarterStartDateWhenNowIsInQuarterIsCalled() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 30);
        Quarter quarter = quarter(startDate, endDate);

        LocalDate nowBeforeQuarterStartDate = LocalDate.of(2024, 6, 30);

        // act + assert
        assertFalse(QuarterRangeChecker.nowIsInQuarter(nowBeforeQuarterStartDate, quarter));
    }

    @DisplayName("Should return false if now is after Quarter EndDate when nowIsInQuarter() is called")
    @Test
    void shouldReturnFalseIfNowIsAfterQuarterEndDateWhenNowIsInQuarterIsCalled() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 30);
        Quarter quarter = quarter(startDate, endDate);

        LocalDate nowAfterQuarterEndDate = LocalDate.of(2024, 10, 1);

        // act + assert
        assertFalse(QuarterRangeChecker.nowIsInQuarter(nowAfterQuarterEndDate, quarter));
    }

    private Quarter quarter(LocalDate startDate, LocalDate endDate) {
        return Quarter.Builder
                .builder() //
                .withStartDate(startDate) //
                .withEndDate(endDate) //
                .build();
    }
}
