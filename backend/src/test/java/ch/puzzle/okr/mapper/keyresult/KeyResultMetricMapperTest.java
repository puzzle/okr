package ch.puzzle.okr.mapper.keyresult;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.mapper.keyresult.helper.TestDataDtoHelper;
import ch.puzzle.okr.mapper.keyresult.helper.TestDataHelper;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.UserBusinessService;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ch.puzzle.okr.mapper.keyresult.helper.AssertHelper.*;
import static ch.puzzle.okr.mapper.keyresult.helper.TestDataHelper.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KeyResultMetricMapperTest {

    private KeyResultMetricMapper keyResultMetricMapper;
    @Mock
    UserBusinessService userBusinessService;
    @Mock
    private ObjectiveBusinessService objectiveBusinessService;
    @Mock
    private CheckInBusinessService checkInBusinessService;
    @InjectMocks
    private ActionMapper actionMapper;
    @Mock
    private KeyResultBusinessService keyResultBusinessService;
    @Mock
    private KeyResultValidationService validator;
    @Mock
    private KeyResultPersistenceService keyResultPersistenceService;

    @BeforeEach
    void setup() {
        actionMapper = new ActionMapper(keyResultBusinessService);
        keyResultMetricMapper = new KeyResultMetricMapper( //
                userBusinessService, //
                objectiveBusinessService, //
                checkInBusinessService, //
                actionMapper);
    }

    @DisplayName("toDto() should map a KeyResultMetric to a Dto")
    @Test
    void toDtoShouldMapKeyResultMetricToDtoWithCheckIn() {
        // arrange
        CheckIn checkIn = TestDataHelper.checkInMetric();
        when(checkInBusinessService.getLastCheckInByKeyResultId(anyLong())).thenReturn(checkIn);

        KeyResultMetric keyResultMetric = keyResultMetric();
        List<Action> actions = List.of(action(keyResultMetric));

        // act
        // TODO naming toKeyResultMetricDto() -> toDto()
        KeyResultDto keyResultDto = keyResultMetricMapper.toKeyResultMetricDto(keyResultMetric, actions);
        KeyResultMetricDto keyResultMetricDto = (KeyResultMetricDto) keyResultDto;

        // assert
        assertNotNull(keyResultMetricDto);
        assertKeyResultMetricDtoWithCheckIn(keyResultMetric, keyResultMetricDto, actions);
    }

    @DisplayName("toDto() should map KeyResultMetric to a Dto without CheckIn")
    @Test
    void toDtoShouldMapKeyResultMetricToDtoWithoutCheckIn() {
        // arrange
        KeyResultMetric keyResultMetric = keyResultMetric();
        List<Action> actions = List.of(action(keyResultMetric));

        // act
        // TODO naming toKeyResultMetricDto() -> toDto()
        KeyResultDto keyResultDto = keyResultMetricMapper.toKeyResultMetricDto(keyResultMetric, actions);
        KeyResultMetricDto keyResultMetricDto = (KeyResultMetricDto) keyResultDto;

        // assert
        assertNotNull(keyResultMetricDto);
        assertKeyResultMetricDtoWithoutCheckIn(keyResultMetric, keyResultMetricDto, actions);
    }

    @DisplayName("toKeyResultMetric() should map Dto to KeyResultMetric")
    @Test
    void toKeyResultMetricShouldMapDtoToKeyResultMetric() {
        // arrange
        when(userBusinessService.getUserById(anyLong())).thenReturn(owner());
        when(objectiveBusinessService.getEntityById(anyLong())).thenReturn(objective());
        KeyResultMetricDto keyResultMetricDto = TestDataDtoHelper.keyResultMetricDto();

        // act
        KeyResult keyResult = keyResultMetricMapper.toKeyResultMetric(keyResultMetricDto);
        KeyResultMetric keyResultMetric = (KeyResultMetric) keyResult;

        // assert
        assertNotNull(keyResultMetric);
        assertKeyResultMetric(keyResultMetricDto, keyResultMetric);
    }
}
