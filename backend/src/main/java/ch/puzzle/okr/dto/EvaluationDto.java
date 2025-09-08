package ch.puzzle.okr.dto;

public record EvaluationDto(long objectiveAmount, long completedObjectivesAmount,
        long successfullyCompletedObjectivesAmount, long keyResultAmount, long keyResultsOrdinalAmount,
        long keyResultsMetricAmount, long keyResultsInTargetOrStretchAmount, long keyResultsInFailAmount,
        long keyResultsInCommitAmount, long keyResultsInTargetAmount, long keyResultsInStretchAmount) {
}
