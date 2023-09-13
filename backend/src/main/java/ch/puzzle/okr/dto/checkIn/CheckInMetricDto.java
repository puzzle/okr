package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.User;

import java.time.LocalDateTime;

public class CheckInMetricDto extends CheckInDto {
    private Double valueMetric;

    public CheckInMetricDto(Long id, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
            User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, Double valueMetric) {
        super(id, changeInfo, initiatives, confidence, keyResultId, createdBy, createdOn, modifiedOn, checkInType);
        this.valueMetric = valueMetric;
    }

    public Double getValue() {
        return valueMetric;
    }

    public void setValue(Double value) {
        this.valueMetric = value;
    }
}
