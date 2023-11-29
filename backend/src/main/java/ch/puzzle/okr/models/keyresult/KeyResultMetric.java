package ch.puzzle.okr.models.keyresult;

import ch.puzzle.okr.models.MessageKey;
import ch.puzzle.okr.models.Unit;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;

@Entity
@DiscriminatorValue(KEY_RESULT_TYPE_METRIC)
public class KeyResultMetric extends KeyResult {
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private Double baseline;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private Double stretchGoal;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Enumerated(EnumType.STRING)
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
        if (o instanceof KeyResultMetric) {
            return super.equals(o) && Objects.equals(baseline, ((KeyResultMetric) o).baseline)
                    && Objects.equals(stretchGoal, ((KeyResultMetric) o).stretchGoal)
                    && Objects.equals(unit, ((KeyResultMetric) o).unit);
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
