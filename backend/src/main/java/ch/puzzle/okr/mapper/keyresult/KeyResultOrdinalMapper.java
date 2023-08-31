package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultOrdinalDto;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

@Component
public class KeyResultOrdinalMapper {

    private final UserPersistenceService userPersistenceService;
    private final ObjectivePersistenceService objectivePersistenceService;

    public KeyResultOrdinalMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService) {
        this.userPersistenceService = userPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
    }

    public KeyResultDto toKeyResultOrdinalDto(KeyResult keyResult) {
        return new KeyResultOrdinalDto(keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getCreatedBy().getId(),
                keyResult.getCreatedBy().getFirstname(), keyResult.getCreatedBy().getLastname(),
                keyResult.getCreatedOn(), keyResult.getModifiedOn(), ((KeyResultOrdinal) keyResult).getCommitZone(),
                ((KeyResultOrdinal) keyResult).getTargetZone(), ((KeyResultOrdinal) keyResult).getStretchZone());
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
