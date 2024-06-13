package ch.puzzle.okr.dto;

import java.util.List;

public record AlignmentDto(Long teamId, String teamName, List<AlignmentObjectDto> alignmentObjects) {
}
