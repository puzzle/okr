package ch.puzzle.okr.dto.checkin;

import ch.puzzle.okr.deserializer.CheckInDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CheckInDeserializer.class)
public interface CheckInDto {

}