package ch.puzzle.okr.models;

import org.hibernate.annotations.Immutable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Immutable
public class Overview {

    @EmbeddedId
    private OverviewId overviewId;
    private String teamName;
    private String objectiveTitle;
    @Enumerated(EnumType.STRING)
    private State objectiveState;
    private Long quarterId;
    private String quarterLabel;
    private String keyResultTitle;
    private Unit unit;
    private Double basisValue;
    private Double targetValue;
    private Double measureValue;
    private Instant measureDate;
    private LocalDateTime createdOn;

    public Overview() {
    }

    private Overview(Builder builder) {
        overviewId = builder.overviewId;
        teamName = builder.teamName;
        objectiveTitle = builder.objectiveTitle;
        objectiveState = builder.objectiveState;
        quarterId = builder.quarterId;
        quarterLabel = builder.quarterLabel;
        keyResultTitle = builder.keyResultTitle;
        unit = builder.unit;
        basisValue = builder.basisValue;
        targetValue = builder.targetValue;
        measureValue = builder.measureValue;
        measureDate = builder.measureDate;
        createdOn = builder.createdOn;
    }

    public OverviewId getOverviewId() {
        return overviewId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getObjectiveTitle() {
        return objectiveTitle;
    }

    public State getObjectiveState() {
        return objectiveState;
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public String getQuarterLabel() {
        return quarterLabel;
    }

    public String getKeyResultTitle() {
        return keyResultTitle;
    }

    public Unit getUnit() {
        return unit;
    }

    public Double getBasisValue() {
        return basisValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public Double getMeasureValue() {
        return measureValue;
    }

    public Instant getMeasureDate() {
        return measureDate;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Overview overview = (Overview) o;
        return Objects.equals(overviewId, overview.overviewId) && Objects.equals(teamName, overview.teamName)
                && Objects.equals(objectiveTitle, overview.objectiveTitle)
                && Objects.equals(objectiveState, overview.objectiveState)
                && Objects.equals(quarterId, overview.quarterId) && Objects.equals(quarterLabel, overview.quarterLabel)
                && Objects.equals(keyResultTitle, overview.keyResultTitle) && unit == overview.unit
                && Objects.equals(basisValue, overview.basisValue) && Objects.equals(targetValue, overview.targetValue)
                && Objects.equals(measureValue, overview.measureValue)
                && Objects.equals(measureDate, overview.measureDate) && Objects.equals(createdOn, overview.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(overviewId, teamName, objectiveTitle, objectiveState, quarterId, quarterLabel,
                keyResultTitle, unit, basisValue, targetValue, measureValue, measureDate, createdOn);
    }

    @Override
    public String toString() {
        return "Overview{" + "overviewId=" + overviewId + ", teamName='" + teamName + '\'' + ", objectiveTitle='"
                + objectiveTitle + '\'' + ", objectiveState=" + objectiveState + ", quarterId=" + quarterId
                + ", quarterLabel='" + quarterLabel + '\'' + ", keyResultTitle='" + keyResultTitle + '\'' + ", unit="
                + unit + ", basisValue=" + basisValue + ", targetValue=" + targetValue + ", measureValue="
                + measureValue + ", measureDate=" + measureDate + ", createdOn=" + createdOn + '}';
    }

    public static final class Builder {
        private OverviewId overviewId;
        private String teamName;
        private String objectiveTitle;
        private State objectiveState;
        private Long quarterId;
        private String quarterLabel;
        private String keyResultTitle;
        private Unit unit;
        private Double basisValue;
        private Double targetValue;
        private Double measureValue;
        private Instant measureDate;
        private LocalDateTime createdOn;

        public Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withOverviewId(OverviewId overviewId) {
            this.overviewId = overviewId;
            return this;
        }

        public Builder withTeamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Builder withObjectiveTitle(String objectiveTitle) {
            this.objectiveTitle = objectiveTitle;
            return this;
        }

        public Builder withObjectiveState(State objectiveState) {
            this.objectiveState = objectiveState;
            return this;
        }

        public Builder withQuarterId(Long quarterId) {
            this.quarterId = quarterId;
            return this;
        }

        public Builder withQuarterLabel(String quarterLabel) {
            this.quarterLabel = quarterLabel;
            return this;
        }

        public Builder withKeyResultTitle(String keyResultTitle) {
            this.keyResultTitle = keyResultTitle;
            return this;
        }

        public Builder withUnit(Unit unit) {
            this.unit = unit;
            return this;
        }

        public Builder withBasisValue(Double basisValue) {
            this.basisValue = basisValue;
            return this;
        }

        public Builder withTargetValue(Double targetValue) {
            this.targetValue = targetValue;
            return this;
        }

        public Builder withMeasureValue(Double measureValue) {
            this.measureValue = measureValue;
            return this;
        }

        public Builder withMeasureDate(Instant measureDate) {
            this.measureDate = measureDate;
            return this;
        }

        public Builder withCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Overview build() {
            return new Overview(this);
        }
    }
}
