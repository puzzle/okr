package ch.puzzle.okr.mapper.keyresult;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.dto.keyresult.KeyResultOrdinalDto;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import java.util.List;
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

    public KeyResultDto toDto(KeyResult keyResult, List<Action> actionList) {
        if (keyResult instanceof KeyResultMetric keyResultMetric) {
            return keyResultMetricMapper.toDto(keyResultMetric, actionList);
        } else if (keyResult instanceof KeyResultOrdinal keyResultOrdinal) {
            return keyResultOrdinalMapper.toDto(keyResultOrdinal, actionList);
        } else {
            throw new ResponseStatusException(BAD_REQUEST,
                                              String
                                                      .format("The KeyResult %s can't be converted to a metric or ordinal KeyResult",
                                                              keyResult));
        }
    }

    public KeyResult toKeyResult(KeyResultDto keyResultDto) {
        if (keyResultDto instanceof KeyResultMetricDto keyResultMetricDto) {
            return keyResultMetricMapper.toKeyResultMetric(keyResultMetricDto);
        } else if (keyResultDto instanceof KeyResultOrdinalDto keyResultOrdinalDto) {
            return keyResultOrdinalMapper.toKeyResultOrdinal(keyResultOrdinalDto);
        } else {
            throw new ResponseStatusException(BAD_REQUEST,
                                              String
                                                      .format("The provided KeyResultDto %s is neither metric nor ordinal",
                                                              keyResultDto));
        }
    }
}
