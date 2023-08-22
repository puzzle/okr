package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MeasureMapper {
    private final KeyResultPersistenceService keyResultPersistenceService;
    // TODO: Remove UserService when Login works and use logged in user for createdBy in toKeyResult method
    private final UserPersistenceService userPersistenceService;

    public MeasureMapper(KeyResultPersistenceService keyResultPersistenceService,
            UserPersistenceService userPersistenceService) {
        this.keyResultPersistenceService = keyResultPersistenceService;
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
                .withCreatedBy(userPersistenceService.getOwnerById(keyResult.getOwner().getId())) // replace
                // current
                // Keyresultowner
                // with
                // current
                // logged in
                // USER!!
                .withCreatedOn(LocalDateTime.now()).withMeasureDate(measureDto.measureDate()).build();
    }

    private KeyResult mapKeyResult(MeasureDto measureDto) {
        return keyResultPersistenceService.getKeyResultById(measureDto.keyResultId());
    }
}
