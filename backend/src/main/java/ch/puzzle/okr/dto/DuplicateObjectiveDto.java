package ch.puzzle.okr.dto;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;


import java.util.List;

public class DuplicateObjectiveDto {
    ObjectiveDto objective;

    List<KeyResultDto> keyResults;

    public DuplicateObjectiveDto(ObjectiveDto objective, List<KeyResultDto> keyResults) {
        this.objective = objective;
        this.keyResults = keyResults;
    }

    public ObjectiveDto getObjective() {
        return objective;
    }

    public List<KeyResultDto> getKeyResults() {
        return keyResults;
    }
}
