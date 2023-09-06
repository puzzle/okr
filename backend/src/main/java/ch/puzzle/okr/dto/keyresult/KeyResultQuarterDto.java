package ch.puzzle.okr.dto.keyresult;

import java.time.LocalDate;

public record KeyResultQuarterDto(Long id, String label, LocalDate startDate, LocalDate endDate) {
}
