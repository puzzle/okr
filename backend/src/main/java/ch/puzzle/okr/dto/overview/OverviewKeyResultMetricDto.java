package ch.puzzle.okr.dto.overview;

import ch.puzzle.okr.models.Unit;

public record OverviewKeyResultMetricDto(Long id, String title, Unit unit, Double baseLine, Double stretchGoal,
        OverviewLastCheckInMetricDto lastCheckIn) implements OverviewKeyResultDto {
}
