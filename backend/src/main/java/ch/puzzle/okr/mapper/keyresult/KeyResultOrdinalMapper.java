package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultOrdinalDto;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.MeasurePersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

@Component
public class KeyResultOrdinalMapper {

    private final UserPersistenceService userPersistenceService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final MeasurePersistenceService measurePersistenceService;

    public KeyResultOrdinalMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService,
            MeasurePersistenceService measurePersistenceService) {
        this.userPersistenceService = userPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.measurePersistenceService = measurePersistenceService;
    }

    public KeyResultDto toKeyResultOrdinalDto(KeyResultOrdinal keyResult) {
        Measure lastMeasure = measurePersistenceService.findFirstMeasureByKeyResultId(keyResult.getId());
        return new KeyResultOrdinalDto(keyResult.getId(), keyResult.getTitle(), keyResult.getDescription(),
                keyResult.getCommitZone(), keyResult.getTargetZone(), keyResult.getStretchZone(),
                keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(), keyResult.getOwner().getLastname(),
                keyResult.getObjective().getId(), keyResult.getObjective().getState().toString(),
                keyResult.getObjective().getQuarter().getId(), keyResult.getObjective().getQuarter().getLabel(),
                keyResult.getObjective().getQuarter().getStartDate(),
                keyResult.getObjective().getQuarter().getEndDate(), lastMeasure.getId(), lastMeasure.getValue(),
                lastMeasure.getConfidence(), lastMeasure.getCreatedOn(), lastMeasure.getComment(),
                keyResult.getCreatedBy().getId(), keyResult.getCreatedBy().getFirstname(),
                keyResult.getCreatedBy().getLastname(), keyResult.getCreatedOn(), keyResult.getModifiedOn());
    }

    public KeyResult toKeyResultOrdinal(KeyResultDto keyResultDto) {
        return KeyResultOrdinal.Builder.builder().withCommitZone(((KeyResultOrdinalDto) keyResultDto).commitZone())
                .withTargetZone(((KeyResultOrdinalDto) keyResultDto).targetZone())
                .withStretchZone(((KeyResultOrdinalDto) keyResultDto).stretchZone())
                .withId(((KeyResultOrdinalDto) keyResultDto).id())
                .withObjective(objectivePersistenceService.findById(((KeyResultOrdinalDto) keyResultDto).objectiveId()))
                .withTitle(((KeyResultOrdinalDto) keyResultDto).title())
                .withDescription(((KeyResultOrdinalDto) keyResultDto).description())
                .withOwner(userPersistenceService.findById(((KeyResultOrdinalDto) keyResultDto).ownerId()))
                .withCreatedBy(userPersistenceService.findById(((KeyResultOrdinalDto) keyResultDto).createdById()))
                .withCreatedOn(((KeyResultOrdinalDto) keyResultDto).createdOn())
                .withModifiedOn(((KeyResultOrdinalDto) keyResultDto).modifiedOn()).build();
    }
}
