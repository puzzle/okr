package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.dto.checkin.CheckInOrdinalDto;
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

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;

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
        if (keyResultOfCheckIn.getKeyResultType().equals(KEY_RESULT_TYPE_METRIC)) {
            return mapper.readValue(root.toString(), CheckInMetricDto.class);
        }
        return mapper.readValue(root.toString(), CheckInOrdinalDto.class);
    }
}
