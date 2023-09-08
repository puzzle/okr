package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.User;

import java.time.LocalDateTime;

public abstract class CheckInDto {
    Long id;
    String changeInfo;
    String initiatives;
    Integer confidence;
    KeyResult keyResult;
    User createdBy;
    LocalDateTime createdOn;
    LocalDateTime modifiedOn;
    String checkInType;

    public CheckInDto(Long id, String changeInfo, String initiatives, Integer confidence, KeyResult keyResult,
            User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType) {
        this.id = id;
        this.changeInfo = changeInfo;
        this.initiatives = initiatives;
        this.confidence = confidence;
        this.keyResult = keyResult;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.checkInType = checkInType;
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

    public KeyResult getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(KeyResult keyResult) {
        this.keyResult = keyResult;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
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
}
