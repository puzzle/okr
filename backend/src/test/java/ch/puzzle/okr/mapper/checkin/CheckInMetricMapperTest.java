package ch.puzzle.okr.mapper.checkin;

import static ch.puzzle.okr.mapper.checkin.helper.AssertHelper.assertCheckInMetric;
import static ch.puzzle.okr.mapper.checkin.helper.AssertHelper.assertCheckInMetricDto;
import static ch.puzzle.okr.mapper.checkin.helper.TestDataDtoHelper.checkInMetricDto;
import static ch.puzzle.okr.mapper.checkin.helper.TestDataHelper.checkInMetric;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.mapper.checkin.helper.TestDataHelper;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
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
class CheckInMetricMapperTest {

    private CheckInMetricMapper checkInMetricMapper;
    @Mock
    private KeyResultBusinessService keyResultBusinessService;
    @Mock
    private KeyResultValidationService validator;
    @Mock
    private KeyResultPersistenceService keyResultPersistenceService;

    @BeforeEach
    void setup() {
        checkInMetricMapper = new CheckInMetricMapper(keyResultBusinessService);
    }

    @DisplayName("Should map check-in-metric to dto when calling toDto()")
    @Test
    void shouldMapCheckInMetricToDto() {
        // arrange
        CheckInMetric checkInMetric = checkInMetric();

        // act
        CheckInMetricDto checkInMetricDto = checkInMetricMapper.toDto(checkInMetric);

        // assert
        assertNotNull(checkInMetricDto);
        assertCheckInMetricDto(checkInMetric, checkInMetricDto);
    }

    @DisplayName("Should map dto to check-in-metric when calling toCheckInMetric()")
    @Test
    void shouldMapDtoToCheckInMetric() {
        // arrange
        KeyResult keyResult = TestDataHelper.keyResult();
        when(keyResultBusinessService.getEntityById(keyResult.getId())).thenReturn(keyResult);

        CheckInMetricDto checkInMetricDto = checkInMetricDto();

        // act
        CheckIn checkIn = checkInMetricMapper.toCheckInMetric(checkInMetricDto);
        CheckInMetric checkInMetric = (CheckInMetric) checkIn;

        // assert
        assertNotNull(checkInMetricDto);
        assertCheckInMetric(checkInMetricDto, checkInMetric);
    }
}
