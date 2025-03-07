package ch.puzzle.okr.mapper.keyresult;

import static ch.puzzle.okr.mapper.keyresult.helper.AssertHelper.*;
import static ch.puzzle.okr.mapper.keyresult.helper.TestDataHelper.*;
import static ch.puzzle.okr.test.TestHelper.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.dto.UnitDto;
import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.mapper.UnitMapper;
import ch.puzzle.okr.mapper.keyresult.helper.TestDataDtoHelper;
import ch.puzzle.okr.mapper.keyresult.helper.TestDataHelper;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.business.*;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import ch.puzzle.okr.test.TestHelper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KeyResultMetricMapperTest {

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
    @Mock
    private UnitBusinessService unitBusinessService;
    @Mock
    private UnitMapper unitMapper;

    @BeforeEach
    void setup() {
        actionMapper = new ActionMapper(keyResultBusinessService);
        keyResultMetricMapper = new KeyResultMetricMapper( //
                                                          userBusinessService, //
                                                          objectiveBusinessService, //
                                                          checkInBusinessService, //
                                                          actionMapper,
                                                          unitBusinessService,
                                                          unitMapper);
    }

    @DisplayName("Should map a KeyResultMetric to a Dto with CheckIn when toDto() is called")
    @Test
    void shouldMapKeyResultMetricToDtoWithCheckInWhenToDtoIsCalled() {
        when(unitMapper.toDto(TestHelper.NUMBER_UNIT)).thenReturn(new UnitDto(1L, "NUMBER", TestHelper.glUserDto(), true));
        // arrange
        CheckIn checkIn = TestDataHelper.checkInMetric();
        when(checkInBusinessService.getLastCheckInByKeyResultId(anyLong())).thenReturn(checkIn);


        KeyResultMetric keyResultMetric = keyResultMetric();
        List<Action> actions = List.of(action(keyResultMetric));

        // act
        KeyResultDto keyResultDto = keyResultMetricMapper.toDto(keyResultMetric, actions);
        KeyResultMetricDto keyResultMetricDto = (KeyResultMetricDto) keyResultDto;

        // assert
        assertNotNull(keyResultMetricDto);
        assertKeyResultMetricDtoWithCheckIn(keyResultMetric, keyResultMetricDto, actions);
    }

    @DisplayName("Should map a KeyResultMetric to a Dto without CheckIn when toDto() is called")
    @Test
    void shouldMapKeyResultMetricToDtoWithoutCheckInWhenToDtoIsCalled() {
        when(unitMapper.toDto(TestHelper.NUMBER_UNIT)).thenReturn(new UnitDto(1L, "NUMBER", TestHelper.glUserDto(), true));

        // arrange
        KeyResultMetric keyResultMetric = keyResultMetric();
        List<Action> actions = List.of(action(keyResultMetric));

        // act
        KeyResultDto keyResultDto = keyResultMetricMapper.toDto(keyResultMetric, actions);
        KeyResultMetricDto keyResultMetricDto = (KeyResultMetricDto) keyResultDto;

        // assert
        assertNotNull(keyResultMetricDto);
        assertKeyResultMetricDtoWithoutCheckIn(keyResultMetric, keyResultMetricDto, actions);
    }

    @DisplayName("Should map a Dto to KeyResultMetric when toKeyResultMetric() is called")
    @Test
    void shouldMapDtoToKeyResultMetricWhenToKeyResultMetricIsCalled() {
        // arrange
        when(userBusinessService.getUserById(anyLong())).thenReturn(owner());
        when(objectiveBusinessService.getEntityById(anyLong())).thenReturn(objective());
        when(unitBusinessService.findUnitByName("FTE")).thenReturn(FTE_UNIT);
        KeyResultMetricDto keyResultMetricDto = TestDataDtoHelper.keyResultMetricDto();

        // act
        KeyResult keyResult = keyResultMetricMapper.toKeyResultMetric(keyResultMetricDto);
        KeyResultMetric keyResultMetric = (KeyResultMetric) keyResult;

        // assert
        assertNotNull(keyResultMetric);
        assertKeyResultMetric(keyResultMetricDto, keyResultMetric);
    }
}
