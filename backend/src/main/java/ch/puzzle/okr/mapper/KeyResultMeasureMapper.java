package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.KeyResultMeasureDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.KeyResultService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class KeyResultMeasureMapper {
    private final KeyResultService keyResultService;
    private final MeasureMapper measureMapper;

    public KeyResultMeasureMapper(KeyResultService keyResultService, MeasureMapper measureMapper) {
        this.keyResultService = keyResultService;
        this.measureMapper = measureMapper;
    }

    public KeyResultMeasureDto toDto(KeyResult keyResult, Measure measure) {
        return new KeyResultMeasureDto(
                keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getQuarter().getId(), keyResult.getQuarter().getNumber(), keyResult.getQuarter().getYear(),
                keyResult.getExpectedEvolution(), keyResult.getUnit(), keyResult.getBasisValue(), keyResult.getTargetValue(), measureMapper.toDto(measure));
    }
}
