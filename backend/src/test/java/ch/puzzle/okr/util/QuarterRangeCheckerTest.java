package ch.puzzle.okr.util;

import ch.puzzle.okr.models.Quarter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuarterRangeCheckerTest {

    @DisplayName("nowIsInQuarter() should return false if Quarter is null")
    @Test
    void nowIsInQuarterShouldReturnFalseIfQuarterIsNull() {
        // arrange
        Quarter quarter = null;
        LocalDate now = LocalDate.now();

        // act + assert
        assertFalse(QuarterRangeChecker.nowIsInQuarter(now, quarter));
    }

    @DisplayName("nowIsInQuarter() should return false if now is null")
    @Test
    void nowIsInQuarterShouldReturnFalseIfNowIsNull() {
        // arrange
        Quarter quarter = Quarter.Builder.builder().build();
        LocalDate now = null;

        // act + assert
        assertFalse(QuarterRangeChecker.nowIsInQuarter(now, quarter));
    }

    @DisplayName("nowIsInQuarter() should return true if now matches Quarter StartDate")
    @Test
    void nowIsInQuarterShouldReturnTrueIfNowMatchesQuarterStartDate() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        Quarter quarter = quarter(startDate, null);

        LocalDate nowMatchesQuarterStartDate = LocalDate.of(2024, 7, 1);

        // act + assert
        assertTrue(QuarterRangeChecker.nowIsInQuarter(nowMatchesQuarterStartDate, quarter));
    }

    @DisplayName("nowIsInQuarter() should return true if now matches Quarter EndDate")
    @Test
    void nowIsInQuarterShouldReturnTrueIfNowMatchesQuarterEndDate() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 30);
        Quarter quarter = quarter(startDate, endDate);

        LocalDate nowMatchesQuarterEndDate = LocalDate.of(2024, 9, 30);

        // act + assert
        assertTrue(QuarterRangeChecker.nowIsInQuarter(nowMatchesQuarterEndDate, quarter));
    }

    @DisplayName("nowIsInQuarter() should return true if now is between Quarter StartDate and EndDate")
    @Test
    void nowIsInQuarterShouldReturnTrueIfNowIsBetweenQuarterStartDateAndEndDate() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 30);
        Quarter quarter = quarter(startDate, endDate);

        LocalDate nowBetweenQuarterStartEndDate = LocalDate.of(2024, 7, 2);

        // act + assert
        assertTrue(QuarterRangeChecker.nowIsInQuarter(nowBetweenQuarterStartEndDate, quarter));
    }

    @DisplayName("nowIsInQuarter() should return false if now is before Quarter StartDate")
    @Test
    void nowIsInQuarterShouldReturnFalseIfNowIsBeforeQuarterStartDate() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 30);
        Quarter quarter = quarter(startDate, endDate);

        LocalDate nowBeforeQuarterStartDate = LocalDate.of(2024, 6, 30);

        // act + assert
        assertFalse(QuarterRangeChecker.nowIsInQuarter(nowBeforeQuarterStartDate, quarter));
    }

    @DisplayName("nowIsInQuarter() should return false if now is after Quarter EndDate")
    @Test
    void nowIsInQuarterShouldReturnFalseIfNowIsAfterQuarterEndDate() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 30);
        Quarter quarter = quarter(startDate, endDate);

        LocalDate nowAfterQuarterEndDate = LocalDate.of(2024, 10, 1);

        // act + assert
        assertFalse(QuarterRangeChecker.nowIsInQuarter(nowAfterQuarterEndDate, quarter));
    }

    private Quarter quarter(LocalDate startDate, LocalDate endDate) {
        return Quarter.Builder.builder() //
                .withStartDate(startDate) //
                .withEndDate(endDate) //
                .build();
    }
}
