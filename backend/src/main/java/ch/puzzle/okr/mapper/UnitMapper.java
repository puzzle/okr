package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.UnitDto;
import ch.puzzle.okr.models.Unit;
import org.springframework.stereotype.Component;

@Component
public class UnitMapper {

    private final UserMapper userMapper;

    public UnitMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UnitDto toDto(Unit unit) {
        return new UnitDto(unit.getId(), unit.getUnitName(), userMapper.toDto(unit.getCreatedBy()), unit.isDefault());
    }

    public Unit toUnit(UnitDto objectiveDto) {
        return Unit.Builder
                .builder()
                .id(objectiveDto.id())
                .unitName(objectiveDto.unitName())
                .isDefault(objectiveDto.isDefault())
                .build();
    }
}
