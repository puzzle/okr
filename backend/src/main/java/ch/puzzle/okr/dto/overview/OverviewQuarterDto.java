package ch.puzzle.okr.dto.overview;

import java.time.LocalDate;

public record OverviewQuarterDto(Long id, String label, LocalDate startDate, LocalDate endDate) {
}
