package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class KeyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_key_result")
    @NotNull
    private Long id;

    @NotNull
    @ManyToOne
    private Objective objective;

    @NotBlank
    @Size(min = 2, max = 250)
    private String title;

    @Size(max = 4096)
    private String description;

    @NotNull
    @ManyToOne
    private User owner;

    @NotNull
    @ManyToOne
    private Quarter quarter;

    @Size(min = 2, max = 250)
    private ExpectedEvolution expectedEvolution;

    @NotNull
    @NotBlank
    private Unit unit;

    @NotNull
    private Integer basisValue;

    @NotNull
    private Integer targetValue;

    @NotNull
    @ManyToOne
    private User createdBy;

    @NotNull
    private LocalDateTime createdOn;

    public KeyResult() {
    }

    private KeyResult(Builder builder) {
        setObjective(builder.objective);
        setTitle(builder.title);
        setDescription(builder.description);
        setOwner(builder.owner);
        setQuarter(builder.quarter);
        setExpectedEvolution(builder.expectedEvolution);
        setUnit(builder.unit);
        setBasisValue(builder.basisValue);
        setTargetValue(builder.targetValue);
        setCreatedBy(builder.createdBy);
        setCreatedOn(builder.createdOn);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyResult keyResult = (KeyResult) o;
        return Objects.equals(id, keyResult.id) && Objects.equals(objective, keyResult.objective) && Objects.equals(title, keyResult.title) && Objects.equals(description, keyResult.description) && Objects.equals(owner, keyResult.owner) && Objects.equals(quarter, keyResult.quarter) && Objects.equals(expectedEvolution, keyResult.expectedEvolution) && Objects.equals(unit, keyResult.unit) && Objects.equals(basisValue, keyResult.basisValue) && Objects.equals(targetValue, keyResult.targetValue) && Objects.equals(createdBy, keyResult.createdBy) && Objects.equals(createdOn, keyResult.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, objective, title, description, owner, quarter, expectedEvolution, unit, basisValue, targetValue, createdBy, createdOn);
    }

    @Override
    public String toString() {
        return "KeyResult{" +
                "id=" + id +
                ", objective=" + objective +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", quarter=" + quarter +
                ", expectedEvolution=" + expectedEvolution +
                ", unit='" + unit + '\'' +
                ", basisValue=" + basisValue +
                ", targetValue=" + targetValue +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                '}';
    }

    public Long getId() {
        return id;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }

    public ExpectedEvolution getExpectedEvolution() {
        return expectedEvolution;
    }

    public void setExpectedEvolution(ExpectedEvolution expectedEvolution) {
        this.expectedEvolution = expectedEvolution;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Integer getBasisValue() {
        return basisValue;
    }

    public void setBasisValue(Integer basisValue) {
        this.basisValue = basisValue;
    }

    public Integer getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Integer targetValue) {
        this.targetValue = targetValue;
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
        private @NotNull Objective objective;
        private @NotBlank @Size(min = 2, max = 250) String title;
        private @Size(max = 4096) String description;
        private @NotNull User owner;
        private @NotNull Quarter quarter;
        private @Size(min = 2, max = 250) ExpectedEvolution expectedEvolution;
        private @NotNull @NotBlank Unit unit;
        private @NotNull Integer basisValue;
        private @NotNull Integer targetValue;
        private @NotNull User createdBy;
        private @NotNull LocalDateTime createdOn;

        public Builder() {
        }

        public Builder id(@NotNull Long val) {
            id = val;
            return this;
        }

        public Builder objective(@NotNull Objective val) {
            objective = val;
            return this;
        }

        public Builder title(@NotBlank @Size(min = 2, max = 250) String val) {
            title = val;
            return this;
        }

        public Builder description(@Size(max = 4096) String val) {
            description = val;
            return this;
        }

        public Builder owner(@NotNull User val) {
            owner = val;
            return this;
        }

        public Builder quarter(@NotNull Quarter val) {
            quarter = val;
            return this;
        }

        public Builder expectedEvolution(@Size(min = 2, max = 250) ExpectedEvolution val) {
            expectedEvolution = val;
            return this;
        }

        public Builder unit(@NotNull @NotBlank Unit val) {
            unit = val;
            return this;
        }

        public Builder basisValue(@NotNull Integer val) {
            basisValue = val;
            return this;
        }

        public Builder targetValue(@NotNull Integer val) {
            targetValue = val;
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

        public KeyResult build() {
            return new KeyResult(this);
        }
    }
}
