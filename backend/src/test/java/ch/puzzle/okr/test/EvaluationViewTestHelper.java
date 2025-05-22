package ch.puzzle.okr.test;

import ch.puzzle.okr.dto.EvaluationDto;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import java.util.List;
import java.util.Random;

public class EvaluationViewTestHelper {
    private EvaluationViewTestHelper() {
    }

    public static List<EvaluationViewId> getEvaluationViewIds(List<Long> teamIds, Long quarterId) {
        return teamIds.stream().map(teamId -> new EvaluationViewId(teamId, quarterId)).toList();
    }

    public static List<EvaluationView> generateEvaluationViews(List<EvaluationViewId> evaluationViewIds) {
        return evaluationViewIds.stream().map(EvaluationViewTestHelper::generateRandomEvaluationView).toList();
    }

    public static EvaluationView createEvaluationView(EvaluationViewId evaluationViewId, List<Integer> data) {
        return EvaluationView.Builder
                .builder()
                .withEvaluationViewId(evaluationViewId)
                .withObjectiveAmount(data.get(0))
                .withCompletedObjectivesAmount(data.get(1))
                .withSuccessfullyCompletedObjectivesAmount(data.get(2))
                .withKeyResultAmount(data.get(3))
                .withKeyResultsOrdinalAmount(data.get(4))
                .withKeyResultsMetricAmount(data.get(5))
                .withKeyResultsInTargetOrStretchAmount(data.get(6))
                .withKeyResultsInFailAmount(data.get(7))
                .withKeyResultsInCommitAmount(data.get(8))
                .withKeyResultsInTargetAmount(data.get(9))
                .withKeyResultsInStretchAmount(data.get(10))
                .build();
    }

    public static EvaluationView generateRandomEvaluationView(EvaluationViewId evaluationViewId) {
        return createEvaluationView(evaluationViewId, generateEvaluationViewData());
    }

    public static EvaluationDto generateEvaluationDto() {
        return generateEvaluationDto(generateEvaluationViewData());
    }

    public static EvaluationDto generateEvaluationDto(List<Integer> data) {
        return new EvaluationDto(data.get(0),
                                 data.get(1),
                                 data.get(2),
                                 data.get(3),
                                 data.get(4),
                                 data.get(5),
                                 data.get(6),
                                 data.get(7),
                                 data.get(8),
                                 data.get(9),
                                 data.get(10));
    }

    public static List<Integer> generateEvaluationViewData() {
        return new Random().ints(11, 0, 101).boxed().toList();
    }

    private static int randomInt() {
        return (int) (Math.random() * 101);
    }
}
