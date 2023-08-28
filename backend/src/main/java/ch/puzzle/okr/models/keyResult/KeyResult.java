package ch.puzzle.okr.models.keyResult;

import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;

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
    private User createdBy;

    @NotNull
    private LocalDateTime createdOn;

    @NotNull
    private LocalDateTime modifiedOn;

    public KeyResult() {
    }

    private KeyResult(Builder builder) {
        id = builder.id;
        setObjective(builder.objective);
        setTitle(builder.title);
        setDescription(builder.description);
        setOwner(builder.owner);
        setExpectedEvolution(builder.expectedEvolution);
        setUnit(builder.unit);
        setBasisValue(builder.basisValue);
        setTargetValue(builder.targetValue);
        setCreatedBy(builder.createdBy);
        setModifiedOn(builder.modifiedOn);
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

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public String toString() {
        return "KeyResult{" + "id=" + id + ", objective=" + objective + ", title='" + title + '\'' + ", description='"
                + description + '\'' + ", owner=" + owner + ", createdBy=" + createdBy + ", createdOn=" + createdOn
                + ", modifiedOn=" + modifiedOn + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        KeyResult keyResult = (KeyResult) o;
        return Objects.equals(id, keyResult.id) && Objects.equals(objective, keyResult.objective)
                && Objects.equals(title, keyResult.title) && Objects.equals(description, keyResult.description)
                && Objects.equals(owner, keyResult.owner) && Objects.equals(createdBy, keyResult.createdBy)
                && Objects.equals(createdOn, keyResult.createdOn) && Objects.equals(modifiedOn, keyResult.modifiedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, objective, title, description, owner, createdBy, createdOn, modifiedOn);
    }

    public static final class Builder {
        private @NotNull Long id;
        private @NotNull Objective objective;
        private @NotBlank @Size(min = 2, max = 250) String title;
        private @Size(min = 2, max = 4096) String description;
        private @NotNull User owner;
        private @Size(min = 2, max = 250) ExpectedEvolution expectedEvolution;
        private @NotNull @NotBlank Unit unit;
        private Double basisValue;
        private @NotNull Double targetValue;
        private @NotNull User createdBy;
        private @NotNull LocalDateTime modifiedOn;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(@NotNull Long id) {
            this.id = id;
            return this;
        }

        public Builder withObjective(@NotNull Objective objective) {
            this.objective = objective;
            return this;
        }

        public Builder withTitle(@NotBlank @Size(min = 2, max = 250) String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(@Size(min = 2, max = 4096) String description) {
            this.description = description;
            return this;
        }

        public Builder withOwner(@NotNull User owner) {
            this.owner = owner;
            return this;
        }

        public Builder withExpectedEvolution(@Size(min = 2, max = 250) ExpectedEvolution expectedEvolution) {
            this.expectedEvolution = expectedEvolution;
            return this;
        }

        public Builder withUnit(@NotNull @NotBlank Unit unit) {
            this.unit = unit;
            return this;
        }

        public Builder withBasisValue(Double basisValue) {
            this.basisValue = basisValue;
            return this;
        }

        public Builder withTargetValue(@NotNull Double targetValue) {
            this.targetValue = targetValue;
            return this;
        }

        public Builder withCreatedBy(@NotNull User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder withModifiedOn(@NotNull LocalDateTime createdOn) {
            this.modifiedOn = createdOn;
            return this;
        }

        public KeyResult build() {
            return new KeyResult(this);
        }
    }
}
