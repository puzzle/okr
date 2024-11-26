package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkin.*;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.models.keyresult.*;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.test.CheckInTestHelpers;
import ch.puzzle.okr.test.KeyResultTestHelpers;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static ch.puzzle.okr.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeserializerHelperTest {

    @Mock
    private KeyResultBusinessService keyResultBusinessService;

    @InjectMocks
    @Spy
    private DeserializerHelper deserializerHelper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @DisplayName("deserialize() should return CheckInMetricDto for metric json")
    @Test
    void deserializeShouldReturnCheckInMetricDtoForMetricJson() throws Exception {
        // arrange

        when(keyResultBusinessService.getEntityById(1000L)) //
                .thenReturn(KeyResultMetric.Builder.builder() //
                        .withId(1000L) //
                        .build());

        JsonParser jsonParser = objectMapper.getFactory().createParser(CheckInTestHelpers.CHECK_IN_METRIC_JSON);

        // act
        CheckInDto checkInDto = deserializerHelper.deserializeMetricOrdinal("keyResultId", jsonParser, CHECK_IN_MAP);

        // assert
        assertInstanceOf(CheckInMetricDto.class, checkInDto);
    }

    @DisplayName("deserialize() should return CheckInOrdinalDto for ordinal json")
    @Test
    void deserializeShouldReturnCheckInOrdinalDtoForOrdinalJson() throws Exception {
        // arrange
        when(keyResultBusinessService.getEntityById(1001L)) //
                .thenReturn(KeyResultOrdinal.Builder.builder() //
                        .withId(1001L) //
                        .build());

        JsonParser jsonParser = objectMapper.getFactory().createParser(CheckInTestHelpers.CHECK_IN_ORDINAL_JSON);

        // act
        CheckInDto checkInDto = deserializerHelper.deserializeMetricOrdinal("keyResultId", jsonParser, CHECK_IN_MAP);

        // assert
        assertInstanceOf(CheckInOrdinalDto.class, checkInDto);
    }

    @DisplayName("deserialize() should return KeyResultMetricDto for metric json")
    @Test
    void deserializeShouldReturnKeyResultMetricDtoForMetricJson() throws Exception {

        when(keyResultBusinessService.getEntityById(42L)) //
                .thenReturn(KeyResultMetric.Builder.builder() //
                        .withId(42L) //
                        .build());

        JsonParser jsonParser = objectMapper.getFactory().createParser(KeyResultTestHelpers.KEY_RESULT_METRIC_JSON);

        // act
        KeyResultDto keyResultDto = deserializerHelper.deserializeMetricOrdinal("id", jsonParser, KEY_RESULT_MAP);

        // assert
        assertInstanceOf(KeyResultMetricDto.class, keyResultDto);
    }

    @DisplayName("deserialize() should return KeyResultOrdinalDto for ordinal json")
    @Test
    void deserializeShouldReturnKeyResultOrdinalDtoForOrdinalJson() throws Exception {
        // arrange

        when(keyResultBusinessService.getEntityById(43L)) //
                .thenReturn(KeyResultOrdinal.Builder.builder() //
                        .withId(43L) //
                        .build());

        JsonParser jsonParser = objectMapper.getFactory().createParser(KeyResultTestHelpers.KEY_RESULT_ORDINAL_JSON);

        // act
        KeyResultDto keyResultDto = deserializerHelper.deserializeMetricOrdinal("id", jsonParser, KEY_RESULT_MAP);

        // assert
        assertInstanceOf(KeyResultOrdinalDto.class, keyResultDto);
    }
}