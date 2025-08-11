package ch.puzzle.okr.dto.overview;

public record OverviewKeyResultMetricDto(Long id, String title, String keyResultType, Double baseline,
        Double wordingCommitValue, Double wordingTargetValue, Double stretchGoal,
        OverviewLastCheckInMetricDto lastCheckIn) implements OverviewKeyResultDto {
}
