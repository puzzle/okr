package ch.puzzle.okr.test;

import ch.puzzle.okr.dto.EvaluationDto;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import ch.puzzle.okr.util.TeamQuarterFilter;

import java.util.List;
import java.util.Random;

public class EvaluationViewTestHelper {
    private EvaluationViewTestHelper() {
    }

    public static List<EvaluationViewId> getEvaluationViewIds(List<Long> teamIds, Long quarterId) {
        return teamIds.stream().map(teamId -> new EvaluationViewId(teamId, quarterId)).toList();
    }

    public static List<EvaluationView> generateEvaluationViews(TeamQuarterFilter filter) {
        return filter.teamIds().stream()
                .map(teamId -> createEvaluationView(filter.quarterId(), teamId))
                .toList();
    }

    public static EvaluationView createEvaluationView(Long quarterId, Long teamId) {
        return EvaluationView.Builder
                .builder()
                .withRowId(1L)
                .withTeamId(teamId)
                .withQuarterId(quarterId)
                .build();
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
}
