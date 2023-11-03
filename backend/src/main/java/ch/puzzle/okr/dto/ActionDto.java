package ch.puzzle.okr.dto;

public record ActionDto(Long id, int version, String action, int priority, boolean isChecked, Long keyResultId) {

    public ActionDto withKeyResultId(Long keyResultId) {
        return new ActionDto(id(), version(), action(), priority(), isChecked(), keyResultId);
    }
}
