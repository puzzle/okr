package ch.puzzle.okr.models.evaluation;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Immutable;

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
}
