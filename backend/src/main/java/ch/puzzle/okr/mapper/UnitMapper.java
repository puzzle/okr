package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.dto.UnitDto;
import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.service.business.QuarterBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UnitMapper {

private final UserMapper userMapper;

    public UnitMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UnitDto toDto(Unit unit) {
        return new UnitDto(unit.getId(),
                           unit.getUnitName(),
                           userMapper.toDto(unit.getCreatedBy()));
    }

    public Unit toUnit(UnitDto objectiveDto) {
        return Unit.Builder
                .builder()
                .id(objectiveDto.id())
                .unitName(objectiveDto.unitName())
                .build();
    }
}
