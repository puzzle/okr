package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.dto.keyresult.KeyResultOrdinalDto;
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
                    String.format("The KeyResult %s can't be converted to a metric or ordinal KeyResult", keyResult));
        }
    }

    public KeyResult toKeyResult(KeyResultDto keyResultDto) {
        if (keyResultDto instanceof KeyResultMetricDto) {
            return keyResultMetricMapper.toKeyResultMetric((KeyResultMetricDto) keyResultDto);
        } else if (keyResultDto instanceof KeyResultOrdinalDto) {
            return keyResultOrdinalMapper.toKeyResultOrdinal((KeyResultOrdinalDto) keyResultDto);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The provided KeyResultDto %s is neither metric nor ordinal", keyResultDto));
        }
    }
}
