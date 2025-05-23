package ch.puzzle.okr.dto.checkin;

import ch.puzzle.okr.models.checkin.Zone;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;

@JsonDeserialize(as = CheckInOrdinalDto.class)
public record CheckInOrdinalDto(Long id, int version, String changeInfo, String initiatives, Integer confidence,
        Long keyResultId, String createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, Zone zone,
        boolean isWriteable) implements CheckInDto {
}
