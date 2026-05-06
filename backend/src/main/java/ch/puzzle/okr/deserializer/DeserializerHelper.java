package ch.puzzle.okr.deserializer;

import tools.jackson.core.JsonParser;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DeserializerHelper {

    public <T> T deserializeMetricOrdinal(JsonParser jsonParser, Map<String, Class<? extends T>> validTypes,
                                          MetricOrdinalDeserializer deserializer) {

        ObjectReadContext readContext = jsonParser.objectReadContext();

        ObjectNode root = (ObjectNode) readContext.readTree(jsonParser);

        String keyResultType = deserializer.getKeyResultType(root);
        if (isKeyResultTypeInvalid(keyResultType, validTypes)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupported entity DTO to deserialize");
        }

        Class<? extends T> targetClass = validTypes.get(keyResultType);

        JsonParser treeParser = root.traverse(readContext);
        return readContext.readValue(treeParser, targetClass);
    }

    private <T> boolean isKeyResultTypeInvalid(String type, Map<String, Class<? extends T>> validTypes) {
        return type == null || !validTypes.containsKey(type);
    }
}