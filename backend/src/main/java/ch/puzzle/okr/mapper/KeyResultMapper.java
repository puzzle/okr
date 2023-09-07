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
        // TODO use instanceOf
        if (keyResultDto.getKeyResultType().equals("metric")) {
            return keyResultMetricMapper.toKeyResultMetric(keyResultDto);
        } else if (keyResultDto.getKeyResultType().equals("ordinal")) {
            return keyResultOrdinalMapper.toKeyResultOrdinal(keyResultDto);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The KeyResultDto " + keyResultDto + " can't be converted to a metric or ordinal KeyResultDto");
        }
    }
}
