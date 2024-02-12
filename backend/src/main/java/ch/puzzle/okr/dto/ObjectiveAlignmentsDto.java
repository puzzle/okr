package ch.puzzle.okr.dto;

import ch.puzzle.okr.dto.keyresult.KeyResultAlignmentsDto;

import java.util.List;

public record ObjectiveAlignmentsDto(Long objectiveId, String objectiveTitle,
        List<KeyResultAlignmentsDto> keyResultAlignmentsDtos) {
}
