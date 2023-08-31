package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MeasureMapper {
    private final KeyResultBusinessService keyResultBusinessService;
    // TODO: Remove UserService when Login works and use logged in user for createdBy in toKeyResult method
    private final UserPersistenceService userPersistenceService;

    public MeasureMapper(KeyResultBusinessService keyResultBusinessService,
            UserPersistenceService userPersistenceService) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.userPersistenceService = userPersistenceService;
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
        KeyResult keyResult = mapKeyResult(measureDto);
        return Measure.Builder.builder().withId(measureDto.id()).withKeyResult(keyResult).withValue(measureDto.value())
                .withChangeInfo(measureDto.changeInfo()).withInitiatives(measureDto.initiatives())
                .withCreatedBy(userPersistenceService.findById(keyResult.getOwner().getId())) // replace
                // current
                // Keyresultowner
                // with
                // current
                // logged in
                // USER!!
                .withCreatedOn(LocalDateTime.now()).withMeasureDate(measureDto.measureDate()).build();
    }

    private KeyResult mapKeyResult(MeasureDto measureDto) {
        return keyResultBusinessService.getKeyResultById(measureDto.keyResultId());
    }
}
