package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.dto.keyresult.KeyResultOrdinalDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Map;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static java.util.Map.entry;

public class KeyResultDeserializer extends JsonDeserializer<KeyResultDto> {

    private final DeserializerHelper deserializerHelper;

    public KeyResultDeserializer(DeserializerHelper deserializerHelper) {
        this.deserializerHelper = deserializerHelper;
    }

    Map<String, Class<? extends KeyResultDto>> map = Map.ofEntries(
            entry(KEY_RESULT_TYPE_METRIC, KeyResultMetricDto.class),
            entry(KEY_RESULT_TYPE_ORDINAL, KeyResultOrdinalDto.class));

    @Override
    public KeyResultDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        String keyResultIdAttribute = "id";
        Class<? extends KeyResultDto> dezerializerClass = deserializerHelper.getDezerializerClass(keyResultIdAttribute,
                root, map);

        return mapper.readValue(root.toString(), dezerializerClass);
    }
}
