package ch.puzzle.okr.dto;

public record ActionDto(Long id, int version, String action, int priority, boolean isChecked, Long keyResultId,
        boolean writeable) {
}
