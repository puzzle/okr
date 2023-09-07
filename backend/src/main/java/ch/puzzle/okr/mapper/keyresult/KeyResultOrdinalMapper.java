package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.MeasurePersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Component;

@Component
public class KeyResultOrdinalMapper {

    private final UserPersistenceService userPersistenceService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final MeasurePersistenceService measurePersistenceService;
    private final MeasureMapper measureMapper;

    public KeyResultOrdinalMapper(UserPersistenceService userPersistenceService,
            ObjectivePersistenceService objectivePersistenceService,
            MeasurePersistenceService measurePersistenceService, MeasureMapper measureMapper) {
        this.userPersistenceService = userPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.measurePersistenceService = measurePersistenceService;
        this.measureMapper = measureMapper;
    }

    public KeyResultDto toKeyResultOrdinalDto(KeyResultOrdinal keyResult) {
        KeyResultUserDto ownerDto = new KeyResultUserDto(keyResult.getOwner().getId(),
                keyResult.getOwner().getFirstname(), keyResult.getOwner().getLastname());
        KeyResultUserDto createdByDto = new KeyResultUserDto(keyResult.getCreatedBy().getId(),
                keyResult.getCreatedBy().getFirstname(), keyResult.getCreatedBy().getLastname());
        KeyResultQuarterDto quarterDto = new KeyResultQuarterDto(keyResult.getObjective().getQuarter().getId(),
                keyResult.getObjective().getQuarter().getLabel(), keyResult.getObjective().getQuarter().getStartDate(),
                keyResult.getObjective().getQuarter().getEndDate());
        KeyResultObjectiveDto objectiveDto = new KeyResultObjectiveDto(keyResult.getObjective().getId(),
                keyResult.getObjective().getState().toString(), quarterDto);
        KeyResultLastCheckInDto lastCheckInDto = getLastCheckInDto(keyResult.getId());

        return new KeyResultOrdinalDto(keyResult.getId(), keyResult.getKeyResultType(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getCommitZone(), keyResult.getTargetZone(),
                keyResult.getStretchZone(), ownerDto, objectiveDto, lastCheckInDto, createdByDto,
                keyResult.getCreatedOn(), keyResult.getModifiedOn());
    }

    public KeyResult toKeyResultOrdinal(KeyResultAbstractDto keyResultAbstractDto) {
        KeyResultOrdinalDto keyResultDto = abstractToOrdinalDto(keyResultAbstractDto);
        return KeyResultOrdinal.Builder.builder().withCommitZone(keyResultDto.commitZone())
                .withTargetZone(keyResultDto.targetZone()).withStretchZone(keyResultDto.stretchZone())
                .withId(keyResultDto.id())
                .withObjective(objectivePersistenceService.findById(keyResultDto.objective().id()))
                .withTitle(keyResultDto.title()).withDescription(keyResultDto.description())
                .withOwner(userPersistenceService.findById(keyResultDto.owner().id()))
                .withCreatedBy(userPersistenceService.findById(keyResultDto.createdBy().id()))
                .withCreatedOn(keyResultDto.createdOn()).withModifiedOn(keyResultDto.modifiedOn())
                .withKeyResultType(keyResultDto.keyResultType()).build();
    }

    public KeyResultOrdinalDto abstractToOrdinalDto(KeyResultAbstractDto keyResultAbstractDto) {
        return new KeyResultOrdinalDto(keyResultAbstractDto.getId(), keyResultAbstractDto.getKeyResultType(),
                keyResultAbstractDto.getTitle(), keyResultAbstractDto.getDescription(),
                keyResultAbstractDto.getCommitZone(), keyResultAbstractDto.getTargetZone(),
                keyResultAbstractDto.getStretchZone(), keyResultAbstractDto.getOwner(),
                keyResultAbstractDto.getObjective(), keyResultAbstractDto.getLastCheckIn(),
                keyResultAbstractDto.getCreatedBy(), keyResultAbstractDto.getCreatedOn(),
                keyResultAbstractDto.getModifiedOn());
    }

    public KeyResultLastCheckInDto getLastCheckInDto(Long keyResultId) {
        Measure lastMeasure = measurePersistenceService.findFirstMeasureByKeyResultId(keyResultId);
        KeyResultLastCheckInDto lastCheckInDto;
        if (lastMeasure == null) {
            lastCheckInDto = null;
        } else {
            MeasureDto measureDto = measureMapper.toDto(lastMeasure);
            // TODO: Replace value, confidence and comment with values from measureDto
            lastCheckInDto = new KeyResultLastCheckInDto(measureDto.id(), 1.0, 0, lastMeasure.getCreatedOn(),
                    "Comment");
        }
        return lastCheckInDto;
    }
}
