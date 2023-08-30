package ch.puzzle.okr.dto.overview;

import java.util.List;

public record OverviewObjectiveDto(Long id, String title, String state, OverviewQuarterDto quarter,
        List<OverviewKeyResultDto> keyResults) {
}
