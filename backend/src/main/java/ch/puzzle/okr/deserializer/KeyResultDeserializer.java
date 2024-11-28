package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

        String keyResultIdAttribute = "id";
        return deserializerHelper.deserializeMetricOrdinal(keyResultIdAttribute, jsonParser, KEY_RESULT_MAP, this);
    }

    @Override
    public String getKeyResultType(JsonNode root) {
        if (!root.has("keyResultType")) {
            return null;
        }
        return root.get("keyResultType").asText();
    }
}
