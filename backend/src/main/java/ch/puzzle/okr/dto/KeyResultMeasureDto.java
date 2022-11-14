package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.models.Unit;

public class KeyResultMeasureDto {
    private Long id;
    private Long objectiveId;
    private String title;
    private String description;
    private Long ownerId;
    private String ownerFirstname;
    private String ownerLastname;
    private Long quarterId;
    private Integer quarterNumber;
    private Integer quarterYear;
    private ExpectedEvolution expectedEvolution;
    private Unit unit;
    private Long basicValue;
    private Long targetValue;

    public KeyResultMeasureDto(Long id, Long objectiveId, String title, String description, Long ownerId, String ownerFirstname,
                        String ownerLastname, Long quarterId, Integer quarterNumber, Integer quarterYear, ExpectedEvolution expectedEvolution,
                        Unit unit, Long basicValue, Long targetValue) {
        this.id = id;
        this.objectiveId = objectiveId;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.ownerFirstname = ownerFirstname;
        this.ownerLastname = ownerLastname;
        this.quarterId = quarterId;
        this.quarterNumber = quarterNumber;
        this.quarterYear = quarterYear;
        this.expectedEvolution = expectedEvolution;
        this.unit = unit;
        this.basicValue = basicValue;
        this.targetValue = targetValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(Long objectiveId) {
        this.objectiveId = objectiveId;
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

    public Integer getQuarterNumber() {
        return quarterNumber;
    }

    public void setQuarterNumber(Integer quarterNumber) {
        this.quarterNumber = quarterNumber;
    }

    public Integer getQuarterYear() {
        return quarterYear;
    }

    public void setQuarterYear(Integer quarterYear) {
        this.quarterYear = quarterYear;
    }

    public ExpectedEvolution getExpectedEvolution() {
        return expectedEvolution;
    }

    public void setExpectedEvolution(ExpectedEvolution expectedEvolution) {
        this.expectedEvolution = expectedEvolution;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Long getBasicValue() {
        return basicValue;
    }

    public void setBasicValue(Long basicValue) {
        this.basicValue = basicValue;
    }

    public Long getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Long targetValue) {
        this.targetValue = targetValue;
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public void setQuarterId(Long quarterId) {
        this.quarterId = quarterId;
    }
}
