package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.EvaluationDto;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EvaluationViewMapper {
    public EvaluationDto toDto(List<EvaluationView> evaluationViews) {
        // TODO: loop only once?
        return new EvaluationDto(evaluationViews.stream().mapToInt(EvaluationView::getObjectiveAmount).sum(),
                                 evaluationViews.stream().mapToInt(EvaluationView::getCompletedObjectivesAmount).sum(),
                                 evaluationViews
                                         .stream()
                                         .mapToInt(EvaluationView::getSuccessfullyCompletedObjectivesAmount)
                                         .sum(),
                                 evaluationViews.stream().mapToInt(EvaluationView::getKeyResultAmount).sum(),
                                 evaluationViews.stream().mapToInt(EvaluationView::getKeyResultsOrdinalAmount).sum(),
                                 evaluationViews.stream().mapToInt(EvaluationView::getKeyResultsMetricAmount).sum(),
                                 evaluationViews
                                         .stream()
                                         .mapToInt(EvaluationView::getKeyResultsInTargetOrStretchAmount)
                                         .sum(),
                                 evaluationViews.stream().mapToInt(EvaluationView::getKeyResultsInFailAmount).sum(),
                                 evaluationViews.stream().mapToInt(EvaluationView::getKeyResultsInCommitAmount).sum(),
                                 evaluationViews.stream().mapToInt(EvaluationView::getKeyResultsInTargetAmount).sum(),
                                 evaluationViews.stream().mapToInt(EvaluationView::getKeyResultsInStretchAmount).sum());
    }

    public List<EvaluationViewId> fromDto(List<Long> teamIds, Long quarterId) {
        return teamIds.stream().map(teamId -> new EvaluationViewId(teamId, quarterId)).toList();
    }
}
