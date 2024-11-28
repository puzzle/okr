package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.test.CheckInTestHelpers;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CheckInDeserializerTest {

    @InjectMocks
    private CheckInDeserializer checkInDeserializer;
    @Mock
    private DeserializerHelper deserializerHelper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @DisplayName("deserialize() should call helper with correct params")
    @Test
    void deserializeShouldReturnCheckInMetricDtoForMetricJson() throws Exception {
        // arrange

        when(deserializerHelper.deserializeMetricOrdinal(any(), any(), any())) //
                .thenReturn(null);

        JsonParser jsonParser = objectMapper.getFactory().createParser(CheckInTestHelpers.CHECK_IN_METRIC_JSON);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // act
        checkInDeserializer.deserialize(jsonParser, ctxt);

        // assert
        verify(deserializerHelper, times(1)).deserializeMetricOrdinal(jsonParser, Constants.CHECK_IN_MAP,
                checkInDeserializer);
    }
}