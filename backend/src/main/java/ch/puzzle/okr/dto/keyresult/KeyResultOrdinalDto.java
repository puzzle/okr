package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record KeyResultOrdinalDto(Long id, String keyResultType, String title, String description, String commitZone,
        String targetZone, String stretchZone, User owner, Long objectiveId, String objectiveState,
        Long objectiveQuarterId, String objectiveQuarterLabel, LocalDate objectiveQuarterStartDate,
        LocalDate objectiveQuarterEndDate, MeasureDto measureDto, User createdBy, LocalDateTime createdOn,
        LocalDateTime modifiedOn) implements KeyResultDto {
}