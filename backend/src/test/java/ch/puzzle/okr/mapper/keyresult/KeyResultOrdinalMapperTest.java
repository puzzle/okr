package ch.puzzle.okr.mapper.keyresult;

import static ch.puzzle.okr.mapper.keyresult.helper.AssertHelper.*;
import static ch.puzzle.okr.mapper.keyresult.helper.TestDataHelper.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultOrdinalDto;
import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.mapper.keyresult.helper.TestDataDtoHelper;
import ch.puzzle.okr.mapper.keyresult.helper.TestDataHelper;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.UserBusinessService;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KeyResultOrdinalMapperTest {

    private KeyResultOrdinalMapper keyResultOrdinalMapper;

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
        keyResultOrdinalMapper = new KeyResultOrdinalMapper( //
                                                            userBusinessService, //
                                                            objectiveBusinessService, //
                                                            checkInBusinessService, //
                                                            actionMapper);
    }

    @DisplayName("toDto() should map a KeyResultOrdinal with CheckIn to a Dto")
    @Test
    void toDtoShouldMapKeyResultWithCheckInOrdinalToDto() {
        // arrange
        CheckIn checkIn = TestDataHelper.checkInOrdinal();
        when(checkInBusinessService.getLastCheckInByKeyResultId(anyLong())).thenReturn(checkIn);

        KeyResultOrdinal keyResultOrdinal = keyResultOrdinal();
        List<Action> actions = List.of(action(keyResultOrdinal));

        // act
        KeyResultDto keyResultDto = keyResultOrdinalMapper.toDto(keyResultOrdinal, actions);
        KeyResultOrdinalDto keyResultOrdinalDto = (KeyResultOrdinalDto) keyResultDto;

        // assert
        assertNotNull(keyResultOrdinalDto);
        assertKeyResultOrdinalDtoWithCheckIn(keyResultOrdinal, keyResultOrdinalDto, actions);
    }

    @DisplayName("toDto() should map a KeyResultOrdinal without CheckIn to a Dto")
    @Test
    void toDtoShouldMapKeyResultWithoutCheckInOrdinalToDto() {
        // arrange
        KeyResultOrdinal keyResultOrdinal = keyResultOrdinal();
        List<Action> actions = List.of(action(keyResultOrdinal));

        // act
        KeyResultDto keyResultDto = keyResultOrdinalMapper.toDto(keyResultOrdinal, actions);
        KeyResultOrdinalDto keyResultOrdinalDto = (KeyResultOrdinalDto) keyResultDto;

        // assert
        assertNotNull(keyResultOrdinalDto);
        assertKeyResultOrdinalDtoWithoutCheckIn(keyResultOrdinal, keyResultOrdinalDto, actions);
    }

    @DisplayName("toKeyResultMetric() should map Dto to KeyResultMetric")
    @Test
    void toKeyResultMetricShouldMapDtoToKeyResultMetric() {
        // arrange
        when(userBusinessService.getUserById(anyLong())).thenReturn(owner());
        when(objectiveBusinessService.getEntityById(anyLong())).thenReturn(objective());
        KeyResultOrdinalDto keyResultOrdinalDto = TestDataDtoHelper.keyResultOrdinalDto();

        // act
        KeyResult keyResult = keyResultOrdinalMapper.toKeyResultOrdinal(keyResultOrdinalDto);
        KeyResultOrdinal keyResultOrdinal = (KeyResultOrdinal) keyResult;

        // assert
        assertNotNull(keyResultOrdinal);
        assertKeyResultOrdinal(keyResultOrdinalDto, keyResultOrdinal);
    }

}
