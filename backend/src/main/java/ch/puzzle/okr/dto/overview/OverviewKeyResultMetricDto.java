package ch.puzzle.okr.dto.overview;

public record OverviewKeyResultMetricDto(Long id, String title, String unit, Double baseline, Double stretchGoal,
        OverviewLastCheckInMetricDto lastCheckIn) implements OverviewKeyResultDto {
}
