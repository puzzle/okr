package ch.puzzle.okr.models.checkin;

import ch.puzzle.okr.models.ErrorMsg;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;

@Entity
@DiscriminatorValue(KEY_RESULT_TYPE_ORDINAL)
public class CheckInOrdinal extends CheckIn {
    @NotNull(message = ErrorMsg.ATTRIBUTE_NOT_NULL)
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

    private CheckInOrdinal(Builder builder) {
        super(builder);
        setZone(builder.zone);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CheckInOrdinal) {
            return super.equals(o) && Objects.equals(zone, ((CheckInOrdinal) o).zone);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), zone);
    }

    @Override
    public String toString() {
        return super.toString() + "CheckInOrdinal{" + "zone=" + zone + '}';
    }

    /* Builder */
    public static final class Builder extends CheckIn.Builder<Builder> {
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
