package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.test.KeyResultTestHelpers;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
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
}
