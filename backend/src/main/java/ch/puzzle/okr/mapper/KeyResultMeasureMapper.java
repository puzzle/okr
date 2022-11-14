package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.KeyResultMeasureDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.service.KeyResultService;

public class KeyResultMeasureMapper {
    private final KeyResultService keyResultService;

    public KeyResultMeasureMapper(KeyResultService keyResultService) {
        this.keyResultService = keyResultService;
    }

    public KeyResultMeasureDto toDto(KeyResult keyResult) {
        return new KeyResultMeasureDto(
                keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getQuarter().getId(), keyResult.getQuarter().getNumber(), keyResult.getQuarter().getYear(),
                keyResult.getExpectedEvolution(), keyResult.getUnit(), keyResult.getBasisValue(), keyResult.getTargetValue(), keyResultService.getLastMeasure());
    }
}
