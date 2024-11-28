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

    public <T> T deserializeMetricOrdinal(JsonParser jsonParser, Map<String, Class<? extends T>> validTypes,
            MetricOrdinalDeserializer deserializer) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);

        String keyResultType = deserializer.getKeyResultType(root);
        if (isKeyResultTypeInvalid(keyResultType, validTypes)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupported entity DTO to deserialize");
        }

        Class<? extends T> targetClass = validTypes.get(keyResultType);
        return mapper.readValue(root.toString(), targetClass);
    }

    private <T> boolean isKeyResultTypeInvalid(String type, Map<String, Class<? extends T>> validTypes) {
        return type == null || !validTypes.containsKey(type);
    }
}
