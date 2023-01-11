package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.UserRepository;
import ch.puzzle.okr.service.MeasureService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MeasureMapper {
    private final MeasureService measureService;
    // TODO: Remove UserRepository when Login works and use logged in user for createdBy in toKeyResult method
    private final UserRepository userRepository;

    public MeasureMapper(MeasureService measureService, UserRepository userRepository) {
        this.measureService = measureService;
        this.userRepository = userRepository;
    }

    public MeasureDto toDto(Measure measure) {
        if (measure == null) {
            return null;
        } else {
            return new MeasureDto(measure.getId(), measure.getKeyResult().getId(), measure.getValue(),
                    measure.getChangeInfo(), measure.getInitiatives(), measure.getCreatedBy().getId(),
                    measure.getCreatedOn(), measure.getMeasureDate());
        }
    }

    public Measure toMeasure(MeasureDto measureDto) {
        return Measure.Builder.builder().withId(measureDto.getId())
                .withKeyResult(this.measureService.mapKeyResult(measureDto)).withValue(measureDto.getValue())
                .withChangeInfo(measureDto.getChangeInfo()).withInitiatives(measureDto.getInitiatives())
                .withCreatedBy(this.userRepository.findById(1L).get()).withCreatedOn(LocalDateTime.now())
                .withMeasureDate(measureDto.getMeasureDate()).build();
    }
}
