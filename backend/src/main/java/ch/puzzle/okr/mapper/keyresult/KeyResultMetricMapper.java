package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.dto.keyresult.KeyResultAbstract;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.mapper.MeasureMapper;
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
    private final MeasureMapper measureMapper;

    public KeyResultMetricMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService,
            MeasurePersistenceService measurePersistenceService, MeasureMapper measureMapper) {
        this.userPersistenceService = userPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.measurePersistenceService = measurePersistenceService;
        this.measureMapper = measureMapper;
    }

    public KeyResultDto toKeyResultMetricDto(KeyResultMetric keyResult) {
        Measure lastMeasure = measurePersistenceService.findFirstMeasureByKeyResultId(keyResult.getId());
        MeasureDto measureDto = lastMeasure == null ? null : measureMapper.toDto(lastMeasure);
        return new KeyResultMetricDto(keyResult.getId(), "metric", keyResult.getTitle(), keyResult.getDescription(),
                keyResult.getBaseline(), keyResult.getStretchGoal(), keyResult.getUnit(), keyResult.getOwner(),
                keyResult.getObjective().getId(), keyResult.getObjective().getState().toString(),
                keyResult.getObjective().getQuarter().getId(), keyResult.getObjective().getQuarter().getLabel(),
                keyResult.getObjective().getQuarter().getStartDate(),
                keyResult.getObjective().getQuarter().getEndDate(), measureDto, keyResult.getCreatedBy(),
                keyResult.getCreatedOn(), keyResult.getModifiedOn());
    }

    public KeyResult toKeyResultMetric(KeyResultAbstract keyResultAbstract) {
        KeyResultMetricDto keyResultDto = abstractToMetricDto(keyResultAbstract);
        return KeyResultMetric.Builder.builder().withBaseline(keyResultDto.baseline())
                .withStretchGoal(keyResultDto.stretchGoal()).withUnit(keyResultDto.unit()).withId(keyResultDto.id())
                .withObjective(objectivePersistenceService.findById(keyResultDto.objectiveId()))
                .withTitle(keyResultDto.title()).withDescription(keyResultDto.description())
                .withOwner(userPersistenceService.findById(keyResultDto.owner().getId()))
                .withCreatedBy(userPersistenceService.findById(keyResultDto.createdBy().getId()))
                .withCreatedOn(keyResultDto.createdOn()).withModifiedOn(keyResultDto.modifiedOn()).build();
    }

    public KeyResultMetricDto abstractToMetricDto(KeyResultAbstract keyResultAbstract) {
        return new KeyResultMetricDto(keyResultAbstract.getId(), keyResultAbstract.getKeyResultType(),
                keyResultAbstract.getTitle(), keyResultAbstract.getDescription(), keyResultAbstract.getBaseline(),
                keyResultAbstract.getStretchGoal(), keyResultAbstract.getUnit(), keyResultAbstract.getOwner(),
                keyResultAbstract.getObjectiveId(), keyResultAbstract.getObjectiveState(),
                keyResultAbstract.getObjectiveQuarterId(), keyResultAbstract.getObjectiveQuarterLabel(),
                keyResultAbstract.getObjectiveQuarterStartDate(), keyResultAbstract.getObjectiveQuarterEndDate(),
                keyResultAbstract.getLastCheckIn(), keyResultAbstract.getCreatedBy(), keyResultAbstract.getCreatedOn(),
                keyResultAbstract.getModifiedOn());
    }
}
