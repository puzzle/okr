package ch.puzzle.okr.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public class MeasureDto {
    private java.lang.Long id;
    private Long keyResultId;
    private Double value;
    private String changeInfo;
    private String initiatives;
    private Instant measureDate;
    private Long createdById;
    private LocalDateTime createdOn;

    public MeasureDto(Long id, Long keyResultId, Double value, String changeInfo, String initiatives, Long createdById,
            LocalDateTime createdOn, Instant measureDate) {
        this.id = id;
        this.keyResultId = keyResultId;
        this.value = value;
        this.changeInfo = changeInfo;
        this.initiatives = initiatives;
        this.createdById = createdById;
        this.createdOn = createdOn;
        this.measureDate = measureDate;
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public Long getKeyResultId() {
        return keyResultId;
    }

    public Double getValue() {
        return value;
    }

    public String getChangeInfo() {
        return changeInfo;
    }

    public String getInitiatives() {
        return initiatives;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public Instant getMeasureDate() {
        return measureDate;
    }
}
