package ch.puzzle.okr.dto.alignment;

import java.util.List;

public record AlignmentLists(List<AlignmentObjectDto> alignmentObjectDtoList,
        List<AlignmentConnectionDto> alignmentConnectionDtoList) {
}
