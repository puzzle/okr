package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.MeasureDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record KeyResultOrdinalDto(Long id, String keyResultType, String title, String description, String commitZone,
        String targetZone, String stretchZone, Long ownerId, String ownerFirstname, String ownerLastname,
        Long objectiveId, String objectiveState, Long objectiveQuarterId, String objectiveQuarterLabel,
        LocalDate objectiveQuarterStartDate, LocalDate objectiveQuarterEndDate, MeasureDto measureDto, Long createdById,
        String createdByFirstname, String createdByLastname, LocalDateTime createdOn, LocalDateTime modifiedOn)
        implements KeyResultDto {
}