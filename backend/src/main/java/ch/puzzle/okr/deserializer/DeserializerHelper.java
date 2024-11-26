package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class DeserializerHelper {

    private final KeyResultBusinessService keyResultBusinessService;

    public DeserializerHelper(KeyResultBusinessService keyResultBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
    }

    public <T> Class<? extends T> getDezerializerClass(String identifier, ObjectNode root,
            Map<String, Class<? extends T>> map) {
        if (!root.has(identifier)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "missing keyResult ID to deserialize keyResult DTO");
        }
        KeyResult keyResult = keyResultBusinessService.getEntityById(root.get(identifier).asLong());

        if (!map.containsKey(keyResult.getKeyResultType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupported entity DTO to deserialize");
        }

        return map.get(keyResult.getKeyResultType());
    }
}
