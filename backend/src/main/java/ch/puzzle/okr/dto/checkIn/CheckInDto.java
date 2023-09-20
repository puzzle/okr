package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.deserializer.CheckInDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CheckInDeserializer.class)
public interface CheckInDto {

}