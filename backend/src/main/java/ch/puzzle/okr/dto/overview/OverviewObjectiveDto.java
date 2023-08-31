package ch.puzzle.okr.dto.overview;

import ch.puzzle.okr.models.State;

import java.util.List;

public record OverviewObjectiveDto(Long id, String title, State state, OverviewQuarterDto quarter,
        List<OverviewKeyResultDto> keyResults) {
}
