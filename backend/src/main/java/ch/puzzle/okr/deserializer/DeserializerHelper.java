package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class DeserializerHelper {

    private final KeyResultBusinessService keyResultBusinessService;

    public DeserializerHelper(KeyResultBusinessService keyResultBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
    }

    public <T> T deserializeMetricOrdinal(String identifier, JsonParser jsonParser, Map<String, Class<? extends T>> map,
            MetricOrdinalDeserializer deserializer) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        JsonNode keyResultIdNode = root.get(identifier);

        String keyResultType = deserializer.getKeyResultType(keyResultIdNode);
        // TODO invert if statement and method
        if (!isKeyResultTypeValid(keyResultType, map)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupported entity DTO to deserialize");
        }

        Class<? extends T> aClass = map.get(keyResultType);
        return mapper.readValue(root.toString(), aClass);
    }

    private <T> boolean isKeyResultTypeValid(String type, Map<String, Class<? extends T>> map) {
        return type != null && map.containsKey(type);
    }
}
