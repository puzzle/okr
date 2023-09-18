package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInOrdinal;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

@Component
public class KeyResultOrdinalMapper {

    private final UserPersistenceService userPersistenceService;
    private final ObjectiveBusinessService objectiveBusinessService;
    private final CheckInBusinessService checkInBusinessService;

    public KeyResultOrdinalMapper(UserPersistenceService userPersistenceService,
            ObjectiveBusinessService objectiveBusinessService, CheckInBusinessService checkInBusinessService) {
        this.userPersistenceService = userPersistenceService;
        this.objectiveBusinessService = objectiveBusinessService;
        this.checkInBusinessService = checkInBusinessService;
    }

    public KeyResultDto toKeyResultOrdinalDto(KeyResultOrdinal keyResult) {
        KeyResultUserDto ownerDto = new KeyResultUserDto(keyResult.getOwner().getId(),
                keyResult.getOwner().getFirstname(), keyResult.getOwner().getLastname());
        KeyResultQuarterDto quarterDto = new KeyResultQuarterDto(keyResult.getObjective().getQuarter().getId(),
                keyResult.getObjective().getQuarter().getLabel(), keyResult.getObjective().getQuarter().getStartDate(),
                keyResult.getObjective().getQuarter().getEndDate());
        KeyResultObjectiveDto objectiveDto = new KeyResultObjectiveDto(keyResult.getObjective().getId(),
                keyResult.getObjective().getState().toString(), quarterDto);
        KeyResultLastCheckInOrdinalDto lastCheckInDto = getLastCheckInDto(keyResult.getId());

        return new KeyResultOrdinalDto(keyResult.getId(), keyResult.getKeyResultType(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getCommitZone(), keyResult.getTargetZone(),
                keyResult.getStretchZone(), ownerDto, objectiveDto, lastCheckInDto, keyResult.getCreatedOn(),
                keyResult.getModifiedOn());
    }

    public KeyResult toKeyResultOrdinal(KeyResultOrdinalDto keyResultOrdinalDto) {
        return KeyResultOrdinal.Builder.builder().withCommitZone(keyResultOrdinalDto.commitZone())
                .withTargetZone(keyResultOrdinalDto.targetZone()).withStretchZone(keyResultOrdinalDto.stretchZone())
                .withId(keyResultOrdinalDto.id())
                .withObjective(objectiveBusinessService.getObjectiveById(keyResultOrdinalDto.objective().id()))
                .withTitle(keyResultOrdinalDto.title()).withDescription(keyResultOrdinalDto.description())
                .withOwner(userPersistenceService.findById(keyResultOrdinalDto.owner().id()))
                .withCreatedOn(keyResultOrdinalDto.createdOn()).withModifiedOn(keyResultOrdinalDto.modifiedOn())
                .withKeyResultType(keyResultOrdinalDto.keyResultType()).build();
    }

    public KeyResultLastCheckInOrdinalDto getLastCheckInDto(Long keyResultId) {
        CheckIn lastCheckIn = checkInBusinessService.getLastCheckInByKeyResultId(keyResultId);
        KeyResultLastCheckInOrdinalDto lastCheckInDto;
        if (lastCheckIn == null) {
            lastCheckInDto = null;
        } else {
            lastCheckInDto = new KeyResultLastCheckInOrdinalDto(lastCheckIn.getId(),
                    ((CheckInOrdinal) lastCheckIn).getValue().toString(), lastCheckIn.getConfidence(),
                    lastCheckIn.getCreatedOn(), lastCheckIn.getChangeInfo(), lastCheckIn.getInitiatives());
        }
        return lastCheckInDto;
    }
}
