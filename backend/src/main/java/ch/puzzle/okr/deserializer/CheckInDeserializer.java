package ch.puzzle.okr.deserializer;

import ch.puzzle.okr.dto.checkIn.CheckInDto;
import ch.puzzle.okr.dto.checkIn.CheckInMetricDto;
import ch.puzzle.okr.dto.checkIn.CheckInOrdinalDto;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class CheckInDeserializer extends JsonDeserializer<CheckInDto> {
    @Override
    public CheckInDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        if(root.has("checkInType") && root.get("checkInType").asText().equals("metric")) {
            return mapper.readValue(root.toString(), CheckInMetricDto.class);
        }
        return mapper.readValue(root.toString(), CheckInOrdinalDto.class);
    }
}
