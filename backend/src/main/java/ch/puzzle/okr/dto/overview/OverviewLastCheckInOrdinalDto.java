package ch.puzzle.okr.dto.overview;

import java.time.LocalDateTime;

public record OverviewLastCheckInOrdinalDto(Long id, String zone, Integer confidence,
        LocalDateTime createdOn) implements OverviewLastCheckInDto {
}
