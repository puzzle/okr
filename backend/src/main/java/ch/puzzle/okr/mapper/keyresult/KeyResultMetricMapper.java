package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

@Component
public class KeyResultMetricMapper {

    private final UserPersistenceService userPersistenceService;
    private final ObjectivePersistenceService objectivePersistenceService;

    public KeyResultMetricMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService) {
        this.userPersistenceService = userPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
    }

    public KeyResultDto toKeyResultMetricDto(KeyResult keyResult) {
        return new KeyResultMetricDto(keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getCreatedBy().getId(),
                keyResult.getCreatedBy().getFirstname(), keyResult.getCreatedBy().getLastname(),
                keyResult.getCreatedOn(), keyResult.getModifiedOn(), ((KeyResultMetric) keyResult).getBaseline(),
                ((KeyResultMetric) keyResult).getStretchGoal(), ((KeyResultMetric) keyResult).getUnit());
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
