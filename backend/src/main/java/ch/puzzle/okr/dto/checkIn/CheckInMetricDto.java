package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.User;

import java.time.LocalDateTime;

public record CheckInMetricDto(Long id, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
        User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, Double valueMetric)
        implements CheckInDto {
}
