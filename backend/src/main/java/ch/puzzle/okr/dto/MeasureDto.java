package ch.puzzle.okr.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public record MeasureDto(Long id, Long keyResultId, Double value, String changeInfo, String initiatives,
        Long createdById, LocalDateTime createdOn, Instant measureDate) {
}
