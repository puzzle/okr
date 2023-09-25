package ch.puzzle.okr.dto.alignment;

import java.util.List;

public record AlignmentObjectiveDto(Long id, String title, List<AlignmentKeyResultDto> keyResults) {
}
