package ch.puzzle.okr.dto.keyresult;

import java.time.LocalDateTime;

public class KeyResultAbstract {
    Long id;
    String keyResultType;
    String title;
    String description;
    Double baseline;
    Double stretchGoal;
    String unit;
    String commitZone;
    String targetZone;
    String stretchZone;
    KeyResultUserDto owner;
    KeyResultObjectiveDto objective;
    KeyResultLastCheckInDto lastCheckIn;
    KeyResultUserDto createdBy;
    LocalDateTime createdOn;
    LocalDateTime modifiedOn;

    public KeyResultAbstract() {
    }

    public KeyResultAbstract(Long id, String keyResultType, String title, String description, Double baseline,
            Double stretchGoal, String unit, String commitZone, String targetZone, String stretchZone,
            KeyResultUserDto owner, KeyResultObjectiveDto objective, KeyResultLastCheckInDto lastCheckIn,
            KeyResultUserDto createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn) {
        this.id = id;
        this.keyResultType = keyResultType;
        this.title = title;
        this.description = description;
        this.baseline = baseline;
        this.stretchGoal = stretchGoal;
        this.unit = unit;
        this.commitZone = commitZone;
        this.targetZone = targetZone;
        this.stretchZone = stretchZone;
        this.owner = owner;
        this.objective = objective;
        this.lastCheckIn = lastCheckIn;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyResultType() {
        return keyResultType;
    }

    public void setKeyResultType(String keyResultType) {
        this.keyResultType = keyResultType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBaseline() {
        return baseline;
    }

    public void setBaseline(Double baseline) {
        this.baseline = baseline;
    }

    public Double getStretchGoal() {
        return stretchGoal;
    }

    public void setStretchGoal(Double stretchGoal) {
        this.stretchGoal = stretchGoal;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCommitZone() {
        return commitZone;
    }

    public void setCommitZone(String commitZone) {
        this.commitZone = commitZone;
    }

    public String getTargetZone() {
        return targetZone;
    }

    public void setTargetZone(String targetZone) {
        this.targetZone = targetZone;
    }

    public String getStretchZone() {
        return stretchZone;
    }

    public void setStretchZone(String stretchZone) {
        this.stretchZone = stretchZone;
    }

    public KeyResultUserDto getOwner() {
        return owner;
    }

    public void setOwner(KeyResultUserDto owner) {
        this.owner = owner;
    }

    public KeyResultObjectiveDto getObjective() {
        return objective;
    }

    public void setObjective(KeyResultObjectiveDto objective) {
        this.objective = objective;
    }

    public KeyResultLastCheckInDto getLastCheckIn() {
        return lastCheckIn;
    }

    public void setLastCheckIn(KeyResultLastCheckInDto lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
    }

    public KeyResultUserDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(KeyResultUserDto createdBy) {
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
}
