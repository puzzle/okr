package ch.puzzle.okr.models.keyresult;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "key_result_type")
public abstract class KeyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_key_result")
    private Long id;

    @NotNull(message = "Objective must not be null")
    @ManyToOne
    private Objective objective;

    @NotBlank(message = "Title can not be blank")
    @Size(min = 2, max = 250, message = "Attribute title must have a length between 2 and 250 characters when saving objective")
    private String title;

    @Size(max = 4096, message = "Attribute description has a max length of 4096 characters")
    private String description;

    @NotNull(message = "Owner must not be null")
    @ManyToOne
    private User owner;

    @NotNull(message = "CreatedBy must not be null")
    @ManyToOne
    private User createdBy;

    @NotNull(message = "CreatedOn must not be null")
    private LocalDateTime createdOn;

    private LocalDateTime modifiedOn;

    @Column(name = "key_result_type", insertable = false, updatable = false)
    private String keyResultType;

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

    public String getKeyResultType() {
        return keyResultType;
    }

    public void setKeyResultType(String keyResultType) {
        this.keyResultType = keyResultType;
    }

    @Override
    public String toString() {
        return "KeyResult{" + "id=" + id + ", objective=" + objective + ", title='" + title + '\'' + ", description='"
                + description + '\'' + ", owner=" + owner + ", createdBy=" + createdBy + ", createdOn=" + createdOn
                + ", modifiedOn=" + modifiedOn + ", keyResultType='" + keyResultType + '\'' + '}';
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
                && Objects.equals(createdOn, keyResult.createdOn) && Objects.equals(modifiedOn, keyResult.modifiedOn)
                && Objects.equals(keyResultType, keyResult.keyResultType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, objective, title, description, owner, createdBy, createdOn, modifiedOn, keyResultType);
    }

    public KeyResult() {
    }

    public KeyResult(Builder builder) {
        id = builder.id;
        setObjective(builder.objective);
        setTitle(builder.title);
        setDescription(builder.description);
        setOwner(builder.owner);
        setCreatedBy(builder.createdBy);
        setCreatedOn(builder.createdOn);
        setModifiedOn(builder.modifiedOn);
        setKeyResultType(builder.keyResultType);
    }

    public abstract static class Builder {
        private Long id;
        private Objective objective;
        private String title;
        private String description;
        private User owner;
        private User createdBy;
        private LocalDateTime createdOn;
        private LocalDateTime modifiedOn;
        private String keyResultType;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withObjective(Objective objective) {
            this.objective = objective;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withOwner(User owner) {
            this.owner = owner;
            return this;
        }

        public Builder withCreatedBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder withCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder withModifiedOn(LocalDateTime modifiedOn) {
            this.modifiedOn = modifiedOn;
            return this;
        }

        public Builder withKeyResultType(String keyResultType) {
            this.keyResultType = keyResultType;
            return this;
        }

        public abstract KeyResult build();
    }
}
