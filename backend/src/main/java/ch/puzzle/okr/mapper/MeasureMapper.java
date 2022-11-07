package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.MeasureRepository;
import org.springframework.stereotype.Component;
import ch.puzzle.okr.service.MeasureService;

@Component
public class MeasureMapper {

    MeasureRepository measureRepository;
    public MeasureDto toDto(Measure measure) {
        return new MeasureDto(measure.getId(), measure.getKeyResult().getId(), measure.getValue(), measure.getChangeInfo(),
                measure.getInitiatives(), measure.getCreatedBy().getId(), measure.getCreatedOn());
    }

    public Measure toMeasure(MeasureDto measureDto) {
        MeasureService measureService = new MeasureService(measureRepository);
        return Measure.Builder.builder()
                .withId(measureDto.getId())
                .withKeyResult(measureService.mapKeyResult(measureDto))
                .withValue(measureDto.getValue())
                .withChangeInfo(measureDto.getChangeInfo())
                .withInitiatives(measureDto.getInitiatives())
                .withCreatedBy(measureService.mapUser(measureDto))
                .withCreatedOn(measureDto.getCreatedOn())
                .build();
    }
}
