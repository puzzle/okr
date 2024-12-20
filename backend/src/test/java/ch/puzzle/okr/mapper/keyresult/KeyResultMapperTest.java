package ch.puzzle.okr.mapper.keyresult;

import static ch.puzzle.okr.mapper.keyresult.helper.TestDataDtoHelper.keyResultMetricDto;
import static ch.puzzle.okr.mapper.keyresult.helper.TestDataDtoHelper.keyResultOrdinalDto;
import static ch.puzzle.okr.mapper.keyresult.helper.TestDataHelper.keyResultMetric;
import static ch.puzzle.okr.mapper.keyresult.helper.TestDataHelper.keyResultOrdinal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.dto.keyresult.KeyResultOrdinalDto;
import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.UserBusinessService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class KeyResultMapperTest {

    private KeyResultMapper keyResultMapper;
    @Mock
    UserBusinessService userBusinessService;
    @Mock
    private ObjectiveBusinessService objectiveBusinessService;
    @Mock
    private CheckInBusinessService checkInBusinessService;
    @InjectMocks
    private ActionMapper actionMapper;

    @BeforeEach
    void setup() {
        KeyResultMetricMapper keyResultMetricMapper = new KeyResultMetricMapper( //
                                                                                userBusinessService, //
                                                                                objectiveBusinessService, //
                                                                                checkInBusinessService, //
                                                                                actionMapper);

        KeyResultOrdinalMapper keyResultOrdinalMapper = new KeyResultOrdinalMapper( //
                                                                                   userBusinessService, //
                                                                                   objectiveBusinessService, //
                                                                                   checkInBusinessService, //
                                                                                   actionMapper);

        keyResultMapper = new KeyResultMapper(keyResultOrdinalMapper, keyResultMetricMapper);
    }

    @DisplayName("toDto() should map KeyResultMetric to KeyResultMetricDto")
    @Test
    void toDtoShouldMapKeyResultMetricToKeyResultMetricDto() {
        // arrange
        KeyResultMetric keyResultMetric = keyResultMetric();
        List<Action> actions = List.of();

        // act
        KeyResultDto keyResultDto = keyResultMapper.toDto(keyResultMetric, actions);

        // assert
        assertEquals(KeyResultMetricDto.class, keyResultDto.getClass());
    }

    @DisplayName("toDto() should map KeyResultOrdinal to KeyResultOrdinalDto")
    @Test
    void toDtoShouldMapKeyResultOrdinalToKeyResultOrdinalDto() {
        // arrange
        KeyResultOrdinal keyResultOrdinal = keyResultOrdinal();
        List<Action> actions = List.of();

        // act
        KeyResultDto keyResultDto = keyResultMapper.toDto(keyResultOrdinal, actions);

        // assert
        assertEquals(KeyResultOrdinalDto.class, keyResultDto.getClass());
    }

    @DisplayName("toKeyResult() should map KeyResultMetricDto to KeyResultMetric")
    @Test
    void toKeyResultShouldMapKeyResultMetricDtoToKeyResultMetric() {
        // arrange
        KeyResultMetricDto keyResultMetricDto = keyResultMetricDto();

        // act
        KeyResult keyResult = keyResultMapper.toKeyResult(keyResultMetricDto);

        // assert
        assertEquals(KeyResultMetric.class, keyResult.getClass());
    }

    @DisplayName("toKeyResult() should map KeyResultOrdinalDto to KeyResultOrdinal")
    @Test
    void toKeyResultShouldMapKeyResultOrdinalDtoToKeyResultOrdinal() {
        // arrange
        KeyResultOrdinalDto keyResultOrdinalDto = keyResultOrdinalDto();

        // act
        KeyResult keyResult = keyResultMapper.toKeyResult(keyResultOrdinalDto);

        // assert
        assertEquals(KeyResultOrdinal.class, keyResult.getClass());
    }

    @DisplayName("toDto() should throw Exception if KeyResult is not Metric or Ordinal")
    @Test
    void toDtoShouldThrowExceptionIfKeyResultIsNotMetricOrOrdinal() {
        // arrange
        KeyResult keyResult = new KeyResult() {
        };
        List<Action> actions = List.of();

        // act + assert
        ResponseStatusException responseStatusException = assertThrows( //
                                                                       ResponseStatusException.class, //
                                                                       () -> keyResultMapper.toDto(keyResult, actions));
        assertEquals(HttpStatus.BAD_REQUEST, responseStatusException.getStatusCode());
    }

    @DisplayName("toKeyResult() should throw Exception if KeyResultDto is not MetricDto or OrdinalDto")
    @Test
    void toKeyResultShouldThrowExceptionIfKeyResultDtoIsNotMetricOrOrdinal() {
        // arrange
        KeyResultDto keyResultDto = List::of;

        // act + assert
        ResponseStatusException responseStatusException = assertThrows( //
                                                                       ResponseStatusException.class, //
                                                                       () -> keyResultMapper.toKeyResult(keyResultDto));
        assertEquals(HttpStatus.BAD_REQUEST, responseStatusException.getStatusCode());
    }
}
