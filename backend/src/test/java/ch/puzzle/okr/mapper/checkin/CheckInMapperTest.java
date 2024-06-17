package ch.puzzle.okr.mapper.checkin;

import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.dto.checkin.CheckInOrdinalDto;
import ch.puzzle.okr.mapper.checkin.helper.TestDataDtoHelper;
import ch.puzzle.okr.mapper.checkin.helper.TestDataHelper;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckInMapperTest {

    private CheckInMapper checkInMapper;
    @Mock
    private KeyResultBusinessService keyResultBusinessService;
    @Mock
    private KeyResultValidationService validator;
    @Mock
    private KeyResultPersistenceService keyResultPersistenceService;

    @BeforeEach
    void setup() {
        CheckInMetricMapper checkInMetricMapper = new CheckInMetricMapper(keyResultBusinessService);
        CheckInOrdinalMapper checkInOrdinalMapper = new CheckInOrdinalMapper(keyResultBusinessService);
        checkInMapper = new CheckInMapper(checkInMetricMapper, checkInOrdinalMapper);
    }

    @DisplayName("toDto() should map CheckInMetric to CheckInMetricDto")
    @Test
    void toDtoShouldMapCheckInMetricToCheckInMetricDto() {
        // arrange
        CheckInMetric checkInMetric = TestDataHelper.checkInMetric();

        // act
        CheckInDto checkInDto = checkInMapper.toDto(checkInMetric);

        // assert
        assertEquals(CheckInMetricDto.class, checkInDto.getClass());
    }

    @DisplayName("toDto() should map CheckInOrdinal to a CheckInOrdinalDto")
    @Test
    void toDtoShouldMapCheckInOrdinalToCheckInOrdinalDto() {
        // arrange
        CheckInOrdinal checkInOrdinal = TestDataHelper.checkInOrdinal();

        // act
        CheckInDto checkInDto = checkInMapper.toDto(checkInOrdinal);

        // assert
        assertEquals(CheckInOrdinalDto.class, checkInDto.getClass());
    }

    @DisplayName("toCheckIn() should map Dto to CheckInMetric")
    @Test
    void toCheckInShouldMapDtoToCheckInMetric() {
        // arrange
        KeyResult keyResult = TestDataHelper.keyResult();
        when(keyResultBusinessService.getEntityById(keyResult.getId())).thenReturn(keyResult);

        CheckInMetricDto checkInMetricDto = TestDataDtoHelper.checkInMetricDto();

        // act
        CheckIn check = checkInMapper.toCheckIn(checkInMetricDto);

        // assert
        assertEquals(CheckInMetric.class, check.getClass());
    }

    @DisplayName("toCheckIn() should map Dto to CheckInOrdinal")
    @Test
    void toCheckInShouldMapDtoToCheckInOrdinal() {
        // arrange
        KeyResult keyResult = TestDataHelper.keyResult();
        when(keyResultBusinessService.getEntityById(keyResult.getId())).thenReturn(keyResult);

        CheckInOrdinalDto checkInOrdinalDto = TestDataDtoHelper.checkInOrdinalDto();

        // act
        CheckIn check = checkInMapper.toCheckIn(checkInOrdinalDto);

        // assert
        assertEquals(CheckInOrdinal.class, check.getClass());
    }

    @DisplayName("toDto() should throw Exception if CheckIn is not Metric or Ordinal")
    @Test
    void toDtoShouldThrowExceptionIfCheckInIsNotMetricOrOrdinal() {
        // arrange
        CheckIn checkIn = new CheckIn() {
        };

        // act + assert
        ResponseStatusException responseStatusException = assertThrows( //
                ResponseStatusException.class, //
                () -> checkInMapper.toDto(checkIn));
        assertEquals(HttpStatus.BAD_REQUEST, responseStatusException.getStatusCode());
    }

    @DisplayName("toCheckIn() should throw Exception if CheckIn is not MetricDto or OrdinalDto")
    @Test
    void toCheckInShouldThrowExceptionIfCheckInIsNotMetricOrOrdinal() {
        // arrange
        CheckInDto checkInDto = new CheckInDto() {
        };

        // act + assert
        ResponseStatusException responseStatusException = assertThrows( //
                ResponseStatusException.class, //
                () -> checkInMapper.toCheckIn(checkInDto));
        assertEquals(HttpStatus.BAD_REQUEST, responseStatusException.getStatusCode());
    }
}
