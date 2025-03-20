package ch.puzzle.okr.dto;

public record EvaluationDto(int objectiveAmount, int completedObjectivesAmount,
        int successfullyCompletedObjectivesAmount, int keyResultAmount, int keyResultsOrdinalAmount,
        int keyResultsMetricAmount, int keyResultsInTargetOrStretchAmount, int keyResultsInFailAmount,
        int keyResultsInCommitAmount, int keyResultsInTargetAmount, int keyResultsInStretchAmount) {
}
