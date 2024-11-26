package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

import static ch.puzzle.okr.Constants.*;

public class KeyResultDeserializer extends JsonDeserializer<KeyResultDto> {

    private final DeserializerHelper deserializerHelper;

    public KeyResultDeserializer(DeserializerHelper deserializerHelper) {
        this.deserializerHelper = deserializerHelper;
    }

    @Override
    public KeyResultDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        String keyResultIdAttribute = "id";
        Class<? extends KeyResultDto> dezerializerClass = deserializerHelper.getDezerializerClass(keyResultIdAttribute,
                root, KEY_RESULT_MAP);

        return mapper.readValue(root.toString(), dezerializerClass);
    }
}
