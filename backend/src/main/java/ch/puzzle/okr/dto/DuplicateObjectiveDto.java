package ch.puzzle.okr.dto;

import java.util.List;

public record DuplicateObjectiveDto(ObjectiveDto objective, List<Long> keyResultIds) {
}
