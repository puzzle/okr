package ch.puzzle.okr.models.keyresult;

import ch.puzzle.okr.models.MessageKey;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.WriteableInterface;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "key_result_type")
public abstract class KeyResult implements WriteableInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_key_result")
    @SequenceGenerator(name = "sequence_key_result", allocationSize = 1)
    private Long id;

    @Version
    private int version;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne
    private Objective objective;

    @NotBlank(message = MessageKey.ATTRIBUTE_NOT_BLANK)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(min = 2, max = 250, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String title;

    @Size(max = 4096, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String description;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne
    private User owner;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne
    private User createdBy;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private LocalDateTime createdOn;

    private LocalDateTime modifiedOn;

    @Column(name = "key_result_type", insertable = false, updatable = false)
    private String keyResultType;

    private transient boolean writeable;

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
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

    private void setKeyResultType(String keyResultType) {
        this.keyResultType = keyResultType;
    }

    @Override
    public boolean isWriteable() {
        return writeable;
    }

    @Override
    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    @Override
    public String toString() {
        return "KeyResult{" + "id=" + id + ", version=" + version + ", objective=" + objective + ", title='" + title
                + '\'' + ", description='" + description + '\'' + ", owner=" + owner + ", createdBy=" + createdBy
                + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", keyResultType='" + keyResultType
                + ", writeable=" + writeable + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        KeyResult keyResult = (KeyResult) o;
        return Objects.equals(id, keyResult.id) && version == keyResult.version
                && Objects.equals(objective, keyResult.objective) && Objects.equals(title, keyResult.title)
                && Objects.equals(description, keyResult.description) && Objects.equals(owner, keyResult.owner)
                && Objects.equals(createdBy, keyResult.createdBy) && Objects.equals(createdOn, keyResult.createdOn)
                && Objects.equals(modifiedOn, keyResult.modifiedOn)
                && Objects.equals(keyResultType, keyResult.keyResultType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, objective, title, description, owner, createdBy, createdOn, modifiedOn,
                keyResultType);
    }

    protected KeyResult() {
    }

    protected KeyResult(Builder<?> builder) {
        id = builder.id;
        version = builder.version;
        setObjective(builder.objective);
        setTitle(builder.title);
        setDescription(builder.description);
        setOwner(builder.owner);
        setCreatedBy(builder.createdBy);
        setCreatedOn(builder.createdOn);
        setModifiedOn(builder.modifiedOn);
        setKeyResultType(builder.keyResultType);
    }

    @SuppressWarnings(value = "unchecked")
    public abstract static class Builder<T> {
        private Long id;
        private int version;
        private Objective objective;
        private String title;
        private String description;
        private User owner;
        private User createdBy;
        private LocalDateTime createdOn;
        private LocalDateTime modifiedOn;
        private final String keyResultType;

        protected Builder(String keyResultType) {
            this.keyResultType = keyResultType;
        }

        public T withId(Long id) {
            this.id = id;
            return (T) this;
        }

        public T withVersion(int version) {
            this.version = version;
            return (T) this;
        }

        public T withObjective(Objective objective) {
            this.objective = objective;
            return (T) this;
        }

        public T withTitle(String title) {
            this.title = title;
            return (T) this;
        }

        public T withDescription(String description) {
            this.description = description;
            return (T) this;
        }

        public T withOwner(User owner) {
            this.owner = owner;
            return (T) this;
        }

        public T withCreatedBy(User createdBy) {
            this.createdBy = createdBy;
            return (T) this;
        }

        public T withCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return (T) this;
        }

        public T withModifiedOn(LocalDateTime modifiedOn) {
            this.modifiedOn = modifiedOn;
            return (T) this;
        }

        public abstract KeyResult build();
    }
}
