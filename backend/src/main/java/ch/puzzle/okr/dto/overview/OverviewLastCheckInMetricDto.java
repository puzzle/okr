package ch.puzzle.okr.dto.overview;

import java.time.LocalDateTime;

public record OverviewLastCheckInMetricDto(Long id, Double value, Integer confidence, LocalDateTime createdOn)
        implements OverviewLastCheckInDto {
}
