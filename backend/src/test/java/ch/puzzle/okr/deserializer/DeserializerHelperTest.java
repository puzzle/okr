package ch.puzzle.okr.deserializer;

import static ch.puzzle.okr.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.dto.checkin.*;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.test.CheckInTestHelpers;
import ch.puzzle.okr.test.KeyResultTestHelpers;
import ch.puzzle.okr.test.TestHelper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class DeserializerHelperTest {

    @InjectMocks
    @Spy
    private DeserializerHelper deserializerHelper;

    @Mock
    private MetricOrdinalDeserializer deserializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @DisplayName("deserialize() should return CheckInMetricDto for metric json")
    @Test
    void deserializeShouldReturnCheckInMetricDtoForMetricJson() throws Exception {
        // arrange
        when(deserializer.getKeyResultType(any())).thenReturn(KEY_RESULT_TYPE_METRIC);
        JsonParser jsonParser = objectMapper.getFactory().createParser(CheckInTestHelpers.CHECK_IN_METRIC_JSON);

        // act
        CheckInDto checkInDto = deserializerHelper.deserializeMetricOrdinal(jsonParser, CHECK_IN_MAP, deserializer);

        // assert
        assertInstanceOf(CheckInMetricDto.class, checkInDto);
        assertCheckInMetricDto((CheckInMetricDto) checkInDto);
    }

    @DisplayName("deserialize() should return CheckInOrdinalDto for ordinal json")
    @Test
    void deserializeShouldReturnCheckInOrdinalDtoForOrdinalJson() throws Exception {
        // arrange
        when(deserializer.getKeyResultType(any())).thenReturn(KEY_RESULT_TYPE_ORDINAL);

        JsonParser jsonParser = objectMapper.getFactory().createParser(CheckInTestHelpers.CHECK_IN_ORDINAL_JSON);

        // act
        CheckInDto checkInDto = deserializerHelper.deserializeMetricOrdinal(jsonParser, CHECK_IN_MAP, deserializer);

        // assert
        assertInstanceOf(CheckInOrdinalDto.class, checkInDto);
        assertCheckInOrdinalDto((CheckInOrdinalDto) checkInDto);
    }

    @DisplayName("deserialize() should return KeyResultMetricDto for metric json")
    @Test
    void deserializeShouldReturnKeyResultMetricDtoForMetricJson() throws Exception {

        when(deserializer.getKeyResultType(any())).thenReturn(KEY_RESULT_TYPE_METRIC);

        JsonParser jsonParser = objectMapper.getFactory().createParser(KeyResultTestHelpers.KEY_RESULT_METRIC_JSON);

        // act
        KeyResultDto keyResultDto = deserializerHelper.deserializeMetricOrdinal(jsonParser, KEY_RESULT_MAP,
                deserializer);

        // assert
        assertInstanceOf(KeyResultMetricDto.class, keyResultDto);
        assertKeyResultMetricDto((KeyResultMetricDto) keyResultDto);
    }

    @DisplayName("deserialize() should return KeyResultOrdinalDto for ordinal json")
    @Test
    void deserializeShouldReturnKeyResultOrdinalDtoForOrdinalJson() throws Exception {
        // arrange

        when(deserializer.getKeyResultType(any())).thenReturn(KEY_RESULT_TYPE_ORDINAL);

        JsonParser jsonParser = objectMapper.getFactory().createParser(KeyResultTestHelpers.KEY_RESULT_ORDINAL_JSON);

        // act
        KeyResultDto keyResultDto = deserializerHelper.deserializeMetricOrdinal(jsonParser, KEY_RESULT_MAP,
                deserializer);

        // assert
        assertInstanceOf(KeyResultOrdinalDto.class, keyResultDto);
        assertKeyResultOrdinalDto((KeyResultOrdinalDto) keyResultDto);
    }

    @DisplayName("CheckIn deserialize() should throw ResponseStatusException if json has no KeyResult id")
    @Test
    void deserializeShouldThrowResponseStatusExceptionIfJsonHasNoKeyResultId() throws Exception {

        // arrange
        String jsonMetric = """
                {
                  "version": 0,
                  "title": "NO_KEY_RESULT_TYPE",
                  "description": "BESCHREIBUNG"
                }
                """;

        when(deserializer.getKeyResultType(any())).thenReturn(null);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonMetric);

        // act + assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            deserializerHelper.deserializeMetricOrdinal(jsonParser, CHECK_IN_MAP, deserializer);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("unsupported entity DTO to deserialize", exception.getReason());
    }

    @DisplayName("deserialize() should throw ResponseStatusException if json has no KeyResult Type")
    @Test
    void deserializeShouldThrowResponseStatusExceptionIfJsonHasNoKeyResultType() throws Exception {
        // arrange
        String jsonMetric = """
                {
                  "id": 45,
                  "version": 0,
                  "title": "NO_KEY_RESULT_TYPE",
                  "description": "BESCHREIBUNG"
                }
                """;
        when(deserializer.getKeyResultType(any())).thenReturn(null);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonMetric);
        Map<String, Class<? extends CheckInDto>> CHECK_IN_MAP = Map.of("", CheckInOrdinalDto.class);
        // act + assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> deserializerHelper
                                                                 .deserializeMetricOrdinal(jsonParser,
                                                                                           CHECK_IN_MAP,
                                                                                           deserializer));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("unsupported entity DTO to deserialize", exception.getReason());
    }

    private void assertCheckInOrdinalDto(CheckInOrdinalDto checkInOrdinalDto) {
        assertEquals(43L, checkInOrdinalDto.id());
        assertEquals("Change_Info", checkInOrdinalDto.changeInfo());
        assertEquals("Initiatives", checkInOrdinalDto.initiatives());
        assertEquals(7, checkInOrdinalDto.confidence());
        assertEquals(1001, checkInOrdinalDto.keyResultId());
        assertEquals(Zone.STRETCH, checkInOrdinalDto.zone());
    }

    private void assertKeyResultMetricDto(KeyResultMetricDto keyResultMetricDto) {
        assertEquals(42L, keyResultMetricDto.id());
        assertEquals("metric", keyResultMetricDto.keyResultType());
        assertEquals("TITLE_METRIC", keyResultMetricDto.title());
        assertEquals("BESCHREIBUNG", keyResultMetricDto.description());
        assertEquals(1.0, keyResultMetricDto.baseline());
        assertEquals(5.0, keyResultMetricDto.stretchGoal());
        assertEquals(TestHelper.NUMBER_UNIT.getUnitName(), keyResultMetricDto.unit().unitName());

        KeyResultUserDto owner = keyResultMetricDto.owner();
        assertOwner(owner);

        KeyResultObjectiveDto objective = keyResultMetricDto.objective();
        assertObjective(objective);
    }

    private void assertKeyResultOrdinalDto(KeyResultOrdinalDto keyResultOrdinalDto) {
        assertEquals(43L, keyResultOrdinalDto.id());
        assertEquals("ordinal", keyResultOrdinalDto.keyResultType());
        assertEquals("TITLE_ORDINAL", keyResultOrdinalDto.title());
        assertEquals("BESCHREIBUNG", keyResultOrdinalDto.description());
        assertEquals("1", keyResultOrdinalDto.commitZone());
        assertEquals("3", keyResultOrdinalDto.targetZone());
        assertEquals("5", keyResultOrdinalDto.stretchZone());

        KeyResultUserDto owner = keyResultOrdinalDto.owner();
        assertOwner(owner);

        KeyResultObjectiveDto objective = keyResultOrdinalDto.objective();
        assertObjective(objective);
    }

    private static void assertCheckInMetricDto(CheckInMetricDto checkInMetricDto) {
        assertEquals(42L, checkInMetricDto.id());
        assertEquals("Change_Info", checkInMetricDto.changeInfo());
        assertEquals("Initiatives", checkInMetricDto.initiatives());
        assertEquals(5, checkInMetricDto.confidence());
        assertEquals(1000, checkInMetricDto.keyResultId());
        assertEquals(23, checkInMetricDto.value());
    }

    private static void assertOwner(KeyResultUserDto owner) {
        assertNotNull(owner);
        assertEquals(1000, owner.id());
        assertEquals("Jaya", owner.firstName());
        assertEquals("Norris", owner.lastName());
    }

    private static void assertObjective(KeyResultObjectiveDto objective) {
        assertNotNull(objective);
        assertEquals(1000, objective.id());
    }
}
