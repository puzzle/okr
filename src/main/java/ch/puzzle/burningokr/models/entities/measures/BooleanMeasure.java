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
@Table(name = "boolean_result")
public class BooleanMeasure extends Measure {
    @Column(name = "value")
    private Boolean value;

    private BooleanMeasure(Builder builder) {
        setValue(builder.value);
        setId(builder.id);
        setKeyResult(builder.keyResult);
        setChangeInfo(builder.changeInfo);
        setCreatedBy(builder.createdBy);
        setCreatedOn(builder.createdOn);
    }

    public BooleanMeasure() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BooleanMeasure that = (BooleanMeasure) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
        return "BooleanMeasure{" +
                "value=" + value +
                '}';
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public static final class Builder {
        private Boolean value;
        private Long id;
        private @NotNull KeyResult keyResult;
        private @NotNull @NotBlank String changeInfo;
        private @NotNull User createdBy;
        private @NotNull LocalDateTime createdOn;

        public Builder() {
        }

        public Builder value(Boolean val) {
            value = val;
            return this;
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

        public BooleanMeasure build() {
            return new BooleanMeasure(this);
        }
    }
}