package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.deserializer.KeyResultDeserializer;
import ch.puzzle.okr.dto.ActionDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = KeyResultDeserializer.class)
public interface KeyResultDto {
    List<ActionDto> getActionList();
}
