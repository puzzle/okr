package ch.puzzle.burningokr.models.entities;

import ch.puzzle.burningokr.models.helper.UNIT;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "key_result")
public class KeyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "objective_id")
    private Objective objective;

    @Size(min = 3, max = 20)
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "quarter_id")
    private Quarter quarter;

    @Column(name = "expected_evolution")
    private Integer expectedEvolution;

    @NotNull
    @Enumerated
    @Column(name = "unit")
    private UNIT unit;

    @Column(name = "basis_value")
    private Integer basisValue;

    @Column(name = "target_value")
    private Integer targetValue;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @NotNull
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    private KeyResult(Builder builder) {
        setId(builder.id);
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

    public KeyResult() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyResult keyResult = (KeyResult) o;
        return Objects.equals(id, keyResult.id) && Objects.equals(objective, keyResult.objective) && Objects.equals(title, keyResult.title) && Objects.equals(description, keyResult.description) && Objects.equals(owner, keyResult.owner) && Objects.equals(quarter, keyResult.quarter) && Objects.equals(expectedEvolution, keyResult.expectedEvolution) && unit == keyResult.unit && Objects.equals(basisValue, keyResult.basisValue) && Objects.equals(targetValue, keyResult.targetValue) && Objects.equals(createdBy, keyResult.createdBy) && Objects.equals(createdOn, keyResult.createdOn);
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
                ", unit=" + unit +
                ", basisValue=" + basisValue +
                ", targetValue=" + targetValue +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                '}';
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Integer targetValue) {
        this.targetValue = targetValue;
    }

    public Integer getBasisValue() {
        return basisValue;
    }

    public void setBasisValue(Integer basisValue) {
        this.basisValue = basisValue;
    }

    public UNIT getUnit() {
        return unit;
    }

    public void setUnit(UNIT unit) {
        this.unit = unit;
    }

    public Integer getExpectedEvolution() {
        return expectedEvolution;
    }

    public void setExpectedEvolution(Integer expectedEvolution) {
        this.expectedEvolution = expectedEvolution;
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static final class Builder {
        private Long id;
        private Objective objective;
        private String title;
        private String description;
        private User owner;
        private Quarter quarter;
        private Integer expectedEvolution;
        private UNIT unit;
        private Integer basisValue;
        private Integer targetValue;
        private User createdBy;
        private LocalDateTime createdOn;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder objective(Objective val) {
            objective = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder owner(User val) {
            owner = val;
            return this;
        }

        public Builder quarter(Quarter val) {
            quarter = val;
            return this;
        }

        public Builder expectedEvolution(Integer val) {
            expectedEvolution = val;
            return this;
        }

        public Builder unit(UNIT val) {
            unit = val;
            return this;
        }

        public Builder basisValue(Integer val) {
            basisValue = val;
            return this;
        }

        public Builder targetValue(Integer val) {
            targetValue = val;
            return this;
        }

        public Builder createdBy(User val) {
            createdBy = val;
            return this;
        }

        public Builder createdOn(LocalDateTime val) {
            createdOn = val;
            return this;
        }

        public KeyResult build() {
            return new KeyResult(this);
        }
    }
}