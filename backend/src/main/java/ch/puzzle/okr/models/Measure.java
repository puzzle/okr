package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Measure {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_measure")
    @NotNull
    private Long id;

    @NotNull
    @ManyToOne
    private KeyResult keyResult;

    @NotNull
    private Integer value;

    @NotNull
    @NotBlank
    private String changeInfo;

    @Size(max = 4096)
    private String initiatives;

    @NotNull
    @ManyToOne
    private User createdBy;

    @NotNull
    private LocalDateTime createdOn;

    public Measure() {
    }

    private Measure(Builder builder) {
        setKeyResult(builder.keyResult);
        setValue(builder.value);
        setChangeInfo(builder.changeInfo);
        setInitiatives(builder.initiatives);
        setCreatedBy(builder.createdBy);
        setCreatedOn(builder.createdOn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measure measure = (Measure) o;
        return Objects.equals(id, measure.id) && Objects.equals(keyResult, measure.keyResult) && Objects.equals(value, measure.value) && Objects.equals(changeInfo, measure.changeInfo) && Objects.equals(initiatives, measure.initiatives) && Objects.equals(createdBy, measure.createdBy) && Objects.equals(createdOn, measure.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keyResult, value, changeInfo, initiatives, createdBy, createdOn);
    }

    @Override
    public String toString() {
        return "Measure{" +
                "id=" + id +
                ", keyResult=" + keyResult +
                ", value=" + value +
                ", changeInfo='" + changeInfo + '\'' +
                ", initiatives='" + initiatives + '\'' +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                '}';
    }

    public Long getId() {
        return id;
    }

    public KeyResult getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(KeyResult keyResult) {
        this.keyResult = keyResult;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getChangeInfo() {
        return changeInfo;
    }

    public void setChangeInfo(String changeInfo) {
        this.changeInfo = changeInfo;
    }

    public String getInitiatives() {
        return initiatives;
    }

    public void setInitiatives(String initiatives) {
        this.initiatives = initiatives;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }


    public static final class Builder {
        private @NotNull Long id;
        private KeyResult keyResult;
        private @NotNull @NotBlank Double value;
        private @NotNull @NotBlank String changeInfo;
        private String initiatives;
        private @NotNull User createdBy;
        private @NotNull LocalDateTime createdOn;

        public Builder() {
        }

        public Builder id(@NotNull Long val) {
            id = val;
            return this;
        }

        public Builder keyResult(KeyResult val) {
            keyResult = val;
            return this;
        }

        public Builder value(@NotNull @NotBlank Double val) {
            value = val;
            return this;
        }

        public Builder changeInfo(@NotNull @NotBlank String val) {
            changeInfo = val;
            return this;
        }

        public Builder initiatives(String val) {
            initiatives = val;
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

        public Measure build() {
            return new Measure(this);
        }
    }
}
