package ch.puzzle.okr.dto.keyresult;

import java.time.LocalDateTime;

public record KeyResultLastCheckInMetricDto(Long id, Double value, Integer confidence, LocalDateTime createdOn,
        String comment) implements KeyResultLastCheckIn {
}
