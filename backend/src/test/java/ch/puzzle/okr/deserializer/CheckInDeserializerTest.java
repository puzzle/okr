package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.dto.checkin.CheckInOrdinalDto;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckInDeserializerTest {

    @Mock
    private KeyResultBusinessService keyResultBusinessService;

    @InjectMocks
    private CheckInDeserializer checkInDeserializer;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @DisplayName("deserialize() should return CheckInMetricDto for metric json")
    @Test
    void deserializeShouldReturnCheckInMetricDtoForMetricJson() throws Exception {
        // arrange
        String jsonMetric = """
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

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonMetric);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // act
        CheckInDto checkInDto = checkInDeserializer.deserialize(jsonParser, ctxt);

        // assert
        assertNotNull(checkInDto);
        assertInstanceOf(CheckInMetricDto.class, checkInDto);

        CheckInMetricDto checkInMetricDto = (CheckInMetricDto) checkInDto;
        assertCheckInMetricDto(checkInMetricDto);
    }

    private static void assertCheckInMetricDto(CheckInMetricDto checkInMetricDto) {
        assertEquals(42L, checkInMetricDto.id());
        assertEquals("Change_Info", checkInMetricDto.changeInfo());
        assertEquals("Initiatives", checkInMetricDto.initiatives());
        assertEquals(5, checkInMetricDto.confidence());
        assertEquals(1000, checkInMetricDto.keyResultId());
        assertEquals(23, checkInMetricDto.value());
    }

    @DisplayName("deserialize() should return CheckInMetricDto for ordinal json")
    @Test
    void deserializeShouldReturnCheckInOrdinalDtoForOrdinalJson() throws Exception {
        // arrange
        String jsonOrdinal = """
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

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonOrdinal);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // act
        CheckInDto checkInDto = checkInDeserializer.deserialize(jsonParser, ctxt);

        // assert
        assertNotNull(checkInDto);
        assertInstanceOf(CheckInOrdinalDto.class, checkInDto);

        CheckInOrdinalDto checkInOrdinalDto = (CheckInOrdinalDto) checkInDto;
        assertCheckInOrdinalDto(checkInOrdinalDto);
    }

    private void assertCheckInOrdinalDto(CheckInOrdinalDto checkInOrdinalDto) {
        assertEquals(43L, checkInOrdinalDto.id());
        assertEquals("Change_Info", checkInOrdinalDto.changeInfo());
        assertEquals("Initiatives", checkInOrdinalDto.initiatives());
        assertEquals(7, checkInOrdinalDto.confidence());
        assertEquals(1001, checkInOrdinalDto.keyResultId());
        assertEquals(Zone.STRETCH, checkInOrdinalDto.value());
    }

    @DisplayName("deserialize() should throw ResponseStatusException if KeyResult is of unsupported type")
    @Test
    void deserializeShouldThrowResponseStatusExceptionIfKeyResultIsUnsupportedType() throws Exception {
        // arrange
        String json = """
                {
                  "id": 0,
                  "keyResultId": 1002
                }
                """;

        KeyResult unsupportedKeyResult = new KeyResult() {
            @Override
            public String getKeyResultType() {
                return "not_supported_type";
            }
        };

        when(keyResultBusinessService.getEntityById(1002L)).thenReturn(unsupportedKeyResult);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // act + assert
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            checkInDeserializer.deserialize(jsonParser, ctxt);
        });

        assertEquals("400 BAD_REQUEST \"unsupported checkIn DTO to deserialize\"",
                responseStatusException.getMessage());

        assertEquals("unsupported checkIn DTO to deserialize", responseStatusException.getReason());
    }

    @DisplayName("deserialize() should throw ResponseStatusException json has no KeyResult Id")
    @Test
    void deserializeShouldThrowResponseStatusExceptionJsonHasNoKeyResultId() throws Exception {
        // arrange
        String jsonWithoutKeyResultId = """
                {
                  "id": 0,
                  "changeInfo": "THIS_JSON_WILL_NOT_BE_USED"
                }
                """;

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonWithoutKeyResultId);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // act + assert
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            checkInDeserializer.deserialize(jsonParser, ctxt);
        });

        assertEquals("400 BAD_REQUEST \"missing keyResult ID to deserialize checkIn DTO\"",
                responseStatusException.getMessage());

        assertEquals("missing keyResult ID to deserialize checkIn DTO", responseStatusException.getReason());
    }
}