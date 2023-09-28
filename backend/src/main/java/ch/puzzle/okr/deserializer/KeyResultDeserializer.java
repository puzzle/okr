package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.dto.keyresult.KeyResultOrdinalDto;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;

public class KeyResultDeserializer extends JsonDeserializer<KeyResultDto> {
    @Override
    public KeyResultDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        if (root.has("keyResultType") && root.get("keyResultType").asText().equals(KEY_RESULT_TYPE_METRIC)) {
            return mapper.readValue(root.toString(), KeyResultMetricDto.class);
        }
        return mapper.readValue(root.toString(), KeyResultOrdinalDto.class);
    }
}
