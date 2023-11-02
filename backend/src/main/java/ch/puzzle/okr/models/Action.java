package ch.puzzle.okr.models;

import ch.puzzle.okr.models.keyresult.KeyResult;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Action implements WriteableInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_action")
    private Long id;

    @NotNull(message = "Action must not be null")
    @Size(max = 4096, message = "Attribute Action has a max length of 4096 characters")
    private String action;

    @NotNull(message = "Priority must not be null")
    private int priority;

    @NotNull(message = "IsChecked must not be null")
    private boolean isChecked;

    @NotNull(message = "KeyResult must not be null")
    @ManyToOne
    private KeyResult keyResult;

    private transient boolean writeable;

    public Action() {
    }

    private Action(Builder builder) {
        id = builder.id;
        action = builder.action;
        priority = builder.priority;
        isChecked = builder.isChecked;
        keyResult = builder.keyResult;
    }

    public Long getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public KeyResult getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(KeyResult keyResult) {
        this.keyResult = keyResult;
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
        return "Action{" + "id=" + id + ", action='" + action + '\'' + ", priority=" + priority + ", isChecked="
                + isChecked + ", keyResult=" + keyResult + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Action action1 = (Action) o;
        return priority == action1.priority && isChecked == action1.isChecked && Objects.equals(id, action1.id)
                && Objects.equals(action, action1.action) && Objects.equals(keyResult, action1.keyResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action, priority, isChecked, keyResult);
    }

    public static final class Builder {
        private Long id;
        private String action;
        private int priority;
        private boolean isChecked;
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

        public Builder withAction(String action) {
            this.action = action;
            return this;
        }

        public Builder withPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder withIsChecked(boolean isChecked) {
            this.isChecked = isChecked;
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