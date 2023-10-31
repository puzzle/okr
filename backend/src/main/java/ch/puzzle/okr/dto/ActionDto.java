package ch.puzzle.okr.dto;

public record ActionDto(Long id, String action, int priority, boolean isChecked, Long keyResultId) {
}
