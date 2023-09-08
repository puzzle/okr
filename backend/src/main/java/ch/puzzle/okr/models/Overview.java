package ch.puzzle.okr.models;

import org.hibernate.annotations.Immutable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;
import java.time.LocalDateTime;

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
    private Double baseline;
    private Double stretchGoal;
    private String unit;
    private String commitZone;
    private String targetZone;
    private String stretchZone;
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
        baseline = builder.baseline;
        stretchGoal = builder.stretchGoal;
        unit = builder.unit;
        commitZone = builder.commitZone;
        targetZone = builder.targetZone;
        stretchZone = builder.stretchZone;
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

    public Double getBaseline() {
        return baseline;
    }

    public Double getStretchGoal() {
        return stretchGoal;
    }

    public String getUnit() {
        return unit;
    }

    public String getCommitZone() {
        return commitZone;
    }

    public String getTargetZone() {
        return targetZone;
    }

    public String getStretchZone() {
        return stretchZone;
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
    public String toString() {
        return "Overview{" + "overviewId=" + overviewId + ", teamName='" + teamName + '\'' + ", objectiveTitle='"
                + objectiveTitle + '\'' + ", objectiveState=" + objectiveState + ", quarterId=" + quarterId
                + ", quarterLabel='" + quarterLabel + '\'' + ", keyResultTitle='" + keyResultTitle + '\''
                + ", baseline=" + baseline + ", stretchGoal=" + stretchGoal + ", unit='" + unit + '\''
                + ", commitZone='" + commitZone + '\'' + ", targetZone='" + targetZone + '\'' + ", stretchZone='"
                + stretchZone + '\'' + ", measureValue=" + measureValue + ", measureDate=" + measureDate
                + ", createdOn=" + createdOn + '}';
    }

    public static final class Builder {
        private OverviewId overviewId;
        private String teamName;
        private String objectiveTitle;
        private State objectiveState;
        private Long quarterId;
        private String quarterLabel;
        private String keyResultTitle;
        private Double baseline;
        private Double stretchGoal;
        private String unit;
        private String commitZone;
        private String targetZone;
        private String stretchZone;
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

        public Builder withBaseline(Double baseline) {
            this.baseline = baseline;
            return this;
        }

        public Builder withStretchGoal(Double stretchGoal) {
            this.stretchGoal = stretchGoal;
            return this;
        }

        public Builder withUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder withCommitZone(String commitZone) {
            this.commitZone = commitZone;
            return this;
        }

        public Builder withTargetZone(String targetZone) {
            this.targetZone = targetZone;
            return this;
        }

        public Builder withStretchZone(String stretchZone) {
            this.stretchZone = stretchZone;
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
