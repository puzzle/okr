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
    private ExpectedEvolution expectedEvolution;
    private Unit unit;
    private Double basicValue;
    private Double targetValue;
    private MeasureDto measure;
    private Long progress;

    public KeyResultMeasureDto(Long id, Long objectiveId, String title, String description, Long ownerId,
            String ownerFirstname, String ownerLastname, ExpectedEvolution expectedEvolution, Unit unit,
            Double basicValue, Double targetValue, MeasureDto measureDto, Long progress) {
        this.id = id;
        this.objectiveId = objectiveId;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.ownerFirstname = ownerFirstname;
        this.ownerLastname = ownerLastname;
        this.expectedEvolution = expectedEvolution;
        this.unit = unit;
        this.basicValue = basicValue;
        this.targetValue = targetValue;
        this.measure = measureDto;
        this.progress = progress;
    }

    public Long getId() {
        return id;
    }

    public Long getObjectiveId() {
        return objectiveId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getOwnerFirstname() {
        return ownerFirstname;
    }

    public String getOwnerLastname() {
        return ownerLastname;
    }

    public ExpectedEvolution getExpectedEvolution() {
        return expectedEvolution;
    }

    public Unit getUnit() {
        return unit;
    }

    public Double getBasicValue() {
        return basicValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public MeasureDto getMeasure() {
        return measure;
    }

    public Long getProgress() {
        return progress;
    }
}
