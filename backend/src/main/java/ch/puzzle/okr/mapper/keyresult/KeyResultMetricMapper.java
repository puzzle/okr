package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.keyResult.MetricDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class KeyResultMetricMapper extends KeyResultMapper {

    public KeyResultMetricMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService) {
        super(userPersistenceService, objectivePersistenceService);
    }

    @Override
    public KeyResultDto toDto(KeyResult keyResult) {
        if (keyResult instanceof KeyResultMetric) {
            return new MetricDto(keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                    keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                    keyResult.getOwner().getLastname(), keyResult.getCreatedBy().getId(),
                    keyResult.getCreatedBy().getFirstname(), keyResult.getCreatedBy().getLastname(),
                    keyResult.getCreatedOn(), keyResult.getModifiedOn(), ((KeyResultMetric) keyResult).getBaseline(),
                    ((KeyResultMetric) keyResult).getStretchGoal(), ((KeyResultMetric) keyResult).getUnit());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A not Metric KeyResult can't be casted to a MetricDto");
        }
    }

    @Override
    public KeyResult toKeyResult(KeyResultDto keyResultDto) {
        if (keyResultDto instanceof MetricDto) {
            return KeyResultMetric.Builder.builder().withBaseline(((MetricDto) keyResultDto).baseline())
                    .withStretchGoal(((MetricDto) keyResultDto).stretchGoal())
                    .withUnit(((MetricDto) keyResultDto).unit()).withId(((MetricDto) keyResultDto).id())
                    .withObjective(objectivePersistenceService.findById(((MetricDto) keyResultDto).objectiveId()))
                    .withTitle(((MetricDto) keyResultDto).title())
                    .withDescription(((MetricDto) keyResultDto).description())
                    .withOwner(userPersistenceService.findById(((MetricDto) keyResultDto).ownerId()))
                    .withCreatedBy(userPersistenceService.findById(((MetricDto) keyResultDto).createdById()))
                    .withCreatedOn(((MetricDto) keyResultDto).createdOn())
                    .withModifiedOn(((MetricDto) keyResultDto).modifiedOn()).build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A not Ordinal KeyResultDto can't be casted to a Ordinal KeyResult");
        }
    }
}
