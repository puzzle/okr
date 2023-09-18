package ch.puzzle.okr.dto.checkIn;

import java.time.LocalDateTime;

public record CheckInMetricDto(Long id, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
        LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, Double valueMetric)
        implements CheckInDto {
}
