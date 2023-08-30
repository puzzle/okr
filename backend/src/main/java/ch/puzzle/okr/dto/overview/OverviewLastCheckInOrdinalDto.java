package ch.puzzle.okr.dto.overview;

import java.time.LocalDateTime;

public record OverviewLastCheckInOrdinalDto(Long id, String value, Integer confidence, LocalDateTime createdOn)
        implements OverviewLastCheckInDto {
}
