package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkin.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static java.util.Map.entry;

@Component
public class CheckInDeserializer extends JsonDeserializer<CheckInDto> {

    private final DeserializerHelper deserializerHelper;
    Map<String, Class<? extends CheckInDto>> map = Map.ofEntries(entry(KEY_RESULT_TYPE_METRIC, CheckInMetricDto.class),
            entry(KEY_RESULT_TYPE_ORDINAL, CheckInOrdinalDto.class));

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
                root, map);

        return mapper.readValue(root.toString(), dezerializerClass);
    }
}
