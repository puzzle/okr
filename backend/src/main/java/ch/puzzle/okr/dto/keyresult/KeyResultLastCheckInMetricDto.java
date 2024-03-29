package ch.puzzle.okr.dto.keyresult;

import java.time.LocalDateTime;

public record KeyResultLastCheckInMetricDto(Long id, int version, Double value, Integer confidence,
        LocalDateTime createdOn, String changeInfo, String initiatives) implements KeyResultLastCheckIn {
}
