package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDTO;
import ch.puzzle.okr.models.KeyResult;
import org.springframework.stereotype.Service;

@Service
public class KeyResultMapper {
    public KeyResultDTO toDto(KeyResult keyResult) {
        return new KeyResultDTO(
                keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getQuarter().getNumber(), keyResult.getQuarter().getYear(),
                keyResult.getExpectedEvolution(), keyResult.getUnit(), keyResult.getBasisValue(), keyResult.getTargetValue());
    }
}
