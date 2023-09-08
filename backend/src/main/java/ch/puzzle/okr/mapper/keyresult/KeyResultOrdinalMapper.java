package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.checkIn.CheckInDto;
import ch.puzzle.okr.dto.checkIn.CheckInOrdinalDto;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.mapper.checkIn.CheckInMapper;
import ch.puzzle.okr.models.checkIn.CheckIn;
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
    private final CheckInMapper checkInMapper;

    public KeyResultOrdinalMapper(UserPersistenceService userPersistenceService,
            ObjectiveBusinessService objectiveBusinessService, CheckInBusinessService checkInBusinessService,
            CheckInMapper checkInMapper) {
        this.userPersistenceService = userPersistenceService;
        this.objectiveBusinessService = objectiveBusinessService;
        this.checkInMapper = checkInMapper;
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

    public KeyResult toKeyResultOrdinal(KeyResultAbstractDto keyResultAbstractDto) {
        KeyResultOrdinalDto keyResultDto = abstractToOrdinalDto(keyResultAbstractDto);
        return KeyResultOrdinal.Builder.builder().withCommitZone(keyResultDto.commitZone())
                .withTargetZone(keyResultDto.targetZone()).withStretchZone(keyResultDto.stretchZone())
                .withId(keyResultDto.id())
                .withObjective(objectiveBusinessService.getObjectiveById(keyResultDto.objective().id()))
                .withTitle(keyResultDto.title()).withDescription(keyResultDto.description())
                .withOwner(userPersistenceService.findById(keyResultDto.owner().id()))
                .withCreatedOn(keyResultDto.createdOn()).withModifiedOn(keyResultDto.modifiedOn())
                .withKeyResultType(keyResultDto.keyResultType()).build();
    }

    public KeyResultOrdinalDto abstractToOrdinalDto(KeyResultAbstractDto keyResultAbstractDto) {
        return new KeyResultOrdinalDto(keyResultAbstractDto.getId(), keyResultAbstractDto.getKeyResultType(),
                keyResultAbstractDto.getTitle(), keyResultAbstractDto.getDescription(),
                keyResultAbstractDto.getCommitZone(), keyResultAbstractDto.getTargetZone(),
                keyResultAbstractDto.getStretchZone(), keyResultAbstractDto.getOwner(),
                keyResultAbstractDto.getObjective(),
                (KeyResultLastCheckInOrdinalDto) keyResultAbstractDto.getLastCheckIn(),
                keyResultAbstractDto.getCreatedOn(), keyResultAbstractDto.getModifiedOn());
    }

    public KeyResultLastCheckInOrdinalDto getLastCheckInDto(Long keyResultId) {
        CheckIn lastCheckIn = checkInBusinessService.getLastCheckInByKeyResultId(keyResultId);
        KeyResultLastCheckInOrdinalDto lastCheckInDto;
        if (lastCheckIn == null) {
            lastCheckInDto = null;
        } else {
            CheckInDto checkInDto = checkInMapper.toDto(lastCheckIn);
            // TODO: Replace value, confidence and comment with values from checkInDto
            lastCheckInDto = new KeyResultLastCheckInOrdinalDto(checkInDto.getId(),
                    ((CheckInOrdinalDto) checkInDto).getValue().toString(), checkInDto.getConfidence(),
                    lastCheckIn.getCreatedOn(), "Comment");
        }
        return lastCheckInDto;
    }
}
