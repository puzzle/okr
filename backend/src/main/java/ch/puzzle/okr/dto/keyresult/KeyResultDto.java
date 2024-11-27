package ch.puzzle.okr.dto.keyresult;

import java.util.List;

import ch.puzzle.okr.deserializer.KeyResultDeserializer;
import ch.puzzle.okr.dto.ActionDto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = KeyResultDeserializer.class)
public interface KeyResultDto {
    List<ActionDto> getActionList();
}
