package ch.puzzle.okr.models.keyresult;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;

import ch.puzzle.okr.models.MessageKey;
import ch.puzzle.okr.models.Unit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@DiscriminatorValue(KEY_RESULT_TYPE_METRIC)
public class KeyResultMetric extends KeyResult {
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private Double baseline;

    private Double commitValue;

    private Double targetValue;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private Double stretchGoal;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne(cascade = CascadeType.MERGE)
    private Unit unit;

    public Double getBaseline() {
        return baseline;
    }

    public void setBaseline(Double baseline) {
        this.baseline = baseline;
    }

    public Double getStretchGoal() {
        return stretchGoal;
    }

    public Double getCommitValue() {
        return commitValue;
    }

    public void setCommitValue(Double commitValue) {
        this.commitValue = commitValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public void setStretchGoal(Double stretchGoal) {
        this.stretchGoal = stretchGoal;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public KeyResultMetric() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof KeyResultMetric keyResultMetric) {
            return super.equals(o) && Objects.equals(baseline, keyResultMetric.baseline)
                   && Objects.equals(stretchGoal, keyResultMetric.stretchGoal)
                   && Objects.equals(commitValue, keyResultMetric.commitValue)
                   && Objects.equals(targetValue, keyResultMetric.targetValue)
                   && Objects.equals(unit, keyResultMetric.unit);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), baseline, commitValue, targetValue, stretchGoal, unit);
    }

    @Override
    public String toString() {
        return "KeyResultMetric{" + "baseline=" + baseline + ", commitValue=" + commitValue + ", targetValue="
               + targetValue + ", stretchGoal=" + stretchGoal + ", unit=" + unit + '}';
    }

    private KeyResultMetric(Builder builder) {
        super(builder);
        setBaseline(builder.baseline);
        setCommitValue(builder.commitValue);
        setTargetValue(builder.targetValue);
        setStretchGoal(builder.stretchGoal);
        setUnit(builder.unit);
    }

    public static class Builder extends KeyResult.Builder<Builder> {
        private Double baseline;
        private Double commitValue;
        private Double targetValue;
        private Double stretchGoal;
        private Unit unit;

        private Builder() {
            super(KEY_RESULT_TYPE_METRIC);
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withBaseline(Double baseline) {
            this.baseline = baseline;
            return this;
        }

        public Builder withStretchGoal(Double stretchGoal) {
            this.stretchGoal = stretchGoal;
            return this;
        }

        public Builder withUnit(Unit unit) {
            this.unit = unit;
            return this;
        }

        public Builder withCommitValue(Double commitValue) {
            this.commitValue = commitValue;
            return this;
        }

        public Builder withTargetValue(Double targetValue) {
            this.targetValue = targetValue;
            return this;
        }

        @Override
        public KeyResult build() {
            return new KeyResultMetric(this);
        }
    }

}
