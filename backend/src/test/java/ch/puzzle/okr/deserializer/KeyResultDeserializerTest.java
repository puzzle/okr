package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.test.KeyResultTestHelpers;
import ch.puzzle.okr.test.TestHelper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Stream;

import static ch.puzzle.okr.test.KeyResultTestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class KeyResultDeserializerTest {

    @InjectMocks
    private KeyResultDeserializer keyResultDeserializer;

    @Mock
    private DeserializerHelper deserializerHelper;

    @Mock
    DeserializationContext deserializationContext;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @DisplayName("deserialize() should return KeyResultMetricDto for metric json")
    @Test
    void deserializeShouldReturnKeyResultMetricDtoForMetricJson() throws Exception {

        when(deserializerHelper.deserializeMetricOrdinal(any(), any(), any())) //
                .thenReturn(null);
        JsonParser jsonParser = objectMapper.getFactory().createParser(KeyResultTestHelpers.KEY_RESULT_METRIC_JSON);

        // act
        keyResultDeserializer.deserialize(jsonParser, deserializationContext);

        // assert
        verify(deserializerHelper, times(1)).deserializeMetricOrdinal(jsonParser, Constants.KEY_RESULT_MAP,
                keyResultDeserializer);
    }

    private static Stream<Arguments> keyResultTypes() {
        return Stream.of(Arguments.of(KEY_RESULT_METRIC_JSON, "metric"),
                Arguments.of(KEY_RESULT_ORDINAL_JSON, "ordinal"));
    }

    @DisplayName("deserialize() should call helper with correct params")
    @ParameterizedTest
    @MethodSource("keyResultTypes")
    void shouldReturnCorrectKeyResulType(String json, String type) throws Exception {
        // arrange
        JsonNode jsonNode = TestHelper.getJsonNode(json);

        // act
        String keyResultType = keyResultDeserializer.getKeyResultType(jsonNode);

        // assert
        assertEquals(type, keyResultType);
    }

    @Test
    void shouldReturnNullForMissingAttribute() throws Exception {
        // arrange
        JsonNode jsonNode = TestHelper.getJsonNode("");

        // act
        String keyResultType = keyResultDeserializer.getKeyResultType(jsonNode);

        // assert
        assertNull(keyResultType);
    }

}
