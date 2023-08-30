package ch.puzzle.okr.dto.keyResult;

import ch.puzzle.okr.dto.KeyResultDto;

import java.time.LocalDateTime;

public record MetricDto(Long id, Long objectiveId, String title, String description, Long ownerId,
        String ownerFirstname, String ownerLastname, Long createdById, String createdByFirstname,
        String createdByLastname, LocalDateTime createdOn, LocalDateTime modifiedOn, Double baseline,
        Double stretchGoal, String unit) implements KeyResultDto {
}
