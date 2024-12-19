package ch.puzzle.okr.deserializer;

import static ch.puzzle.okr.test.CheckInTestHelpers.*;
import static ch.puzzle.okr.test.CheckInTestHelpers.CHECK_IN_METRIC_JSON;
import static ch.puzzle.okr.test.KeyResultTestHelpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.test.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckInDeserializerTest {
    @Mock
    private KeyResultBusinessService keyResultBusinessService;
    @InjectMocks
    private CheckInDeserializer checkInDeserializer;
    @Mock
    private DeserializerHelper deserializerHelper;
    @Mock
    DeserializationContext deserializationContext;

    @DisplayName("deserialize() should call helper with correct params")
    @Test
    void deserializeShouldReturnCheckInMetricDtoForMetricJson() throws Exception {
        // arrange
        when(deserializerHelper.deserializeMetricOrdinal(any(), any(), any())) //
                .thenReturn(null);

        JsonParser jsonParser = TestHelper.createJsonParser(CHECK_IN_METRIC_JSON);

        // act
        checkInDeserializer.deserialize(jsonParser, deserializationContext);

        // assert
        verify(deserializerHelper, times(1)).deserializeMetricOrdinal(jsonParser, Constants.CHECK_IN_MAP,
                checkInDeserializer);
    }

    private static Stream<Arguments> checkInTypes() {
        return Stream
                .of(Arguments.of(metricKeyResult, CHECK_IN_METRIC_JSON, "metric"),
                    Arguments.of(ordinalKeyResult, CHECK_IN_ORDINAL_JSON, "ordinal"));
    }

    @DisplayName("deserialize() should call helper with correct params")
    @ParameterizedTest
    @MethodSource("checkInTypes")
    void shouldReturnCorrectKeyResulType(KeyResult kr, String json, String type) throws Exception {
        // arrange
        when(keyResultBusinessService.getEntityById(any())).thenReturn(kr);
        JsonNode jsonNode = TestHelper.getJsonNode(json);

        // act
        String keyResultType = checkInDeserializer.getKeyResultType(jsonNode);

        // assert
        assertEquals(type, keyResultType);
    }

    @Test
    void shouldReturnNullForMissingAttribute() throws Exception {
        // arrange
        JsonNode jsonNode = TestHelper.getJsonNode("");

        // act
        String keyResultType = checkInDeserializer.getKeyResultType(jsonNode);

        // assert
        assertNull(keyResultType);
    }
}