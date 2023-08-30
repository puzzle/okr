package ch.puzzle.okr.dto.keyResult;

import ch.puzzle.okr.dto.KeyResultDto;

import java.time.LocalDateTime;

public record OrdinalDto(Long id, Long objectiveId, String title, String description, Long ownerId,
        String ownerFirstname, String ownerLastname, Long createdById, String createdByFirstname,
        String createdByLastname, LocalDateTime createdOn, LocalDateTime modifiedOn, String commitZone,
        String targetZone, String stretchZone) implements KeyResultDto {
}