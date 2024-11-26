package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkin.*;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.models.checkin.*;
import ch.puzzle.okr.models.keyresult.*;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
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

    Map<String, Class<? extends CheckInDto>> map = Map.ofEntries(entry(KEY_RESULT_TYPE_METRIC, CheckInMetricDto.class),
            entry(KEY_RESULT_TYPE_ORDINAL, CheckInOrdinalDto.class));

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

        when(keyResultBusinessService.getEntityById(42L)) //
                .thenReturn(KeyResultMetric.Builder.builder() //
                        .withId(42L) //
                        .build());

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonMetric);

        ObjectMapper localObjectMapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = localObjectMapper.readTree(jsonParser);

        // act
        Class<? extends CheckInDto> dezerializerClass = deserializerHelper.getDezerializerClass("id", root, map);

        // assert
        assertEquals(CheckInMetricDto.class, dezerializerClass);
    }

    // private static void assertCheckInMetricDto(CheckInMetricDto checkInMetricDto) {
    // assertEquals(42L, checkInMetricDto.id());
    // assertEquals("Change_Info", checkInMetricDto.changeInfo());
    // assertEquals("Initiatives", checkInMetricDto.initiatives());
    // assertEquals(5, checkInMetricDto.confidence());
    // assertEquals(1000, checkInMetricDto.keyResultId());
    // assertEquals(23, checkInMetricDto.value());
    // }
    //
    // @DisplayName("deserialize() should return CheckInOrdinalDto for ordinal json")
    // @Test
    // void deserializeShouldReturnCheckInOrdinalDtoForOrdinalJson() throws Exception {
    // // arrange
    // String jsonOrdinal = """
    // {
    // "id": 43,
    // "version": 0,
    // "changeInfo": "Change_Info",
    // "initiatives": "Initiatives",
    // "confidence": 7,
    // "keyResultId": 1001,
    // "createdOn": null,
    // "modifiedOn": null,
    // "value": "STRETCH",
    // "writeable": false
    // }
    // """;
    //
    // when(keyResultBusinessService.getEntityById(1001L)) //
    // .thenReturn(KeyResultOrdinal.Builder.builder() //
    // .withId(1001L) //
    // .build());
    //
    // JsonParser jsonParser = objectMapper.getFactory().createParser(jsonOrdinal);
    // DeserializationContext ctxt = mock(DeserializationContext.class);
    //
    // // act
    // CheckInDto checkInDto = checkInDeserializer.deserialize(jsonParser, ctxt);
    //
    // // assert
    // assertNotNull(checkInDto);
    // assertInstanceOf(CheckInOrdinalDto.class, checkInDto);
    //
    // CheckInOrdinalDto checkInOrdinalDto = (CheckInOrdinalDto) checkInDto;
    // assertCheckInOrdinalDto(checkInOrdinalDto);
    // }
    //
    // private void assertCheckInOrdinalDto(CheckInOrdinalDto checkInOrdinalDto) {
    // assertEquals(43L, checkInOrdinalDto.id());
    // assertEquals("Change_Info", checkInOrdinalDto.changeInfo());
    // assertEquals("Initiatives", checkInOrdinalDto.initiatives());
    // assertEquals(7, checkInOrdinalDto.confidence());
    // assertEquals(1001, checkInOrdinalDto.keyResultId());
    // assertEquals(Zone.STRETCH, checkInOrdinalDto.value());
    // }
    //
    // @DisplayName("deserialize() should throw ResponseStatusException if KeyResult is of unsupported type")
    // @Test
    // void deserializeShouldThrowResponseStatusExceptionIfKeyResultIsUnsupportedType() throws Exception {
    // // arrange
    // String json = """
    // {
    // "id": 0,
    // "keyResultId": 1002
    // }
    // """;
    //
    // KeyResult unsupportedKeyResult = new KeyResult() {
    // @Override
    // public String getKeyResultType() {
    // return "not_supported_type";
    // }
    // };
    //
    // when(keyResultBusinessService.getEntityById(1002L)).thenReturn(unsupportedKeyResult);
    //
    // JsonParser jsonParser = objectMapper.getFactory().createParser(json);
    // DeserializationContext ctxt = mock(DeserializationContext.class);
    //
    // // act + assert
    // ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
    // checkInDeserializer.deserialize(jsonParser, ctxt);
    // });
    //
    // assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    // assertEquals("unsupported checkIn DTO to deserialize", exception.getReason());
    // }
    //
    // @DisplayName("deserialize() should throw ResponseStatusException if json has no KeyResult Id")
    // @Test
    // void deserializeShouldThrowResponseStatusExceptionIfJsonHasNoKeyResultId() throws Exception {
    // // arrange
    // String jsonWithoutKeyResultId = """
    // {
    // "id": 0,
    // "changeInfo": "THIS_JSON_WILL_NOT_BE_USED"
    // }
    // """;
    //
    // JsonParser jsonParser = objectMapper.getFactory().createParser(jsonWithoutKeyResultId);
    // DeserializationContext ctxt = mock(DeserializationContext.class);
    //
    // // act + assert
    // ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
    // checkInDeserializer.deserialize(jsonParser, ctxt);
    // });
    //
    // assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    // assertEquals("missing keyResult ID to deserialize checkIn DTO", exception.getReason());
    // }
}