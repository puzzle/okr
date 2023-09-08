package ch.puzzle.okr.models.checkin;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("metric")
public class CheckInMetric extends CheckIn {
    @NotNull(message = "Value must not be null")
    private Double value;

    /* Getter and Setter */
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    /* Constructor */
    public CheckInMetric() {
        super();
    }

    public CheckInMetric(Builder builder) {
        super(builder);
        setValue(builder.value);
    }

    /* Builder */
    public static final class Builder extends CheckIn.Builder {
        private Double value;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withValue(Double value) {
            this.value = value;
            return this;
        }

        @Override
        public CheckIn build() {
            return new CheckInMetric(this);
        }
    }
}
