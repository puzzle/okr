package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyResultOrdinalMapper {

    private final UserBusinessService userBusinessService;
    private final ObjectiveBusinessService objectiveBusinessService;
    private final CheckInBusinessService checkInBusinessService;
    private final ActionMapper actionMapper;

    public KeyResultOrdinalMapper(UserBusinessService userBusinessService,
            ObjectiveBusinessService objectiveBusinessService, CheckInBusinessService checkInBusinessService,
            ActionMapper actionMapper) {
        this.userBusinessService = userBusinessService;
        this.objectiveBusinessService = objectiveBusinessService;
        this.checkInBusinessService = checkInBusinessService;
        this.actionMapper = actionMapper;
    }

    public KeyResultDto toKeyResultOrdinalDto(KeyResultOrdinal keyResult, List<Action> actionList) {
        KeyResultUserDto ownerDto = new KeyResultUserDto(keyResult.getOwner().getId(),
                keyResult.getOwner().getFirstname(), keyResult.getOwner().getLastname());
        KeyResultQuarterDto quarterDto = new KeyResultQuarterDto(keyResult.getObjective().getQuarter().getId(),
                keyResult.getObjective().getQuarter().getLabel(), keyResult.getObjective().getQuarter().getStartDate(),
                keyResult.getObjective().getQuarter().getEndDate());
        KeyResultObjectiveDto objectiveDto = new KeyResultObjectiveDto(keyResult.getObjective().getId(),
                keyResult.getObjective().getState().toString(), quarterDto);
        KeyResultLastCheckInOrdinalDto lastCheckInDto = getLastCheckInDto(keyResult.getId());

        return new KeyResultOrdinalDto(keyResult.getId(), keyResult.getVersion(), keyResult.getKeyResultType(),
                keyResult.getTitle(), keyResult.getDescription(), keyResult.getCommitZone(), keyResult.getTargetZone(),
                keyResult.getStretchZone(), ownerDto, objectiveDto, lastCheckInDto, keyResult.getCreatedOn(),
                keyResult.getModifiedOn(), keyResult.isWriteable(),
                actionList.stream().map(actionMapper::toDto).toList());
    }

    public KeyResult toKeyResultOrdinal(KeyResultOrdinalDto keyResultOrdinalDto) {
        return KeyResultOrdinal.Builder.builder().withCommitZone(keyResultOrdinalDto.commitZone())
                .withTargetZone(keyResultOrdinalDto.targetZone()).withStretchZone(keyResultOrdinalDto.stretchZone())
                .withId(keyResultOrdinalDto.id()).withVersion(keyResultOrdinalDto.version())
                .withObjective(objectiveBusinessService.getEntityById(keyResultOrdinalDto.objective().id()))
                .withTitle(keyResultOrdinalDto.title()).withDescription(keyResultOrdinalDto.description())
                .withOwner(userBusinessService.getUserById(keyResultOrdinalDto.owner().id()))
                .withModifiedOn(keyResultOrdinalDto.modifiedOn()).build();
    }

    public KeyResultLastCheckInOrdinalDto getLastCheckInDto(Long keyResultId) {
        CheckIn lastCheckIn = checkInBusinessService.getLastCheckInByKeyResultId(keyResultId);
        KeyResultLastCheckInOrdinalDto lastCheckInDto;
        if (lastCheckIn == null) {
            lastCheckInDto = null;
        } else {
            lastCheckInDto = new KeyResultLastCheckInOrdinalDto(lastCheckIn.getId(), lastCheckIn.getVersion(),
                    ((CheckInOrdinal) lastCheckIn).getZone(), lastCheckIn.getConfidence(), lastCheckIn.getCreatedOn(),
                    lastCheckIn.getChangeInfo(), lastCheckIn.getInitiatives());
        }
        return lastCheckInDto;
    }
}
