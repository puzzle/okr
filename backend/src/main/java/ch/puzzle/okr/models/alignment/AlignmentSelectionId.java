package ch.puzzle.okr.models.alignment;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AlignmentSelectionId implements Serializable {

    private Long objectiveId;
    private Long keyResultId;

    public AlignmentSelectionId() {
    }

    private AlignmentSelectionId(Long objectiveId, Long keyResultId) {
        this.objectiveId = objectiveId;
        this.keyResultId = keyResultId;
    }

    private AlignmentSelectionId(Builder builder) {
        this(builder.objectiveId, builder.keyResultId);
    }

    public static AlignmentSelectionId of(Long objectiveId, Long keyResultId) {
        return new AlignmentSelectionId(objectiveId, keyResultId);
    }

    public Long getObjectiveId() {
        return objectiveId;
    }

    public Long getKeyResultId() {
        return keyResultId;
    }

    @Override
    public String toString() {
        return "AlignmentSelectionId{" + "objectiveId=" + objectiveId + ", keyResultId=" + keyResultId + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AlignmentSelectionId that = (AlignmentSelectionId) o;
        return Objects.equals(objectiveId, that.objectiveId) && Objects.equals(keyResultId, that.keyResultId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectiveId, keyResultId);
    }

    public static final class Builder {
        private Long objectiveId;
        private Long keyResultId;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withObjectiveId(Long objectiveId) {
            this.objectiveId = objectiveId;
            return this;
        }

        public Builder withKeyResultId(Long keyResultId) {
            this.keyResultId = keyResultId;
            return this;
        }

        public AlignmentSelectionId build() {
            return new AlignmentSelectionId(this);
        }
    }
}
