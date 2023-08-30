package ch.puzzle.okr.mapper.keyResult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.keyResult.MetricDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.models.keyResult.KeyResult;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;

public class MetricMapper extends KeyResultMapper {

    public MetricMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService) {
        super(userPersistenceService, objectivePersistenceService);
    }

    @Override
    public KeyResultDto toDto(KeyResult keyResult) {
        return new MetricDto(keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getCreatedBy().getId(), keyResult.getModifiedOn(),
                keyResult.getBaseline(), keyResult.getStretchGoal, keyResult.getUnit());
    }
}
