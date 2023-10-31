package ch.puzzle.okr.dto;

public record ActionDto(Long id, String action, int priority, boolean isChecked, Long keyResultId) {

    public ActionDto withKeyResultId(Long keyResultId) {
        return new ActionDto(id(), action(), priority(), isChecked(), keyResultId);
    }
}
