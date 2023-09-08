package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.User;

import java.time.LocalDateTime;

public class CheckInMetricDto extends CheckInDto {
    private Double value;

    public CheckInMetricDto(Long id, String changeInfo, String initiatives, Integer confidence, KeyResult keyResult,
                            User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, Double value) {
        super(id, changeInfo, initiatives, confidence, keyResult, createdBy, createdOn, modifiedOn, checkInType);
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
