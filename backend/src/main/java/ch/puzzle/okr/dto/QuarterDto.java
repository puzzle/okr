package ch.puzzle.okr.dto;

import java.time.LocalDate;

public record QuarterDto(Long id, String label, LocalDate startDate, LocalDate endDate, boolean isBacklogQuarter) {
}
