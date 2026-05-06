package ch.puzzle.okr.deserializer;

import static ch.puzzle.okr.Constants.*;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.*;
import java.io.IOException;

public class KeyResultDeserializer extends ValueDeserializer<KeyResultDto> implements MetricOrdinalDeserializer {

    private final DeserializerHelper deserializerHelper;

    public KeyResultDeserializer(DeserializerHelper deserializerHelper) {
        this.deserializerHelper = deserializerHelper;
    }

    @Override
    public KeyResultDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
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
