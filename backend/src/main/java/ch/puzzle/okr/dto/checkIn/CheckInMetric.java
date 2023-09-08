package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.User;

import java.time.LocalDateTime;

public class CheckInMetric extends CheckInDto {
    Double value;
    public CheckInMetric(Long id, String changeInfo, String initiatives, Integer confidence, KeyResult keyResult, User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, Double value) {
        super(id, changeInfo, initiatives, confidence, keyResult, createdBy, createdOn, modifiedOn, checkInType);
        this.value = value;
    }
}
