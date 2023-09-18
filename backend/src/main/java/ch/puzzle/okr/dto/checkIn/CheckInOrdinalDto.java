package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.User;

import java.time.LocalDateTime;

public record CheckInOrdinalDto(Long id, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
        User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, String zone)
        implements CheckInDto {
}
