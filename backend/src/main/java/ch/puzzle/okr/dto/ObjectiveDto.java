package ch.puzzle.okr.dto;

public record ObjectiveDto(Long id, String title, Long teamId, String teamName, Long quarterId, String quarterLabel,
        String description, Long progress) {
}
