package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.EvaluationDto;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EvaluationViewMapper {
    public EvaluationDto toDto(List<EvaluationView> evaluationViews) {
        return new EvaluationDto(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
    }

    public List<EvaluationViewId> fromDto(List<Long> teamIds, Long quarterId) {
        return teamIds.stream().map(teamId -> new EvaluationViewId(teamId, quarterId)).toList();
    }
}
