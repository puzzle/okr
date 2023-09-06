package ch.puzzle.okr.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@DiscriminatorValue("objective")
public class ObjectiveAlignment extends Alignment implements AlignmentInterface<Objective> {

    @ManyToOne
    private Objective targetObjective;

    public ObjectiveAlignment() {
        super();
    }

    public ObjectiveAlignment(Builder builder) {
        super(builder);
        setAlignmentTarget(builder.targetObjective);
    }

    @Override
    public Objective getAlignmentTarget() {
        return targetObjective;
    }

    @Override
    public void setAlignmentTarget(Objective alignmentTarget) {
        targetObjective = alignmentTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o))
            return false;
        ObjectiveAlignment that = (ObjectiveAlignment) o;
        return Objects.equals(targetObjective, that.targetObjective);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), targetObjective);
    }

    @Override
    public String toString() {
        return "ObjectiveAlignment{" + super.toString() + "targetObjective=" + targetObjective + '}';
    }

    public static final class Builder extends Alignment.Builder<Builder> {
        private Objective targetObjective;

        private Builder() {
            super();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withTargetObjective(Objective targetObjective) {
            this.targetObjective = targetObjective;
            return this;
        }

        @Override
        public ObjectiveAlignment build() {
            return new ObjectiveAlignment(this);
        }
    }
}
