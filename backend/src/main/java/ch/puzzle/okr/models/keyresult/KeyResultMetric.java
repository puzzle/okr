package ch.puzzle.okr.models.keyresult;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;

import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.MessageKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@DiscriminatorValue(KEY_RESULT_TYPE_METRIC)
public class KeyResultMetric extends KeyResult {
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private Double baseline;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private Double stretchGoal;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne
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
                   && Objects.equals(unit, keyResultMetric.unit);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), baseline, stretchGoal, unit);
    }

    @Override
    public String toString() {
        return super.toString() + "KeyResultMetric{" + "baseline=" + baseline + ", stretchGoal=" + stretchGoal
               + ", unit='" + unit + '\'' + '}';
    }

    private KeyResultMetric(Builder builder) {
        super(builder);
        setBaseline(builder.baseline);
        setStretchGoal(builder.stretchGoal);
        setUnit(builder.unit);
    }

    public static class Builder extends KeyResult.Builder<Builder> {
        private Double baseline;
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

        @Override
        public KeyResult build() {
            return new KeyResultMetric(this);
        }
    }

}
