package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.MeasurePersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

@Component
public class KeyResultMetricMapper {

    private final UserPersistenceService userPersistenceService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final MeasurePersistenceService measurePersistenceService;

    public KeyResultMetricMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService,
            MeasurePersistenceService measurePersistenceService) {
        this.userPersistenceService = userPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.measurePersistenceService = measurePersistenceService;
    }

    public KeyResultDto toKeyResultMetricDto(KeyResultMetric keyResult) {
        Measure lastMeasure = measurePersistenceService.findFirstMeasureByKeyResultId(keyResult.getId());
        return new KeyResultMetricDto(keyResult.getId(), keyResult.getTitle(), keyResult.getDescription(),
                keyResult.getBaseline(), keyResult.getStretchGoal(), keyResult.getUnit(), keyResult.getOwner().getId(),
                keyResult.getOwner().getFirstname(), keyResult.getOwner().getLastname(),
                keyResult.getObjective().getId(), keyResult.getObjective().getState().toString(),
                keyResult.getObjective().getQuarter().getId(), keyResult.getObjective().getQuarter().getLabel(),
                keyResult.getObjective().getQuarter().getStartDate(),
                keyResult.getObjective().getQuarter().getEndDate(), lastMeasure.getId(), lastMeasure.getValue(),
                lastMeasure.getConfidence(), lastMeasure.getCreatedOn(), lastMeasure.getComment(),
                keyResult.getCreatedBy().getId(), keyResult.getCreatedBy().getFirstname(),
                keyResult.getCreatedBy().getLastname(), keyResult.getCreatedOn(), keyResult.getModifiedOn());
    }

    public KeyResult toKeyResultMetric(KeyResultDto keyResultDto) {
        return KeyResultMetric.Builder.builder().withBaseline(((KeyResultMetricDto) keyResultDto).baseline())
                .withStretchGoal(((KeyResultMetricDto) keyResultDto).stretchGoal())
                .withUnit(((KeyResultMetricDto) keyResultDto).unit()).withId(((KeyResultMetricDto) keyResultDto).id())
                .withObjective(objectivePersistenceService.findById(((KeyResultMetricDto) keyResultDto).objectiveId()))
                .withTitle(((KeyResultMetricDto) keyResultDto).title())
                .withDescription(((KeyResultMetricDto) keyResultDto).description())
                .withOwner(userPersistenceService.findById(((KeyResultMetricDto) keyResultDto).ownerId()))
                .withCreatedBy(userPersistenceService.findById(((KeyResultMetricDto) keyResultDto).createdById()))
                .withCreatedOn(((KeyResultMetricDto) keyResultDto).createdOn())
                .withModifiedOn(((KeyResultMetricDto) keyResultDto).modifiedOn()).build();
    }
}
