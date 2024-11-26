package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkin.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        String keyResultIdAttribute = "keyResultId";
        Class<? extends CheckInDto> dezerializerClass = deserializerHelper.getDezerializerClass(keyResultIdAttribute,
                root, CHECK_IN_MAP);

        return mapper.readValue(root.toString(), dezerializerClass);
    }
}
