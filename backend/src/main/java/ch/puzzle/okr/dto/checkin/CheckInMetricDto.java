package ch.puzzle.okr.dto.checkin;

import java.time.LocalDateTime;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CheckInMetricDto.class)
public record CheckInMetricDto(Long id, int version, String changeInfo, String initiatives, Integer confidence,
        Long keyResultId, String createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, Double value,
        boolean isWriteable) implements CheckInDto {
}
