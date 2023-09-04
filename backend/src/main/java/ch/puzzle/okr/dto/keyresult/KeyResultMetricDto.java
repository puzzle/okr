package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.MeasureDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record KeyResultMetricDto(Long id, String keyResultType, String title, String description, Double baseline,
        Double stretchGoal, String unit, Long ownerId, String ownerFirstname, String ownerLastname, Long objectiveId,
        String objectiveState, Long objectiveQuarterId, String objectiveQuarterLabel,
        LocalDate objectiveQuarterStartDate, LocalDate objectiveQuarterEndDate, MeasureDto lastCheckIn,
        Long createdById, String createdByFirstname, String createdByLastname, LocalDateTime createdOn,
        LocalDateTime modifiedOn) implements KeyResultDto {
}
