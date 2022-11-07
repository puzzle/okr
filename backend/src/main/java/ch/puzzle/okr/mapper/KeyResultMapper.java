package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.models.KeyResult;
import org.springframework.stereotype.Service;

@Service
public class KeyResultMapper {
    public KeyResultDto toDto(KeyResult keyResult) {
        return new KeyResultDto(
                keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getQuarter().getNumber(), keyResult.getQuarter().getYear(),
                keyResult.getExpectedEvolution(), keyResult.getUnit(), keyResult.getBasisValue(), keyResult.getTargetValue());
    }

    public KeyResult toKeyResult(KeyResultDto keyResultDto){
        return KeyResult.Builder.builder()
                .withId(keyResultDto.getId())
                .withTitle(keyResultDto.getTitle())
                .withDescription(keyResultDto.getDescription())
                .withTargetValue(keyResultDto.getTargetValue())
                .withBasisValue(keyResultDto.getBasicValue())
                .withExpectedEvolution(keyResultDto.getExpectedEvolution())
                .withUnit(keyResultDto.getUnit())
                .build();
    }
}
