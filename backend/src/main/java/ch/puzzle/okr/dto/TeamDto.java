package ch.puzzle.okr.dto;

public record TeamDto(Long id, String name, Integer activeObjectives) {
    public TeamDto(Long id, String name) {
        this(id, name, null);
    }
}
