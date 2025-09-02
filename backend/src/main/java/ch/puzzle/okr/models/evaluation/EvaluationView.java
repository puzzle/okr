package ch.puzzle.okr.models.evaluation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;

import java.time.Instant;
import java.util.Objects;

@Entity
@Immutable
public class EvaluationView {

    @Id
    private Long rowId;

    private Long keyResultId;
    private Long objectiveId;
    private Long teamId;
    private Long quarterId;

    private String objectiveState;
    private String keyResultType;
    private Double baseline;
    private Double commitValue;
    private Double targetValue;
    private Double stretchGoal;

    private Double valueMetric;
    private String zone;
    private Instant latestCheckInDate;

    public EvaluationView() {
    }

    private EvaluationView(Builder builder) {
        this.rowId = builder.rowId;
        this.keyResultId = builder.keyResultId;
        this.objectiveId = builder.objectiveId;
        this.teamId = builder.teamId;
        this.quarterId = builder.quarterId;
        this.objectiveState = builder.objectiveState;
        this.keyResultType = builder.keyResultType;
        this.baseline = builder.baseline;
        this.commitValue = builder.commitValue;
        this.targetValue = builder.targetValue;
        this.stretchGoal = builder.stretchGoal;
        this.valueMetric = builder.valueMetric;
        this.zone = builder.zone;
        this.latestCheckInDate = builder.latestCheckInDate;
    }

    public Long getRowId() {
        return rowId;
    }

    public Long getKeyResultId() {
        return keyResultId;
    }

    public Long getObjectiveId() {
        return objectiveId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public String getObjectiveState() {
        return objectiveState;
    }

    public String getKeyResultType() {
        return keyResultType;
    }

    public Double getBaseline() {
        return baseline;
    }

    public Double getCommitValue() {
        return commitValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public Double getStretchGoal() {
        return stretchGoal;
    }

    public Double getValueMetric() {
        return valueMetric;
    }

    public String getZone() {
        return zone;
    }

    public Instant getLatestCheckInDate() {
        return latestCheckInDate;
    }

    public static final class Builder {
        private Long rowId;
        private Long keyResultId;
        private Long objectiveId;
        private Long teamId;
        private Long quarterId;

        private String objectiveState;
        private String keyResultType;
        private Double baseline;
        private Double commitValue;
        private Double targetValue;
        private Double stretchGoal;

        private Double valueMetric;
        private String zone;
        private Instant latestCheckInDate;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withRowId(Long val) {
            this.rowId = val;
            return this;
        }

        public Builder withKeyResultId(Long val) {
            this.keyResultId = val;
            return this;
        }

        public Builder withObjectiveId(Long val) {
            this.objectiveId = val;
            return this;
        }

        public Builder withTeamId(Long val) {
            this.teamId = val;
            return this;
        }

        public Builder withQuarterId(Long val) {
            this.quarterId = val;
            return this;
        }

        public Builder withObjectiveState(String val) {
            this.objectiveState = val;
            return this;
        }

        public Builder withKeyResultType(String val) {
            this.keyResultType = val;
            return this;
        }

        public Builder withBaseline(Double val) {
            this.baseline = val;
            return this;
        }

        public Builder withCommitValue(Double val) {
            this.commitValue = val;
            return this;
        }

        public Builder withTargetValue(Double val) {
            this.targetValue = val;
            return this;
        }

        public Builder withStretchGoal(Double val) {
            this.stretchGoal = val;
            return this;
        }

        public Builder withValueMetric(Double val) {
            this.valueMetric = val;
            return this;
        }

        public Builder withZone(String val) {
            this.zone = val;
            return this;
        }

        public Builder withLatestCheckInDate(Instant val) {
            this.latestCheckInDate = val;
            return this;
        }

        public EvaluationView build() {
            return new EvaluationView(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EvaluationView that)) {
            return false;
        }
        return Objects.equals(getRowId(), that.getRowId())
                && Objects.equals(getKeyResultId(), that.getKeyResultId())
                && Objects.equals(getObjectiveId(), that.getObjectiveId())
                && Objects.equals(getTeamId(), that.getTeamId())
                && Objects.equals(getQuarterId(), that.getQuarterId())
                && Objects.equals(getObjectiveState(), that.getObjectiveState())
                && Objects.equals(getKeyResultType(), that.getKeyResultType())
                && Objects.equals(getBaseline(), that.getBaseline())
                && Objects.equals(getCommitValue(), that.getCommitValue())
                && Objects.equals(getTargetValue(), that.getTargetValue())
                && Objects.equals(getStretchGoal(), that.getStretchGoal())
                && Objects.equals(getValueMetric(), that.getValueMetric())
                && Objects.equals(getZone(), that.getZone())
                && Objects.equals(getLatestCheckInDate(), that.getLatestCheckInDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getRowId(),
                getKeyResultId(),
                getObjectiveId(),
                getTeamId(),
                getQuarterId(),
                getObjectiveState(),
                getKeyResultType(),
                getBaseline(),
                getCommitValue(),
                getTargetValue(),
                getStretchGoal(),
                getValueMetric(),
                getZone(),
                getLatestCheckInDate()
        );
    }
}
