package ch.puzzle.okr.dto;

public record KeyResultDto(Long id, Long objectiveId, String title, String description, Long ownerId,
        String ownerFirstname, String ownerLastname) {
}
