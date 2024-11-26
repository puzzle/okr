package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkin.*;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.models.keyresult.*;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
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
        String checkInMetricJson = """
                {
                  "id": 42,
                  "version": 0,
                  "changeInfo": "Change_Info",
                  "initiatives": "Initiatives",
                  "confidence": 5,
                  "keyResultId": 1000,
                  "createdOn": null,
                  "modifiedOn": null,
                  "value": 23.0,
                  "writeable": false
                }
                """;

        when(keyResultBusinessService.getEntityById(1000L)) //
                .thenReturn(KeyResultMetric.Builder.builder() //
                        .withId(1000L) //
                        .build());

        JsonParser jsonParser = objectMapper.getFactory().createParser(checkInMetricJson);

        // act
        CheckInDto checkInDto = deserializerHelper.deserializeMetricOrdinal("keyResultId", jsonParser, CHECK_IN_MAP);

        // assert
        assertInstanceOf(CheckInMetricDto.class, checkInDto);
    }

    @DisplayName("deserialize() should return CheckInOrdinalDto for ordinal json")
    @Test
    void deserializeShouldReturnCheckInOrdinalDtoForOrdinalJson() throws Exception {
        // arrange
        String checkInOrdinalJson = """
                {
                "id": 43,
                "version": 0,
                "changeInfo": "Change_Info",
                "initiatives": "Initiatives",
                "confidence": 7,
                "keyResultId": 1001,
                "createdOn": null,
                "modifiedOn": null,
                "value": "STRETCH",
                "writeable": false
                }
                """;

        when(keyResultBusinessService.getEntityById(1001L)) //
                .thenReturn(KeyResultOrdinal.Builder.builder() //
                        .withId(1001L) //
                        .build());

        JsonParser jsonParser = objectMapper.getFactory().createParser(checkInOrdinalJson);

        // act
        CheckInDto checkInDto = deserializerHelper.deserializeMetricOrdinal("keyResultId", jsonParser, CHECK_IN_MAP);

        // assert
        assertInstanceOf(CheckInOrdinalDto.class, checkInDto);
    }

    @DisplayName("deserialize() should return KeyResultMetricDto for metric json")
    @Test
    void deserializeShouldReturnKeyResultMetricDtoForMetricJson() throws Exception {
        // arrange
        String keyResultMetricJson = """
                {
                  "id": 42,
                  "version": 0,
                  "keyResultType": "metric",
                  "title": "TITLE_METRIC",
                  "description": "BESCHREIBUNG",
                  "baseline": 1.0,
                  "stretchGoal": 5.0,
                  "unit": "NUMBER",
                  "owner": {
                    "id": 1000,
                    "firstname": "Jaya",
                    "lastname": "Norris"
                  },
                  "objective": {
                    "id": 1000,
                    "state": "ongoing-icon.svg",
                    "keyResultQuarterDto": null
                  },
                  "lastCheckIn": null,
                  "createdOn": null,
                  "modifiedOn": null,
                  "writeable": false,
                  "actionList": []
                }
                """;

        when(keyResultBusinessService.getEntityById(42L)) //
                .thenReturn(KeyResultMetric.Builder.builder() //
                        .withId(42L) //
                        .build());

        JsonParser jsonParser = objectMapper.getFactory().createParser(keyResultMetricJson);

        // act
        KeyResultDto keyResultDto = deserializerHelper.deserializeMetricOrdinal("id", jsonParser, KEY_RESULT_MAP);

        // assert
        assertInstanceOf(KeyResultMetricDto.class, keyResultDto);
    }

    @DisplayName("deserialize() should return KeyResultOrdinalDto for ordinal json")
    @Test
    void deserializeShouldReturnKeyResultOrdinalDtoForOrdinalJson() throws Exception {
        // arrange
        String keyResultOrdinalJson = """
                {
                  "id": 43,
                  "version": 0,
                  "keyResultType": "ordinal",
                  "title": "TITLE_ORDINAL",
                  "description": "BESCHREIBUNG",
                  "commitZone": "1",
                  "targetZone": "3",
                  "stretchZone": "5",
                  "owner": {
                    "id": 1000,
                    "firstname": "Jaya",
                    "lastname": "Norris"
                  },
                  "objective": {
                    "id": 1000,
                    "state": "ongoing-icon.svg",
                    "keyResultQuarterDto": null
                  },
                  "lastCheckIn": null,
                  "createdOn": null,
                  "modifiedOn": null,
                  "writeable": false,
                  "actionList": []
                }
                """;

        when(keyResultBusinessService.getEntityById(43L)) //
                .thenReturn(KeyResultOrdinal.Builder.builder() //
                        .withId(43L) //
                        .build());

        JsonParser jsonParser = objectMapper.getFactory().createParser(keyResultOrdinalJson);

        // act
        KeyResultDto keyResultDto = deserializerHelper.deserializeMetricOrdinal("id", jsonParser, KEY_RESULT_MAP);

        // assert
        assertInstanceOf(KeyResultOrdinalDto.class, keyResultDto);
    }
}