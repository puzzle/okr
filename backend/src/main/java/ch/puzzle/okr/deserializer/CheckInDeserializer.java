package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkin.*;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static ch.puzzle.okr.Constants.*;

@Component
public class CheckInDeserializer extends JsonDeserializer<CheckInDto> implements MetricOrdinalDeserializer {

    private final DeserializerHelper deserializerHelper;
    private final KeyResultBusinessService keyResultBusinessService;

    public CheckInDeserializer(DeserializerHelper deserializerHelper,
            KeyResultBusinessService keyResultBusinessService) {
        this.deserializerHelper = deserializerHelper;
        this.keyResultBusinessService = keyResultBusinessService;
    }

    @Override
    public CheckInDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String keyResultIdAttribute = "keyResultId";
        return deserializerHelper.deserializeMetricOrdinal(keyResultIdAttribute, jsonParser, CHECK_IN_MAP, this);
    }

    @Override
    public String getKeyResultType(JsonNode root) {
        String identifier = "keyResultId";
        JsonNode keyResultIdNode = root.get(identifier);
        if (keyResultIdNode == null) {
            return null;
        }
        return keyResultBusinessService.getEntityById(keyResultIdNode.asLong()).getKeyResultType();
    }
}
