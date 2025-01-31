package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.dto.ActionDto;
import ch.puzzle.okr.dto.UnitDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import java.util.List;

@JsonDeserialize(as = KeyResultMetricDto.class)
public record KeyResultMetricDto(Long id, int version, String keyResultType, String title, String description,
                                 Double baseline, Double stretchGoal, UnitDto unit, KeyResultUserDto owner, KeyResultObjectiveDto objective,
                                 KeyResultLastCheckInMetricDto lastCheckIn, LocalDateTime createdOn, LocalDateTime modifiedOn,
                                 boolean isWriteable, List<ActionDto> actionList) implements KeyResultDto {
    @Override
    public List<ActionDto> getActionList() {
        return actionList;
    }
}
