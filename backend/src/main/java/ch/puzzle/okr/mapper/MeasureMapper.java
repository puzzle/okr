package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.MeasureService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MeasureMapper {
    private final MeasureService measureService;

    public MeasureMapper(MeasureService measureService) {
        this.measureService = measureService;
    }

    public MeasureDto toDto(Measure measure) {
        if (measure == null) {
            return null;
        } else {
            return new MeasureDto(measure.getId(), measure.getKeyResult().getId(), measure.getValue(), measure.getChangeInfo(),
                    measure.getInitiatives(), measure.getCreatedBy().getId(), measure.getCreatedOn());
        }
    }

    public Measure toMeasure(MeasureDto measureDto) {
        return Measure.Builder.builder()
                .withId(measureDto.getId())
                .withKeyResult(this.measureService.mapKeyResult(measureDto))
                .withValue(measureDto.getValue())
                .withChangeInfo(measureDto.getChangeInfo())
                .withInitiatives(measureDto.getInitiatives())
                .withCreatedBy(this.measureService.mapUser(measureDto))
                .withCreatedOn(LocalDateTime.now())
                .build();
    }
}
