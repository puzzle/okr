package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

@Component
public abstract class KeyResultMapper {

    // TODO: Remove UserService when Login works and use logged in user for createdBy in toKeyResult method
    public final UserPersistenceService userPersistenceService;

    public final ObjectivePersistenceService objectivePersistenceService;

    public KeyResultMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService) {
        this.userPersistenceService = userPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
    }

    public abstract KeyResultDto toDto(KeyResult keyResult);

    public abstract KeyResult toKeyResult(KeyResultDto keyResultDto);
}
