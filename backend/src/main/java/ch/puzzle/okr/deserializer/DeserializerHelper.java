package ch.puzzle.okr.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Component
public class DeserializerHelper {

    public DeserializerHelper() {
    }

    public <T> T deserializeMetricOrdinal(JsonParser jsonParser, Map<String, Class<? extends T>> map,
            MetricOrdinalDeserializer deserializer) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);

        String keyResultType = deserializer.getKeyResultType(root);
        if (isKeyResultTypeInvalid(keyResultType, map)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupported entity DTO to deserialize");
        }

        Class<? extends T> aClass = map.get(keyResultType);
        return mapper.readValue(root.toString(), aClass);
    }

    private <T> boolean isKeyResultTypeInvalid(String type, Map<String, Class<? extends T>> map) {
        return type == null || !map.containsKey(type);
    }
}
