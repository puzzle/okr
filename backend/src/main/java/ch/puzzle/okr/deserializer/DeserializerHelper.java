package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Component
public class DeserializerHelper {

    private final KeyResultBusinessService keyResultBusinessService;

    public DeserializerHelper(KeyResultBusinessService keyResultBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
    }

    public <T> T deserializeMetricOrdinal(String identifier, JsonParser jsonParser, Map<String, Class<? extends T>> map)
            throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        JsonNode keyResultId = root.get(identifier);
        if (keyResultId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "missing keyResult ID to deserialize keyResult DTO");
        }
        KeyResult keyResult = keyResultBusinessService.getEntityById(keyResultId.asLong());

        if (!map.containsKey(keyResult.getKeyResultType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupported entity DTO to deserialize");
        }

        Class<? extends T> aClass = map.get(keyResult.getKeyResultType());
        return mapper.readValue(root.toString(), aClass);
    }
}
