package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;

import java.time.LocalDateTime;

public record KeyResultMetricDto(Long id, String keyResultType, String title, String description, Double baseline,
        Double stretchGoal, String unit, KeyResultUserDto owner, KeyResultObjectiveDto objective,
        KeyResultLastCheckInDto lastCheckIn, LocalDateTime createdOn, LocalDateTime modifiedOn)
        implements KeyResultDto {
}
