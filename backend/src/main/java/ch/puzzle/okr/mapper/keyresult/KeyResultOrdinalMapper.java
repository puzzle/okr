package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.keyResult.OrdinalDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class KeyResultOrdinalMapper extends KeyResultMapper {

    public KeyResultOrdinalMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService) {
        super(userPersistenceService, objectivePersistenceService);
    }

    @Override
    public KeyResultDto toDto(KeyResult keyResult) {
        if (keyResult instanceof KeyResultOrdinal) {
            return new OrdinalDto(keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                    keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                    keyResult.getOwner().getLastname(), keyResult.getCreatedBy().getId(),
                    keyResult.getCreatedBy().getFirstname(), keyResult.getCreatedBy().getLastname(),
                    keyResult.getCreatedOn(), keyResult.getModifiedOn(), ((KeyResultOrdinal) keyResult).getCommitZone(),
                    ((KeyResultOrdinal) keyResult).getTargetZone(), ((KeyResultOrdinal) keyResult).getStretchZone());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A not Ordinal KeyResult can't be casted to a OrdinalDto");
        }
    }

    @Override
    public KeyResult toKeyResult(KeyResultDto keyResultDto) {
        if (keyResultDto instanceof OrdinalDto) {
            return KeyResultOrdinal.Builder.builder().withCommitZone(((OrdinalDto) keyResultDto).commitZone())
                    .withTargetZone(((OrdinalDto) keyResultDto).targetZone())
                    .withStretchZone(((OrdinalDto) keyResultDto).stretchZone()).withId(((OrdinalDto) keyResultDto).id())
                    .withObjective(objectivePersistenceService.findById(((OrdinalDto) keyResultDto).objectiveId()))
                    .withTitle(((OrdinalDto) keyResultDto).title())
                    .withDescription(((OrdinalDto) keyResultDto).description())
                    .withOwner(userPersistenceService.findById(((OrdinalDto) keyResultDto).ownerId()))
                    .withCreatedBy(userPersistenceService.findById(((OrdinalDto) keyResultDto).createdById()))
                    .withCreatedOn(((OrdinalDto) keyResultDto).createdOn())
                    .withModifiedOn(((OrdinalDto) keyResultDto).modifiedOn()).build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A not Ordinal KeyResultDto can't be casted to a Ordinal KeyResult");
        }
    }
}
