package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;

import java.time.LocalDateTime;

public record KeyResultOrdinalDto(Long id, String keyResultType, String title, String description, String commitZone,
        String targetZone, String stretchZone, KeyResultUserDto owner, KeyResultObjectiveDto objective,
        KeyResultLastCheckInDto lastCheckIn, LocalDateTime createdOn, LocalDateTime modifiedOn)
        implements KeyResultDto {
}