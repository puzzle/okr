package ch.puzzle.okr.models;

import ch.puzzle.okr.models.keyresult.KeyResult;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Action extends Deletable implements WriteableInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_action")
    @SequenceGenerator(name = "sequence_action", allocationSize = 1)
    private Long id;

    @Version
    private int version;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(max = 4096, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String actionPoint;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private int priority;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private boolean checked;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne
    private KeyResult keyResult;

    @Transient
    private boolean writeable;

    public Action() {
    }

    private Action(Builder builder) {
        id = builder.id;
        version = builder.version;
        actionPoint = builder.action;
        priority = builder.priority;
        checked = builder.checked;
        keyResult = builder.keyResult;
    }

    public Long getId() {
        return id;
    }

    public void resetId() {
        id = null;
    }

    public String getActionPoint() {
        return actionPoint;
    }

    public void setActionPoint(String action) {
        this.actionPoint = action;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public KeyResult getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(KeyResult keyResult) {
        this.keyResult = keyResult;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
        return "Action{" + "id=" + id + ", version=" + version + ", action='" + actionPoint + '\'' + ", priority="
               + priority + ", checked=" + checked + ", keyResult=" + keyResult + ", writeable=" + writeable + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Action action1 = (Action) o;
        return version == action1.version && priority == action1.priority && checked == action1.checked
               && writeable == action1.writeable && Objects.equals(id, action1.id)
               && Objects.equals(actionPoint, action1.actionPoint) && Objects.equals(keyResult, action1.keyResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, actionPoint, priority, checked, keyResult, writeable);
    }

    public static final class Builder {
        private Long id;
        private int version;
        private String action;
        private int priority;
        private boolean checked;
        private KeyResult keyResult;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder withAction(String action) {
            this.action = action;
            return this;
        }

        public Builder withPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder isChecked(boolean checked) {
            this.checked = checked;
            return this;
        }

        public Builder withKeyResult(KeyResult keyResult) {
            this.keyResult = keyResult;
            return this;
        }

        public Action build() {
            return new Action(this);
        }
    }
}