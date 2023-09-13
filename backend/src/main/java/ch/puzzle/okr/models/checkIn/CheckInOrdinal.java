package ch.puzzle.okr.models.checkIn;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("ordinal")
public class CheckInOrdinal extends CheckIn {
    @NotNull(message = "Value must not be null")
    private String zone;

    /* Getter and Setter */
    public String getValue() {
        return zone;
    }

    public void setValue(String value) {
        this.zone = value;
    }

    /* Constructor */
    public CheckInOrdinal() {
        super();
    }

    public CheckInOrdinal(Builder builder) {
        super(builder);
        setValue(builder.value);
    }

    /* Builder */
    public static final class Builder extends CheckIn.Builder {
        private String value;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withValue(String value) {
            this.value = value;
            return this;
        }

        @Override
        public CheckIn build() {
            return new CheckInOrdinal(this);
        }
    }
}
