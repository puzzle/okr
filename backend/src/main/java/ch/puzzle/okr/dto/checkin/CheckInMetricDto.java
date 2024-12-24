package ch.puzzle.okr.dto.checkin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;

@JsonDeserialize(as = CheckInMetricDto.class)
public record CheckInMetricDto(Long id, int version, String changeInfo, String initiatives, Integer confidence,
        Long keyResultId, LocalDateTime createdOn, LocalDateTime modifiedOn, Double value,
        boolean isWriteable) implements CheckInDto {
}
