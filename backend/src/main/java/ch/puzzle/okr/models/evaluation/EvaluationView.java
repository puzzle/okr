package ch.puzzle.okr.models.evaluation;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Immutable;

import java.util.Objects;

@Entity
@Immutable
public class EvaluationView {
    @EmbeddedId
    private EvaluationViewId evaluationViewId;

    private int objectiveAmount;
    private int completedObjectivesAmount;
    private int successfullyCompletedObjectivesAmount;

    private int keyResultAmount;
    private int keyResultsOrdinalAmount;
    private int keyResultsMetricAmount;
    private int keyResultsInTargetOrStretchAmount;

    private int keyResultsInFailAmount;
    private int keyResultsInCommitAmount;
    private int keyResultsInTargetAmount;
    private int keyResultsInStretchAmount;

    private EvaluationView(Builder builder) {
        evaluationViewId = builder.evaluationViewId;
        objectiveAmount = builder.objectiveAmount;
        completedObjectivesAmount = builder.completedObjectivesAmount;
        successfullyCompletedObjectivesAmount = builder.successfullyCompletedObjectivesAmount;
        keyResultAmount = builder.keyResultAmount;
        keyResultsOrdinalAmount = builder.keyResultsOrdinalAmount;
        keyResultsMetricAmount = builder.keyResultsMetricAmount;
        keyResultsInTargetOrStretchAmount = builder.keyResultsInTargetOrStretchAmount;
        keyResultsInFailAmount = builder.keyResultsInFailAmount;
        keyResultsInCommitAmount = builder.keyResultsInCommitAmount;
        keyResultsInTargetAmount = builder.keyResultsInTargetAmount;
        keyResultsInStretchAmount = builder.keyResultsInStretchAmount;
    }

    public EvaluationView() {
    }

    public EvaluationViewId getEvaluationViewId() {
        return evaluationViewId;
    }

    public int getObjectiveAmount() {
        return objectiveAmount;
    }

    public int getCompletedObjectivesAmount() {
        return completedObjectivesAmount;
    }

    public int getSuccessfullyCompletedObjectivesAmount() {
        return successfullyCompletedObjectivesAmount;
    }

    public int getKeyResultAmount() {
        return keyResultAmount;
    }

    public int getKeyResultsOrdinalAmount() {
        return keyResultsOrdinalAmount;
    }

    public int getKeyResultsMetricAmount() {
        return keyResultsMetricAmount;
    }

    public int getKeyResultsInTargetOrStretchAmount() {
        return keyResultsInTargetOrStretchAmount;
    }

    public int getKeyResultsInFailAmount() {
        return keyResultsInFailAmount;
    }

    public int getKeyResultsInCommitAmount() {
        return keyResultsInCommitAmount;
    }

    public int getKeyResultsInTargetAmount() {
        return keyResultsInTargetAmount;
    }

    public int getKeyResultsInStretchAmount() {
        return keyResultsInStretchAmount;
    }


    public static final class Builder {
        private EvaluationViewId evaluationViewId;
        private int objectiveAmount;
        private int completedObjectivesAmount;
        private int successfullyCompletedObjectivesAmount;
        private int keyResultAmount;
        private int keyResultsOrdinalAmount;
        private int keyResultsMetricAmount;
        private int keyResultsInTargetOrStretchAmount;
        private int keyResultsInFailAmount;
        private int keyResultsInCommitAmount;
        private int keyResultsInTargetAmount;
        private int keyResultsInStretchAmount;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withEvaluationViewId(EvaluationViewId val) {
            evaluationViewId = val;
            return this;
        }

        public Builder withObjectiveAmount(int val) {
            objectiveAmount = val;
            return this;
        }

        public Builder withCompletedObjectivesAmount(int val) {
            completedObjectivesAmount = val;
            return this;
        }

        public Builder withSuccessfullyCompletedObjectivesAmount(int val) {
            successfullyCompletedObjectivesAmount = val;
            return this;
        }

        public Builder withKeyResultAmount(int val) {
            keyResultAmount = val;
            return this;
        }

        public Builder withKeyResultsOrdinalAmount(int val) {
            keyResultsOrdinalAmount = val;
            return this;
        }

        public Builder withKeyResultsMetricAmount(int val) {
            keyResultsMetricAmount = val;
            return this;
        }

        public Builder withKeyResultsInTargetOrStretchAmount(int val) {
            keyResultsInTargetOrStretchAmount = val;
            return this;
        }

        public Builder withKeyResultsInFailAmount(int val) {
            keyResultsInFailAmount = val;
            return this;
        }

        public Builder withKeyResultsInCommitAmount(int val) {
            keyResultsInCommitAmount = val;
            return this;
        }

        public Builder withKeyResultsInTargetAmount(int val) {
            keyResultsInTargetAmount = val;
            return this;
        }

        public Builder withKeyResultsInStretchAmount(int val) {
            keyResultsInStretchAmount = val;
            return this;
        }

        public EvaluationView build() {
            return new EvaluationView(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EvaluationView that)) {
            return false;
        }
        return getObjectiveAmount() == that.getObjectiveAmount() &&
               getCompletedObjectivesAmount() == that.getCompletedObjectivesAmount() &&
               getSuccessfullyCompletedObjectivesAmount() == that.getSuccessfullyCompletedObjectivesAmount() &&
               getKeyResultAmount() == that.getKeyResultAmount() &&
               getKeyResultsOrdinalAmount() == that.getKeyResultsOrdinalAmount() &&
               getKeyResultsMetricAmount() == that.getKeyResultsMetricAmount() &&
               getKeyResultsInTargetOrStretchAmount() == that.getKeyResultsInTargetOrStretchAmount() &&
               getKeyResultsInFailAmount() == that.getKeyResultsInFailAmount() &&
               getKeyResultsInCommitAmount() == that.getKeyResultsInCommitAmount() &&
               getKeyResultsInTargetAmount() == that.getKeyResultsInTargetAmount() &&
               getKeyResultsInStretchAmount() == that.getKeyResultsInStretchAmount() &&
               Objects.equals(getEvaluationViewId(), that.getEvaluationViewId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEvaluationViewId(),
                            getObjectiveAmount(),
                            getCompletedObjectivesAmount(),
                            getSuccessfullyCompletedObjectivesAmount(),
                            getKeyResultAmount(),
                            getKeyResultsOrdinalAmount(),
                            getKeyResultsMetricAmount(),
                            getKeyResultsInTargetOrStretchAmount(),
                            getKeyResultsInFailAmount(),
                            getKeyResultsInCommitAmount(),
                            getKeyResultsInTargetAmount(),
                            getKeyResultsInStretchAmount());
    }
}
