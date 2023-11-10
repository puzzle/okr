package ch.puzzle.okr.dto.overview;

import java.util.List;

public record OverviewDto(OverviewTeamDto team, List<OverviewObjectiveDto> objectives, Boolean writable) {
}
