package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
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
    @Column(name = "`value`")
    private Double value;

    @NotNull
    @NotBlank
    private String changeInfo;

    @Size(max = 4096)
    private String initiatives;

    @NotNull
    @ManyToOne
    private User createdBy;

    @NotNull
    private Instant measureDate;

    @NotNull
    private LocalDateTime createdOn;

    public Measure() {
    }

    private Measure(Builder builder) {
        id = builder.id;
        setKeyResult(builder.keyResult);
        setValue(builder.value);
        setChangeInfo(builder.changeInfo);
        setInitiatives(builder.initiatives);
        setCreatedBy(builder.createdBy);
        setMeasureDate(builder.measureDate);
        setCreatedOn(builder.createdOn);
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

    public Instant getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(Instant measureDate) {
        this.measureDate = measureDate;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "Measure{" + "id=" + id + ", keyResult=" + keyResult + ", value=" + value + ", changeInfo='" + changeInfo
                + '\'' + ", initiatives='" + initiatives + '\'' + ", createdBy=" + createdBy + ", measureDate="
                + measureDate + ", createdOn=" + createdOn + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Measure measure = (Measure) o;
        return Objects.equals(id, measure.id) && Objects.equals(keyResult, measure.keyResult)
                && Objects.equals(value, measure.value) && Objects.equals(changeInfo, measure.changeInfo)
                && Objects.equals(initiatives, measure.initiatives) && Objects.equals(createdBy, measure.createdBy)
                && Objects.equals(measureDate, measure.measureDate) && Objects.equals(createdOn, measure.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keyResult, value, changeInfo, initiatives, createdBy, measureDate, createdOn);
    }

    public static final class Builder {
        private @NotNull Long id;
        private @NotNull KeyResult keyResult;
        private @NotNull Double value;
        private @NotNull @NotBlank String changeInfo;
        private @Size(max = 4096) String initiatives;
        private @NotNull User createdBy;
        private @NotNull Instant measureDate;
        private @NotNull LocalDateTime createdOn;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(@NotNull Long id) {
            this.id = id;
            return this;
        }

        public Builder withKeyResult(@NotNull KeyResult keyResult) {
            this.keyResult = keyResult;
            return this;
        }

        public Builder withValue(@NotNull Double value) {
            this.value = value;
            return this;
        }

        public Builder withChangeInfo(@NotNull @NotBlank String changeInfo) {
            this.changeInfo = changeInfo;
            return this;
        }

        public Builder withInitiatives(@Size(max = 4096) String initiatives) {
            this.initiatives = initiatives;
            return this;
        }

        public Builder withCreatedBy(@NotNull User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder withMeasureDate(@NotNull Instant measureDate) {
            this.measureDate = measureDate;
            return this;
        }

        public Builder withCreatedOn(@NotNull LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Measure build() {
            return new Measure(this);
        }
    }
}
