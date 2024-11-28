package ch.puzzle.okr.deserializer;

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
        JsonNode keyResultIdNode = root.get(identifier);
        Long idFromPath = getIdFromPath();
        if (keyResultIdNode == null && idFromPath == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "missing keyResult ID to deserialize keyResult DTO");
        }
        long id = keyResultIdNode == null ? idFromPath : keyResultIdNode.asLong();
        KeyResult keyResult = keyResultBusinessService.getEntityById(id);

        if (keyResult.getKeyResultType() == null || !map.containsKey(keyResult.getKeyResultType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupported entity DTO to deserialize");
        }

        Class<? extends T> aClass = map.get(keyResult.getKeyResultType());
        return mapper.readValue(root.toString(), aClass);
    }

    private Long getIdFromPath() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        Map attributes = (Map) request.getAttribute("org.springframework.web.servlet.View.pathVariables");
        if (attributes == null) {
            return null;
        }

        return (Long) attributes.getOrDefault("id", null);
    }
}
