package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.checkIn.CheckInDto;
import ch.puzzle.okr.dto.checkIn.CheckInMetricDto;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.mapper.checkIn.CheckInMapper;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInMetric;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

@Component
public class KeyResultMetricMapper {

    private final UserPersistenceService userPersistenceService;
    private final ObjectiveBusinessService objectiveBusinessService;

    private final CheckInBusinessService checkInBusinessService;
    private final CheckInMapper checkInMapper;

    public KeyResultMetricMapper(UserPersistenceService userPersistenceService,
            ObjectiveBusinessService objectiveBusinessService, CheckInBusinessService checkInBusinessService,
            CheckInMapper checkInMapper) {
        this.userPersistenceService = userPersistenceService;
        this.objectiveBusinessService = objectiveBusinessService;
        this.checkInBusinessService = checkInBusinessService;
        this.checkInMapper = checkInMapper;
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

        return new KeyResultMetricDto(keyResult.getId(), keyResult.getKeyResultType(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getBaseline(), keyResult.getStretchGoal(), keyResult.getUnit(),
                ownerDto, objectiveDto, lastCheckInDto, keyResult.getCreatedOn(), keyResult.getModifiedOn());
    }

    public KeyResult toKeyResultMetric(KeyResultAbstractDto keyResultAbstractDto) {
        KeyResultMetricDto keyResultDto = abstractToMetricDto(keyResultAbstractDto);
        return KeyResultMetric.Builder.builder().withBaseline(keyResultDto.baseline())
                .withStretchGoal(keyResultDto.stretchGoal()).withUnit(keyResultDto.unit()).withId(keyResultDto.id())
                .withObjective(objectiveBusinessService.getObjectiveById(keyResultDto.objective().id()))
                .withTitle(keyResultDto.title()).withDescription(keyResultDto.description())
                .withOwner(userPersistenceService.findById(keyResultDto.owner().id()))
                .withCreatedOn(keyResultDto.createdOn()).withModifiedOn(keyResultDto.modifiedOn())
                .withKeyResultType(keyResultDto.keyResultType()).build();
    }

    public KeyResultMetricDto abstractToMetricDto(KeyResultAbstractDto keyResultAbstractDto) {
        return new KeyResultMetricDto(keyResultAbstractDto.getId(), keyResultAbstractDto.getKeyResultType(),
                keyResultAbstractDto.getTitle(), keyResultAbstractDto.getDescription(),
                keyResultAbstractDto.getBaseline(), keyResultAbstractDto.getStretchGoal(),
                keyResultAbstractDto.getUnit(), keyResultAbstractDto.getOwner(), keyResultAbstractDto.getObjective(),
                (KeyResultLastCheckInMetricDto) keyResultAbstractDto.getLastCheckIn(),
                keyResultAbstractDto.getCreatedOn(), keyResultAbstractDto.getModifiedOn());
    }

    public KeyResultLastCheckInMetricDto getLastCheckInDto(Long keyResultId) {
        CheckIn lastCheckIn = checkInBusinessService.getLastCheckInByKeyResultId(keyResultId);
        KeyResultLastCheckInMetricDto lastCheckInDto;
        if (lastCheckIn == null) {
            lastCheckInDto = null;
        } else {
            lastCheckInDto = new KeyResultLastCheckInMetricDto(lastCheckIn.getId(),
                    ((CheckInMetric) lastCheckIn).getValue(), lastCheckIn.getConfidence(), lastCheckIn.getCreatedOn(),
                    lastCheckIn.getChangeInfo(), lastCheckIn.getInitiatives());
        }
        return lastCheckInDto;
    }
}
