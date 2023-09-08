package ch.puzzle.okr.dto.overview;

public record OverviewKeyResultMetricDto(Long id, String title, String unit, Double baseLine, Double stretchGoal,
        OverviewLastCheckInMetricDto lastCheckIn) implements OverviewKeyResultDto {
}
