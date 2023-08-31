package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;

import java.time.LocalDateTime;

public record KeyResultMetricDto(Long id, String title, String description, Double baseline, Double stretchGoal,
        String unit, Long ownerId, String ownerFirstname, String ownerLastname, Long objectiveId, String objectiveState,
        Long objectiveQuarterId, String objectiveQuarterLabel, LocalDateTime objectiveQuarterStartDate,
        LocalDateTime objectiveQuarterEndDate, Long lastCheckInId, Double lastCheckInValue,
        Integer lastCheckInConfidence, LocalDateTime lastCheckInCreatedOn, String lastCheckInComment, Long createdById,
        String createdByFirstname, String createdByLastname, LocalDateTime createdOn, LocalDateTime modifiedOn)
        implements KeyResultDto {
}
