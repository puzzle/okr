package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkin.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static ch.puzzle.okr.Constants.*;

@Component
public class CheckInDeserializer extends JsonDeserializer<CheckInDto> {

    private final DeserializerHelper deserializerHelper;

    public CheckInDeserializer(DeserializerHelper deserializerHelper) {
        this.deserializerHelper = deserializerHelper;
    }

    @Override
    public CheckInDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String keyResultIdAttribute = "keyResultId";
        return deserializerHelper.deserializeMetricOrdinal(keyResultIdAttribute, jsonParser, CHECK_IN_MAP);
    }
}
