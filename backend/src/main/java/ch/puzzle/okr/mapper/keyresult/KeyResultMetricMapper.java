package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.authorization.ActionAuthorizationService;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyResultMetricMapper {

    private final UserBusinessService userBusinessService;
    private final ObjectiveBusinessService objectiveBusinessService;
    private final ActionAuthorizationService actionAuthorizationService;
    private final CheckInBusinessService checkInBusinessService;
    private final ActionMapper actionMapper;

    public KeyResultMetricMapper(UserBusinessService userBusinessService,
            ObjectiveBusinessService objectiveBusinessService, CheckInBusinessService checkInBusinessService,
                                 ActionAuthorizationService actionAuthorizationService, ActionMapper actionMapper) {
        this.userBusinessService = userBusinessService;
        this.objectiveBusinessService = objectiveBusinessService;
        this.checkInBusinessService = checkInBusinessService;
        this.actionAuthorizationService = actionAuthorizationService;
        this.actionMapper = actionMapper;
    }

    public KeyResultDto toKeyResultMetricDto(KeyResultMetric keyResult) {
        KeyResultUserDto ownerDto = new KeyResultUserDto(keyResult.getOwner().getId(),
                keyResult.getOwner().getFirstname(), keyResult.getOwner().getLastname());
        KeyResultQuarterDto quarterDto = new KeyResultQuarterDto(keyResult.getObjective().getQuarter().getId(),
                keyResult.getObjective().getQuarter().getLabel(), keyResult.getObjective().getQuarter().getStartDate(),
                keyResult.getObjective().getQuarter().getEndDate());
        KeyResultObjectiveDto objectiveDto = new KeyResultObjectiveDto(keyResult.getObjective().getId(),
                keyResult.getObjective().getState().toString(), quarterDto);
        KeyResultLastCheckInMetricDto lastCheckInDto = getLastCheckInDto(keyResult.getId());
        List<Action> actionList = actionAuthorizationService.getEntitiesByKeyResultId(keyResult.getId());

        return new KeyResultMetricDto(keyResult.getId(), keyResult.getVersion(), keyResult.getKeyResultType(),
                keyResult.getTitle(), keyResult.getDescription(), keyResult.getBaseline(), keyResult.getStretchGoal(),
                keyResult.getUnit(), ownerDto, objectiveDto, lastCheckInDto, keyResult.getCreatedOn(),
                keyResult.getModifiedOn(), keyResult.isWriteable(), actionList.stream().map(actionMapper::toDto).toList());
    }

    public KeyResult toKeyResultMetric(KeyResultMetricDto keyResultMetricDto) {
        return KeyResultMetric.Builder.builder().withBaseline(keyResultMetricDto.baseline())
                .withStretchGoal(keyResultMetricDto.stretchGoal()).withUnit(keyResultMetricDto.unit())
                .withId(keyResultMetricDto.id()).withVersion(keyResultMetricDto.version())
                .withObjective(objectiveBusinessService.getEntityById(keyResultMetricDto.objective().id()))
                .withTitle(keyResultMetricDto.title()).withDescription(keyResultMetricDto.description())
                .withOwner(userBusinessService.getOwnerById(keyResultMetricDto.owner().id()))
                .withModifiedOn(keyResultMetricDto.modifiedOn()).build();
    }

    public KeyResultLastCheckInMetricDto getLastCheckInDto(Long keyResultId) {
        CheckIn lastCheckIn = checkInBusinessService.getLastCheckInByKeyResultId(keyResultId);
        KeyResultLastCheckInMetricDto lastCheckInDto;
        if (lastCheckIn == null) {
            lastCheckInDto = null;
        } else {
            lastCheckInDto = new KeyResultLastCheckInMetricDto(lastCheckIn.getId(), lastCheckIn.getVersion(),
                    ((CheckInMetric) lastCheckIn).getValue(), lastCheckIn.getConfidence(), lastCheckIn.getCreatedOn(),
                    lastCheckIn.getChangeInfo(), lastCheckIn.getInitiatives());
        }
        return lastCheckInDto;
    }
}
