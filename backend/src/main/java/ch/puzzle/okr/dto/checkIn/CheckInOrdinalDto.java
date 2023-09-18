package ch.puzzle.okr.dto.checkIn;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@JsonDeserialize(as = CheckInOrdinalDto.class)
public record CheckInOrdinalDto(Long id, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
        LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, String zone) implements CheckInDto {
}
