package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.EvaluationDto;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import java.util.List;

import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import ch.puzzle.okr.service.business.EvaluationViewBusinessService;
import org.springframework.stereotype.Component;

@Component
public class EvaluationViewMapper {

    private final EvaluationViewBusinessService evaluationService;

    public EvaluationViewMapper(EvaluationViewBusinessService evaluationService) {
        this.evaluationService = evaluationService;
    }

    public EvaluationDto toDto(List<EvaluationView> views) {
        return new EvaluationDto(
                evaluationService.calculateObjectiveSum(views),
                evaluationService.calculateCompletedObjectivesSum(views),
                evaluationService.calculateSuccessfullyCompletedObjectivesSum(views),
                evaluationService.calculateKeyResultSum(views),
                evaluationService.calculateKeyResultsOrdinalSum(views),
                evaluationService.calculateKeyResultsMetricSum(views),
                evaluationService.calculateKeyResultsInTargetOrStretchSum(views),
                evaluationService.calculateKeyResultsInFailSum(views),
                evaluationService.calculateKeyResultsInCommitSum(views),
                evaluationService.calculateKeyResultsInTargetSum(views),
                evaluationService.calculateKeyResultsInStretchSum(views)
        );
    }

    public List<EvaluationViewId> fromDto(List<Long> teamIds, Long quarterId) {
        return teamIds.stream().map(teamId -> new EvaluationViewId(teamId, quarterId)).toList();
    }
}