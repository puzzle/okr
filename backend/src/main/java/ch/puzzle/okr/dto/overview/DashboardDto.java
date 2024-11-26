package ch.puzzle.okr.dto.overview;

import java.util.List;

public record DashboardDto(Boolean isAdminAccess, List<OverviewDto> overviews) {
}
