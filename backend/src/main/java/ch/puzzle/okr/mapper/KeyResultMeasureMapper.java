package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultMeasureDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.ProgressService;
import org.springframework.stereotype.Component;

@Component
public class KeyResultMeasureMapper {
    private final MeasureMapper measureMapper;
    private final ProgressService progressService;

    public KeyResultMeasureMapper(MeasureMapper measureMapper, ProgressService progressService) {
        this.measureMapper = measureMapper;
        this.progressService = progressService;
    }

    public KeyResultMeasureDto toDto(KeyResult keyResult, Measure measure) {
        return new KeyResultMeasureDto(keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getExpectedEvolution(), keyResult.getUnit(),
                keyResult.getBasisValue(), keyResult.getTargetValue(), measureMapper.toDto(measure),
                this.progressService.calculateKeyResultProgress(keyResult));
    }
}
