package ch.puzzle.okr.dto.keyresult;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@JsonDeserialize(as = KeyResultOrdinalDto.class)
public record KeyResultOrdinalDto(Long id, String keyResultType, String title, String description, String commitZone,
        String targetZone, String stretchZone, KeyResultUserDto owner, KeyResultObjectiveDto objective,
        KeyResultLastCheckInOrdinalDto lastCheckIn, LocalDateTime createdOn, LocalDateTime modifiedOn,
        boolean writeable) implements KeyResultDto {
}