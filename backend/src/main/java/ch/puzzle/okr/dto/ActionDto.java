package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.keyresult.KeyResult;

public record ActionDto(Long id, String action, int priority, boolean isChecked, KeyResult keyResult) {
}
