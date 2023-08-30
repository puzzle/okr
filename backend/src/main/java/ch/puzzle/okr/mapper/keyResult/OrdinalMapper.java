package ch.puzzle.okr.mapper.keyResult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.models.keyResult.KeyResult;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;

public class OrdinalMapper extends KeyResultMapper {

    public OrdinalMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService) {
        super(userPersistenceService, objectivePersistenceService);
    }

    @Override
    public KeyResultDto toDto(KeyResult keyResult) {
        return new KeyResultDto(keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getCreatedBy().getId(),
                keyResult.getCreatedBy().getFirstname(), keyResult.getCreatedBy().getLastname(),
                keyResult.getCreatedOn(), keyResult.getModifiedOn(), keyResult.getCommitZone(),
                keyResult.getTargetZone(), keyResult.getStretchZone());
    }
}
