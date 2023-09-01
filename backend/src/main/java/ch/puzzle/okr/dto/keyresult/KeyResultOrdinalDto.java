package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record KeyResultOrdinalDto(Long id, String title, String description, String commitZone, String targetZone,
        String stretchZone, Long ownerId, String ownerFirstname, String ownerLastname, Long objectiveId,
        String objectiveState, Long objectiveQuarterId, String objectiveQuarterLabel,
        LocalDate objectiveQuarterStartDate, LocalDate objectiveQuarterEndDate, Long lastCheckInId,
        Double lastCheckInValue, Integer lastCheckInConfidence, LocalDateTime lastCheckInCreatedOn,
        String lastCheckInComment, Long createdById, String createdByFirstname, String createdByLastname,
        LocalDateTime createdOn, LocalDateTime modifiedOn) implements KeyResultDto {
}