package ch.puzzle.okr.models.keyResult;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("Metric")
public class KeyResultMetric extends KeyResult {
    @NotNull
    private Double baseline;

    @NotNull
    private Double stretchGoal;

    @NotNull
    private String unit;

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

    public KeyResultMetric() {
    }

    public KeyResultMetric(Builder builder) {
        super(builder);
        setBaseline(builder.baseline);
        setStretchGoal(builder.stretchGoal);
        setUnit(builder.unit);
    }

    public static class Builder extends KeyResult.Builder {
        private @NotNull Double baseline;
        private @NotNull Double stretchGoal;
        private @NotNull String unit;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withBaseline(@NotNull Double baseline) {
            this.baseline = baseline;
            return this;
        }

        public Builder withStretchGoal(@NotNull Double stretchGoal) {
            this.stretchGoal = stretchGoal;
            return this;
        }

        public Builder withUnit(@NotNull String unit) {
            this.unit = unit;
            return this;
        }

        @Override
        public KeyResult build() {
            return new KeyResultMetric(this);
        }
    }

}
