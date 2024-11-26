package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkin.*;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.checkin.Zone;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    private static void assertCheckInMetricDto(CheckInMetricDto checkInMetricDto) {
        assertEquals(42L, checkInMetricDto.id());
        assertEquals("Change_Info", checkInMetricDto.changeInfo());
        assertEquals("Initiatives", checkInMetricDto.initiatives());
        assertEquals(5, checkInMetricDto.confidence());
        assertEquals(1000, checkInMetricDto.keyResultId());
        assertEquals(23, checkInMetricDto.value());
    }

    private void assertCheckInOrdinalDto(CheckInOrdinalDto checkInOrdinalDto) {
        assertEquals(43L, checkInOrdinalDto.id());
        assertEquals("Change_Info", checkInOrdinalDto.changeInfo());
        assertEquals("Initiatives", checkInOrdinalDto.initiatives());
        assertEquals(7, checkInOrdinalDto.confidence());
        assertEquals(1001, checkInOrdinalDto.keyResultId());
        assertEquals(Zone.STRETCH, checkInOrdinalDto.value());
    }

    private void assertKeyResultMetricDto(KeyResultMetricDto keyResultMetricDto) {
        assertEquals(42L, keyResultMetricDto.id());
        assertEquals("metric", keyResultMetricDto.keyResultType());
        assertEquals("TITLE_METRIC", keyResultMetricDto.title());
        assertEquals("BESCHREIBUNG", keyResultMetricDto.description());
        assertEquals(1.0, keyResultMetricDto.baseline());
        assertEquals(5.0, keyResultMetricDto.stretchGoal());
        assertEquals(Unit.NUMBER, keyResultMetricDto.unit());

        KeyResultUserDto owner = keyResultMetricDto.owner();
        assertOwner(owner);

        KeyResultObjectiveDto objective = keyResultMetricDto.objective();
        assertObjective(objective);
    }

    private static void assertOwner(KeyResultUserDto owner) {
        assertNotNull(owner);
        assertEquals(1000, owner.id());
        assertEquals("Jaya", owner.firstname());
        assertEquals("Norris", owner.lastname());
    }

    private static void assertObjective(KeyResultObjectiveDto objective) {
        assertNotNull(objective);
        assertEquals(1000, objective.id());
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

    private void assertBadRequest(ResponseStatusException exception) {
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("unsupported keyResult DTO to deserialize", exception.getReason());
    }
}