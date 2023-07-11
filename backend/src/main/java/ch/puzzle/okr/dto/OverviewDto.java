package ch.puzzle.okr.dto;

import java.util.List;

public record OverviewDto(TeamDto team, List<ObjectiveDto> objectives) {
}
