package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkIn.CheckInDto;
import ch.puzzle.okr.dto.checkIn.CheckInMetricDto;
import ch.puzzle.okr.dto.checkIn.CheckInOrdinalDto;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CheckInDeserializer extends JsonDeserializer<CheckInDto> {

    private final KeyResultBusinessService keyResultBusinessService;

    public CheckInDeserializer(KeyResultBusinessService keyResultBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
    }

    @Override
    public CheckInDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        KeyResult keyResultOfCheckIn = keyResultBusinessService.getKeyResultById(root.get("keyResultId").asLong());
        if (keyResultOfCheckIn.getKeyResultType().equals("metric")) {
            return mapper.readValue(root.toString(), CheckInMetricDto.class);
        }
        return mapper.readValue(root.toString(), CheckInOrdinalDto.class);
    }
}
