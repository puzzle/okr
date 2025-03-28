package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.EvaluationDto;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EvaluationViewMapper {
    public EvaluationDto toDto(List<EvaluationView> evaluationViews) {
        int objectiveSum = 0;
        int completedObjectivesSum = 0;
        int successfullyCompletedObjectivesSum = 0;
        int keyResultSum = 0;
        int keyResultsOrdinalSum = 0;
        int keyResultsMetricSum = 0;
        int keyResultsInTargetOrStretchSum = 0;
        int keyResultsInFailSum = 0;
        int keyResultsInCommitSum = 0;
        int keyResultsInTargetSum = 0;
        int keyResultsInStretchSum = 0;

        for (EvaluationView view : evaluationViews) {
            objectiveSum += view.getObjectiveAmount();
            completedObjectivesSum += view.getCompletedObjectivesAmount();
            successfullyCompletedObjectivesSum += view.getSuccessfullyCompletedObjectivesAmount();
            keyResultSum += view.getKeyResultAmount();
            keyResultsOrdinalSum += view.getKeyResultsOrdinalAmount();
            keyResultsMetricSum += view.getKeyResultsMetricAmount();
            keyResultsInTargetOrStretchSum += view.getKeyResultsInTargetOrStretchAmount();
            keyResultsInFailSum += view.getKeyResultsInFailAmount();
            keyResultsInCommitSum += view.getKeyResultsInCommitAmount();
            keyResultsInTargetSum += view.getKeyResultsInTargetAmount();
            keyResultsInStretchSum += view.getKeyResultsInStretchAmount();
        }

        return new EvaluationDto(
                objectiveSum,
                completedObjectivesSum,
                successfullyCompletedObjectivesSum,
                keyResultSum,
                keyResultsOrdinalSum,
                keyResultsMetricSum,
                keyResultsInTargetOrStretchSum,
                keyResultsInFailSum,
                keyResultsInCommitSum,
                keyResultsInTargetSum,
                keyResultsInStretchSum
        );
    }

    public List<EvaluationViewId> fromDto(List<Long> teamIds, Long quarterId) {
        return teamIds.stream().map(teamId -> new EvaluationViewId(teamId, quarterId)).toList();
    }
}
