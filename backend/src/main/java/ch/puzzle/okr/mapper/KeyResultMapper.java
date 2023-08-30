package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.models.keyResult.KeyResult;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

@Component
public abstract class KeyResultMapper {

    // TODO: Remove UserService when Login works and use logged in user for createdBy in toKeyResult method
    private final UserPersistenceService userPersistenceService;

    private final ObjectivePersistenceService objectivePersistenceService;

    public KeyResultMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService) {
        this.userPersistenceService = userPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
    }

    public abstract KeyResultDto toDto(KeyResult keyResult);

    // public KeyResult toKeyResultMetric(KeyResultMetricDto keyResult) {
    // return
    // KeyResultMetric.Builder.builder().withBaseline(keyResult.baseline()).withStretchGoal(keyResult.stretchGoal())
    // .withUnit(keyResult.unit()).withId(keyResult.id()).withTitle(keyResult.title()).withDescription(keyResult.description())
    // .withObjective(objectivePersistenceService.findById(keyResult.objectiveId())).withOwner(userPersistenceService.findById(keyResult.ownerId()))
    // .withModifiedOn(keyResult.modifiedOn()).withCreatedBy(userPersistenceService.findById(keyResult.createdById())).withCreatedOn(keyResult.createdOn())
    // .build();
    // }
    //
    // public KeyResultMetricDto toKeyResultMetricDto(KeyResultMetric keyResult) {
    // return new KeyResultMetricDto(keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
    // keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
    // keyResult.getOwner().getLastname(), keyResult.getCreatedBy().getId(), keyResult.getCreatedBy().getFirstname(),
    // keyResult.getCreatedBy().getLastname(), keyResult.getCreatedOn(), keyResult.getModifiedOn(),
    // keyResult.getBaseline(), keyResult.getStretchGoal(), keyResult.getUnit());
    // }
}
