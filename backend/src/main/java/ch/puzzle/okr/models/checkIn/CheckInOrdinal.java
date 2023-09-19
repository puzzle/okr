package ch.puzzle.okr.models.checkIn;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("ordinal")
public class CheckInOrdinal extends CheckIn {
    @NotNull(message = "Value must not be null")
    @Enumerated(EnumType.STRING)
    private Zone zone;

    /* Getter and Setter */
    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    /* Constructor */
    public CheckInOrdinal() {
        super();
    }

    public CheckInOrdinal(Builder builder) {
        super(builder);
        setZone(builder.zone);
    }

    /* Builder */
    public static final class Builder extends CheckIn.Builder {
        private Zone zone;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withZone(Zone zone) {
            this.zone = zone;
            return this;
        }

        @Override
        public CheckIn build() {
            return new CheckInOrdinal(this);
        }
    }
}
