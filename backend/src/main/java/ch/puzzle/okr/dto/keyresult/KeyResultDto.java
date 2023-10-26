package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.deserializer.KeyResultDeserializer;
import ch.puzzle.okr.models.Action;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = KeyResultDeserializer.class)
public interface KeyResultDto {
    public List<Action> getActionList();
}
