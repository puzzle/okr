package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultAbstractDto;
import ch.puzzle.okr.mapper.keyresult.KeyResultMetricMapper;
import ch.puzzle.okr.mapper.keyresult.KeyResultOrdinalMapper;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class KeyResultMapper {

    // TODO: Remove UserService when Login works and use logged in user for createdBy in toKeyResult method
    private final KeyResultMetricMapper keyResultMetricMapper;
    private final KeyResultOrdinalMapper keyResultOrdinalMapper;

    public KeyResultMapper(KeyResultOrdinalMapper keyResultOrdinalMapper, KeyResultMetricMapper keyResultMetricMapper) {
        this.keyResultOrdinalMapper = keyResultOrdinalMapper;
        this.keyResultMetricMapper = keyResultMetricMapper;
    }

    public KeyResultDto toDto(KeyResult keyResult) {
        if (keyResult instanceof KeyResultMetric keyResultMetric) {
            return keyResultMetricMapper.toKeyResultMetricDto(keyResultMetric);
        } else if (keyResult instanceof KeyResultOrdinal keyResultOrdinal) {
            return keyResultOrdinalMapper.toKeyResultOrdinalDto(keyResultOrdinal);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The KeyResult " + keyResult + " can't be converted to a metric or ordinal KeyResult");
        }
    }

    public KeyResult toKeyResult(KeyResultAbstractDto keyResultDto) {
        if (isMetricKeyResult(keyResultDto)) {
            return keyResultMetricMapper.toKeyResultMetric(keyResultDto);
        } else if (isOrdinalKeyResult(keyResultDto)) {
            return keyResultOrdinalMapper.toKeyResultOrdinal(keyResultDto);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The provided KeyResultDto is neither metric nor ordinal");
        }
    }

    private boolean isMetricKeyResult(KeyResultAbstractDto keyResultDto) {
        return keyResultDto.getKeyResultType().equals("metric") && keyResultDto.getBaseline() != null
                && keyResultDto.getStretchGoal() != null && keyResultDto.getUnit() != null;
    }

    private boolean isOrdinalKeyResult(KeyResultAbstractDto keyResultDto) {
        return keyResultDto.getKeyResultType().equals("ordinal") && keyResultDto.getCommitZone() != null
                && keyResultDto.getTargetZone() != null && keyResultDto.getStretchZone() != null;

    }
}
