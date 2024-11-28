package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

import static ch.puzzle.okr.Constants.*;

public class KeyResultDeserializer extends JsonDeserializer<KeyResultDto> implements MetricOrdinalDeserializer {

    private final DeserializerHelper deserializerHelper;

    public KeyResultDeserializer(DeserializerHelper deserializerHelper) {

        this.deserializerHelper = deserializerHelper;
    }

    @Override
    public KeyResultDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        return deserializerHelper.deserializeMetricOrdinal(jsonParser, KEY_RESULT_MAP, this);
    }

    @Override
    public String getKeyResultType(JsonNode root) {
        JsonNode keyResultId = root.get(KEY_RESULT_TYPE_ATTRIBUTE_NAME);
        if (keyResultId == null) {
            return null;
        }
        return keyResultId.asText();
    }
}
