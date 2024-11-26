package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

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

        String keyResultIdAttribute = "id";
        return deserializerHelper.getDeserializerClass(keyResultIdAttribute, jsonParser, KEY_RESULT_MAP);
    }
}
