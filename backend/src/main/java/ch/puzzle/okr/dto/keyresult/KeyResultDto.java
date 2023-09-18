package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.deserializer.KeyResultDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = KeyResultDeserializer.class)
public interface KeyResultDto {
}
