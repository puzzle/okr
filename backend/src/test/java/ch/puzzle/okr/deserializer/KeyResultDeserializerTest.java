package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.models.Unit;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class KeyResultDeserializerTest {

    @InjectMocks
    private KeyResultDeserializer keyResultDeserializer;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @DisplayName("deserialize() should return KeyResultMetricDto for metric json")
    @Test
    void deserializeShouldReturnKeyResultMetricDtoForMetricJson() throws Exception {
        // arrange
        String jsonMetric = """
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

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonMetric);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // act
        KeyResultDto keyResultDto = keyResultDeserializer.deserialize(jsonParser, ctxt);

        // assert
        assertNotNull(keyResultDto);
        assertInstanceOf(KeyResultMetricDto.class, keyResultDto);

        KeyResultMetricDto keyResultMetricDto = (KeyResultMetricDto) keyResultDto;
        assertKeyResultMetricDto(keyResultMetricDto);
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

    @DisplayName("deserialize() should return KeyResultOrdinalDto for ordinal json")
    @Test
    void deserializeShouldReturnKeyResultOrdinalDtoForOrdinalJson() throws Exception {
        // arrange
        String jsonMetric = """
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

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonMetric);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // act
        KeyResultDto keyResultDto = keyResultDeserializer.deserialize(jsonParser, ctxt);

        // assert
        assertNotNull(keyResultDto);
        assertInstanceOf(KeyResultOrdinalDto.class, keyResultDto);

        KeyResultOrdinalDto keyResultOrdinalDto = (KeyResultOrdinalDto) keyResultDto;
        assertKeyResultOrdinalDto(keyResultOrdinalDto);
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

    @DisplayName("deserialize() should throw ResponseStatusException if KeyResult is of unsupported type")
    @Test
    void deserializeShouldThrowResponseStatusExceptionIfKeyResultIsUnsupportedType() throws Exception {
        // arrange
        String jsonMetric = """
                {
                  "id": 44,
                  "version": 0,
                  "keyResultType": "unsupported",
                  "title": "TITLE_UNSUPPORTED",
                  "description": "BESCHREIBUNG"
                }
                """;

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonMetric);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // act + assert
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            keyResultDeserializer.deserialize(jsonParser, ctxt);
        });

        assertBadRequest(responseStatusException);
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

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonMetric);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // act + assert
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            keyResultDeserializer.deserialize(jsonParser, ctxt);
        });

        assertBadRequest(responseStatusException);
    }

    private void assertBadRequest(ResponseStatusException responseStatusException) {
        assertEquals("400 BAD_REQUEST \"unsupported keyResult DTO to deserialize\"",
                responseStatusException.getMessage());
        assertEquals("unsupported keyResult DTO to deserialize", responseStatusException.getReason());
    }

}
