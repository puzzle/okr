package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.User;

import java.time.LocalDateTime;

public class CheckInAbstractDTO {
    Long id;
    String changeInfo;
    String initiatives;
    Integer confidence;

    Long keyResultId;
    LocalDateTime createdOn;
    LocalDateTime modifiedOn;
    String checkInType;
    String zone;
    Double valueMetric;

    public CheckInAbstractDTO(Long id, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
            LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, String zone, Double valueMetric) {
        this.id = id;
        this.changeInfo = changeInfo;
        this.initiatives = initiatives;
        this.confidence = confidence;
        this.keyResultId = keyResultId;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.checkInType = checkInType;
        this.zone = zone;
        this.valueMetric = valueMetric;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChangeInfo() {
        return changeInfo;
    }

    public void setChangeInfo(String changeInfo) {
        this.changeInfo = changeInfo;
    }

    public String getInitiatives() {
        return initiatives;
    }

    public void setInitiatives(String initiatives) {
        this.initiatives = initiatives;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public Long getKeyResultId() {
        return keyResultId;
    }

    public void setKeyResultId(Long keyResultId) {
        this.keyResultId = keyResultId;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getCheckInType() {
        return checkInType;
    }

    public void setCheckInType(String checkInType) {
        this.checkInType = checkInType;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Double getValue() {
        return valueMetric;
    }

    public void setValue(Double valueMetric) {
        this.valueMetric = valueMetric;
    }
}
