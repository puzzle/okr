package ch.puzzle.okr.dto.overview;

import java.util.List;

public record DashboardDto(Boolean adminAccess, List<OverviewDto> overviews) {
}
