package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.QuarterDto;
import ch.puzzle.okr.models.Quarter;

import java.util.List;

public class QuarterMapper {

    public List<QuarterDto> toDtos(List<Quarter> quarterList) {
        return quarterList.stream().map(this::toDto).toList();
    }

    public QuarterDto toDto(Quarter quarter) {
        return new QuarterDto(quarter.getId(),
                quarter.getLabel(),
                quarter.getStartDate(),
                quarter.getEndDate(),
                quarter.isBacklogQuarter());
    }
}
