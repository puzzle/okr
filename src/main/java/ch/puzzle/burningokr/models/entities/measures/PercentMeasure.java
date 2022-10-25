package ch.puzzle.burningokr.models.entities.measures;

import ch.puzzle.burningokr.models.entities.KeyResult;
import ch.puzzle.burningokr.models.entities.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class PercentMeasure extends Measure {
    @NotNull
    @Column(name = "value")
    private Double value;

    @NotNull
    @Column(name = "initiative")
    private Double initiative;

    private PercentMeasure(Builder builder) {
        setId(builder.id);
        setKeyResult(builder.keyResult);
        setChangeInfo(builder.changeInfo);
        setCreatedBy(builder.createdBy);
        setCreatedOn(builder.createdOn);
        setValue(builder.value);
        setInitiative(builder.initiative);
    }

    public PercentMeasure() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PercentMeasure that = (PercentMeasure) o;
        return Objects.equals(value, that.value) && Objects.equals(initiative, that.initiative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, initiative);
    }

    @Override
    public String toString() {
        return "PercentMeasure{" +
                "value=" + value +
                ", initiative=" + initiative +
                '}';
    }

    public Double getInitiative() {
        return initiative;
    }

    public void setInitiative(Double initiative) {
        this.initiative = initiative;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public static final class Builder {
        private Long id;
        private @NotNull KeyResult keyResult;
        private @NotNull @NotBlank String changeInfo;
        private @NotNull User createdBy;
        private @NotNull LocalDateTime createdOn;
        private Double value;
        private Double initiative;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder keyResult(@NotNull KeyResult val) {
            keyResult = val;
            return this;
        }

        public Builder changeInfo(@NotNull @NotBlank String val) {
            changeInfo = val;
            return this;
        }

        public Builder createdBy(@NotNull User val) {
            createdBy = val;
            return this;
        }

        public Builder createdOn(@NotNull LocalDateTime val) {
            createdOn = val;
            return this;
        }

        public Builder value(Double val) {
            value = val;
            return this;
        }

        public Builder initiative(Double val) {
            initiative = val;
            return this;
        }

        public PercentMeasure build() {
            return new PercentMeasure(this);
        }
    }
}