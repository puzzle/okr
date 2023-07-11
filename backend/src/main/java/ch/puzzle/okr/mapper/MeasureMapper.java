package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.MeasureService;
import ch.puzzle.okr.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MeasureMapper {
    private final MeasureService measureService;
    // TODO: Remove UserService when Login works and use logged in user for createdBy in toKeyResult method
    private final UserService userService;

    public MeasureMapper(MeasureService measureService, UserService userService) {
        this.measureService = measureService;
        this.userService = userService;
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
        return Measure.Builder.builder().withId(measureDto.id()).withKeyResult(measureService.mapKeyResult(measureDto))
                .withValue(measureDto.value()).withChangeInfo(measureDto.changeInfo())
                .withInitiatives(measureDto.initiatives())
                .withCreatedBy(userService.getOwnerById(measureService.mapKeyResult(measureDto).getOwner().getId())) // replace
                // current
                // Keyresultowner
                // with
                // current
                // logged in
                // USER!!
                .withCreatedOn(LocalDateTime.now()).withMeasureDate(measureDto.measureDate()).build();
    }
}
