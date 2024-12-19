package ch.puzzle.okr.mapper.checkin;

import static ch.puzzle.okr.mapper.checkin.helper.AssertHelper.assertCheckInOrdinal;
import static ch.puzzle.okr.mapper.checkin.helper.AssertHelper.assertCheckInOrdinalDto;
import static ch.puzzle.okr.mapper.checkin.helper.TestDataDtoHelper.checkInOrdinalDto;
import static ch.puzzle.okr.mapper.checkin.helper.TestDataHelper.checkInOrdinal;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.dto.checkin.CheckInOrdinalDto;
import ch.puzzle.okr.mapper.checkin.helper.TestDataHelper;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CheckInOrdinalMapperTest {

    private CheckInOrdinalMapper checkInOrdinalMapper;
    @Mock
    private KeyResultBusinessService keyResultBusinessService;
    @Mock
    private KeyResultValidationService validator;
    @Mock
    private KeyResultPersistenceService keyResultPersistenceService;

    @BeforeEach
    void setup() {
        checkInOrdinalMapper = new CheckInOrdinalMapper(keyResultBusinessService);
    }

    @DisplayName("toDto() should map CheckInOrdinal to Dto")
    @Test
    void toDtoShouldMapCheckInOrdinalToDto() {
        // arrange
        CheckInOrdinal checkInOrdinal = checkInOrdinal();

        // act
        CheckInOrdinalDto checkInOrdinalDto = checkInOrdinalMapper.toDto(checkInOrdinal);

        // assert
        assertNotNull(checkInOrdinalDto);
        assertCheckInOrdinalDto(checkInOrdinal, checkInOrdinalDto);
    }

    @DisplayName("toCheckInOrdinal() should map Dto to checkInOrdinal")
    @Test
    void toCheckInOrdinalShouldMapDtoToCheckInOrdinal() {
        // arrange
        KeyResult keyResult = TestDataHelper.keyResult();
        when(keyResultBusinessService.getEntityById(keyResult.getId())).thenReturn(keyResult);

        CheckInOrdinalDto checkInOrdinalDto = checkInOrdinalDto();

        // act
        CheckIn checkIn = checkInOrdinalMapper.toCheckInOrdinal(checkInOrdinalDto);
        CheckInOrdinal checkInOrdinal = (CheckInOrdinal) checkIn;

        // assert
        assertNotNull(checkInOrdinal);
        assertCheckInOrdinal(checkInOrdinalDto, checkInOrdinal);
    }

}
