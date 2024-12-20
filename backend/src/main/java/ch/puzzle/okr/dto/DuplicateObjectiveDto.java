package ch.puzzle.okr.dto;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import java.util.List;

public record DuplicateObjectiveDto(ObjectiveDto objective, List<KeyResultDto> keyResults) {
}
