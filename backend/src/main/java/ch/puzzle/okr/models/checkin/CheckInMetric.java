package ch.puzzle.okr.models.checkin;

import ch.puzzle.okr.models.MessageKey;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;

@Entity
@DiscriminatorValue(KEY_RESULT_TYPE_METRIC)
public class CheckInMetric extends CheckIn {
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
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

    private CheckInMetric(Builder builder) {
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
    public static final class Builder extends CheckIn.Builder<Builder> {
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
