package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.dto.ActionDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.List;

@JsonDeserialize(as = KeyResultOrdinalDto.class)
public record KeyResultOrdinalDto(Long id, int version, String keyResultType, String title, String description,
        String commitZone, String targetZone, String stretchZone, KeyResultUserDto owner,
        KeyResultObjectiveDto objective, KeyResultLastCheckInOrdinalDto lastCheckIn, LocalDateTime createdOn,
        LocalDateTime modifiedOn, boolean writeable, List<ActionDto> actionList) implements KeyResultDto {
    @Override
    public List<ActionDto> getActionList() {
        return this.actionList;
    }
}