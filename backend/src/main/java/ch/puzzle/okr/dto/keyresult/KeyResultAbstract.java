package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.dto.MeasureDto;

import java.time.LocalDate;
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
    Long ownerId;
    String ownerFirstname;
    String ownerLastname;
    Long objectiveId;
    String objectiveState;
    Long objectiveQuarterId;
    String objectiveQuarterLabel;
    LocalDate objectiveQuarterStartDate;
    LocalDate objectiveQuarterEndDate;
    MeasureDto lastCheckIn;
    Long createdById;
    String createdByFirstname;
    String createdByLastname;
    LocalDateTime createdOn;
    LocalDateTime modifiedOn;

    public KeyResultAbstract() {
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerFirstname() {
        return ownerFirstname;
    }

    public void setOwnerFirstname(String ownerFirstname) {
        this.ownerFirstname = ownerFirstname;
    }

    public String getOwnerLastname() {
        return ownerLastname;
    }

    public void setOwnerLastname(String ownerLastname) {
        this.ownerLastname = ownerLastname;
    }

    public Long getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(Long objectiveId) {
        this.objectiveId = objectiveId;
    }

    public String getObjectiveState() {
        return objectiveState;
    }

    public void setObjectiveState(String objectiveState) {
        this.objectiveState = objectiveState;
    }

    public Long getObjectiveQuarterId() {
        return objectiveQuarterId;
    }

    public void setObjectiveQuarterId(Long objectiveQuarterId) {
        this.objectiveQuarterId = objectiveQuarterId;
    }

    public String getObjectiveQuarterLabel() {
        return objectiveQuarterLabel;
    }

    public void setObjectiveQuarterLabel(String objectiveQuarterLabel) {
        this.objectiveQuarterLabel = objectiveQuarterLabel;
    }

    public LocalDate getObjectiveQuarterStartDate() {
        return objectiveQuarterStartDate;
    }

    public void setObjectiveQuarterStartDate(LocalDate objectiveQuarterStartDate) {
        this.objectiveQuarterStartDate = objectiveQuarterStartDate;
    }

    public LocalDate getObjectiveQuarterEndDate() {
        return objectiveQuarterEndDate;
    }

    public void setObjectiveQuarterEndDate(LocalDate objectiveQuarterEndDate) {
        this.objectiveQuarterEndDate = objectiveQuarterEndDate;
    }

    public MeasureDto getLastCheckIn() {
        return lastCheckIn;
    }

    public void setLastCheckIn(MeasureDto lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByFirstname() {
        return createdByFirstname;
    }

    public void setCreatedByFirstname(String createdByFirstname) {
        this.createdByFirstname = createdByFirstname;
    }

    public String getCreatedByLastname() {
        return createdByLastname;
    }

    public void setCreatedByLastname(String createdByLastname) {
        this.createdByLastname = createdByLastname;
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
