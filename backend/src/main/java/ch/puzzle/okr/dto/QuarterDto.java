package ch.puzzle.okr.dto;

import java.time.LocalDateTime;

public record QuarterDto(Long id, String label, LocalDateTime startDate, LocalDateTime endDate) {
}
