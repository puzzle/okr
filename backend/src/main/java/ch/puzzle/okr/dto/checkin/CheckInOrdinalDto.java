package ch.puzzle.okr.dto.checkin;

import java.time.LocalDateTime;

import ch.puzzle.okr.models.checkin.Zone;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CheckInOrdinalDto.class)
public record CheckInOrdinalDto(
        Long id, int version, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
        LocalDateTime createdOn, LocalDateTime modifiedOn, Zone value, boolean writeable
) implements CheckInDto {
}
