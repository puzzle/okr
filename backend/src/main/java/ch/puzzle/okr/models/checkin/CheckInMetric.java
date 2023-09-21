package ch.puzzle.okr.models.checkin;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@DiscriminatorValue("metric")
public class CheckInMetric extends CheckIn {
    @NotNull(message = "Value must not be null")
    private Double valueMetric;

    /* Getter and Setter */
    public Double getValue() {
        return valueMetric;
    }

    public void setValue(Double value) {
        this.valueMetric = value;
    }

    /* Constructor */
    public CheckInMetric() {
        super();
    }

    public CheckInMetric(Builder builder) {
        super(builder);
        setValue(builder.value);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CheckInMetric) {
            return super.equals(o) && Objects.equals(valueMetric, ((CheckInMetric) o).valueMetric);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), valueMetric);
    }

    @Override
    public String toString() {
        return super.toString() + "CheckInMetric{" + "valueMetric=" + valueMetric + '}';
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
