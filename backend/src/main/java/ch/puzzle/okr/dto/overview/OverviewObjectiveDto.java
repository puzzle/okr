package ch.puzzle.okr.dto.overview;

import java.util.List;

import ch.puzzle.okr.models.State;

public record OverviewObjectiveDto(
        Long id, String title, State state, OverviewQuarterDto quarter, List<OverviewKeyResultDto> keyResults
) {
}
