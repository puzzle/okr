package ch.puzzle.okr.models;

import ch.puzzle.okr.models.keyresult.KeyResult;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@DiscriminatorValue("keyResult")
public class KeyResultAlignment extends Alignment implements AlignmentInterface<KeyResult> {

    @ManyToOne
    private KeyResult targetKeyResult;

    public KeyResultAlignment() {
        super();
    }

    private KeyResultAlignment(Builder builder) {
        super(builder);
        setAlignmentTarget(builder.targetKeyResult);
    }

    @Override
    public KeyResult getAlignmentTarget() {
        return targetKeyResult;
    }

    @Override
    public void setAlignmentTarget(KeyResult alignmentTarget) {
        this.targetKeyResult = alignmentTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o))
            return false;
        KeyResultAlignment that = (KeyResultAlignment) o;
        return Objects.equals(targetKeyResult, that.targetKeyResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), targetKeyResult);
    }

    @Override
    public String toString() {
        return "KeyResultAlignment{" + super.toString() + "alignmentTarget=" + targetKeyResult + '}';
    }

    public static final class Builder extends Alignment.Builder<Builder> {
        private KeyResult targetKeyResult;

        private Builder() {
            super();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withTargetKeyResult(KeyResult targetKeyResult) {
            this.targetKeyResult = targetKeyResult;
            return this;
        }

        @Override
        public KeyResultAlignment build() {
            return new KeyResultAlignment(this);
        }
    }
}
