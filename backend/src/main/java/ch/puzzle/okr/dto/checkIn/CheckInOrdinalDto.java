package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.checkIn.Zone;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@JsonDeserialize(as = CheckInOrdinalDto.class)
public record CheckInOrdinalDto(Long id, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
        LocalDateTime createdOn, LocalDateTime modifiedOn, Zone value) implements CheckInDto {
}
