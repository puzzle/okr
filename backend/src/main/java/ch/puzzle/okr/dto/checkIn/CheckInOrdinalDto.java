package ch.puzzle.okr.dto.checkIn;

import java.time.LocalDateTime;

public record CheckInOrdinalDto(Long id, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
        LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, String zone) implements CheckInDto {
}
