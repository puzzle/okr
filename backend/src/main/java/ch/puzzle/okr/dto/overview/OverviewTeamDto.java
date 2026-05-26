package ch.puzzle.okr.dto.overview;

import java.time.LocalDate;

public record OverviewTeamDto(Long id, String name, LocalDate markedAsArchivedAt) {
}
