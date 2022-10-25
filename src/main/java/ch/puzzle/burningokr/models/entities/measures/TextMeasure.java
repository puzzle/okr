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
@Table(name = "text_result")
public class TextMeasure extends Measure {
    @Column(name = "measure_value")
    private String value;

    @Column(name = "initative")
    private String initative;

    public String getInitative() {
        return initative;
    }

    public void setInitative(String initative) {
        this.initative = initative;
    }

    private TextMeasure(Builder builder) {
        setId(builder.id);
        setKeyResult(builder.keyResult);
        setChangeInfo(builder.changeInfo);
        setCreatedBy(builder.createdBy);
        setCreatedOn(builder.createdOn);
        setValue(builder.value);
        setInitative(builder.initative);
    }

    public TextMeasure() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TextMeasure that = (TextMeasure) o;
        return Objects.equals(value, that.value) && Objects.equals(initative, that.initative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, initative);
    }

    @Override
    public String toString() {
        return "TextMeasure{" +
                "value='" + value + '\'' +
                ", initative='" + initative + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static final class Builder {
        private Long id;
        private @NotNull KeyResult keyResult;
        private @NotNull @NotBlank String changeInfo;
        private @NotNull User createdBy;
        private @NotNull LocalDateTime createdOn;
        private String value;
        private String initative;

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

        public Builder value(String val) {
            value = val;
            return this;
        }

        public Builder initative(String val) {
            initative = val;
            return this;
        }

        public TextMeasure build() {
            return new TextMeasure(this);
        }
    }
}