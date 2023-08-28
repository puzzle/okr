package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.models.keyResult.KeyResult;
import ch.puzzle.okr.models.keyResult.KeyResultMetric;
import ch.puzzle.okr.models.keyResult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KeyResultMapper {

    // TODO: Remove UserService when Login works and use logged in user for createdBy in toKeyResult method
    private final UserPersistenceService userPersistenceService;

    private final ObjectivePersistenceService objectivePersistenceService;

    public KeyResultMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService) {
        this.userPersistenceService = userPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
    }

    public KeyResultDto toDto(KeyResult keyResult) {
        return new KeyResultDto(keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getExpectedEvolution(), keyResult.getUnit(),
                keyResult.getBasisValue(), keyResult.getTargetValue(), 0L);
    }

    public KeyResult toKeyResultMetric(KeyResultDto keyResultDto) {
        return (KeyResultMetric) new KeyResultMetric.MetricBuilder().withId(keyResultDto.id())
                .withTitle(keyResultDto.title()).withOwner(userPersistenceService.findById(keyResultDto.ownerId()))
                .withObjective(objectivePersistenceService.findById(keyResultDto.objectiveId()))
                .withDescription(keyResultDto.description()).withCreatedOn(keyResultDto.createdOn)
                .withCreatedBy(keyResultDto.createdBy).withModifiedOn(keyResultDto.modifiedOn).withBaseLine()
                .withStretchGoal().withUnit().build();
    }

    public KeyResult toKeyResultOrdinal(KeyResultDto keyResultDto) {
        return (KeyResultOrdinal) new KeyResultOrdinal.OrdinalBuilder().withId(keyResultDto.id())
                .withTitle(keyResultDto.title()).withOwner(userPersistenceService.findById(keyResultDto.ownerId()))
                .withObjective(objectivePersistenceService.findById(keyResultDto.objectiveId()))
                .withDescription(keyResultDto.description()).withCreatedOn(keyResultDto.createdOn)
                .withCreatedBy(keyResultDto.createdBy).withModifiedOn(keyResultDto.modifiedOn).withCommitZone()
                .withTargetZone().withStretchZone().build();
    }
}
