package ch.puzzle.okr.dto.keyresult;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@JsonDeserialize(as = KeyResultMetricDto.class)
public record KeyResultMetricDto(Long id, String keyResultType, String title, String description, Double baseline,
        Double stretchGoal, String unit, KeyResultUserDto owner, KeyResultObjectiveDto objective,
        KeyResultLastCheckInMetricDto lastCheckIn, LocalDateTime createdOn, LocalDateTime modifiedOn)
        implements KeyResultDto {
}
