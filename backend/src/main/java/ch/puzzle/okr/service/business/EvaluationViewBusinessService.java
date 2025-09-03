package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.repository.EvaluationViewRepository;
import ch.puzzle.okr.util.TeamQuarterFilter;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class EvaluationViewBusinessService {

    private final EvaluationViewRepository evaluationViewRepository;

    public EvaluationViewBusinessService(EvaluationViewRepository evaluationViewRepository) {
        this.evaluationViewRepository = evaluationViewRepository;
    }

    public List<EvaluationView> findByFilter(TeamQuarterFilter filter) {
        return evaluationViewRepository.findByTeamIdInAndQuarterId(filter.teamIds(), filter.quarterId());
    }

    public int calculateObjectiveSum(List<EvaluationView> views) {
        return (int) views.stream().map(EvaluationView::getObjectiveId).distinct().count();
    }

    public int calculateCompletedObjectivesSum(List<EvaluationView> views) {
        return (int) views
                .stream()
                .filter(Predicate.not(v -> "ONGOING".equals(v.getObjectiveState())))
                .map(EvaluationView::getObjectiveId)
                .distinct()
                .count();
    }

    public int calculateSuccessfullyCompletedObjectivesSum(List<EvaluationView> views) {
        return (int) views
                .stream()
                .filter(v -> "SUCCESSFUL".equals(v.getObjectiveState()))
                .map(EvaluationView::getObjectiveId)
                .distinct()
                .count();
    }

    public int calculateKeyResultSum(List<EvaluationView> views) {
        return (int) views.stream().map(EvaluationView::getKeyResultId).filter(Objects::nonNull).distinct().count();
    }

    public int calculateKeyResultsOrdinalSum(List<EvaluationView> views) {
        return (int) views.stream().filter(v -> "ordinal".equalsIgnoreCase(v.getKeyResultType())).count();
    }

    public int calculateKeyResultsMetricSum(List<EvaluationView> views) {
        return (int) views.stream().filter(v -> "metric".equalsIgnoreCase(v.getKeyResultType())).count();
    }

    public int calculateKeyResultsInTargetOrStretchSum(List<EvaluationView> views) {
        return (int) views.stream().filter(this::isKeyResultInTargetOrStretch).count();
    }

    public int calculateKeyResultsInFailSum(List<EvaluationView> views) {
        return (int) views.stream().filter(this::isKeyResultInFail).count();
    }

    public int calculateKeyResultsInCommitSum(List<EvaluationView> views) {
        return (int) views.stream().filter(this::isKeyResultInCommit).count();
    }

    public int calculateKeyResultsInTargetSum(List<EvaluationView> views) {
        return (int) views.stream().filter(this::isKeyResultInTarget).count();
    }

    public int calculateKeyResultsInStretchSum(List<EvaluationView> views) {
        return (int) views.stream().filter(this::isKeyResultInStretch).count();
    }

    private boolean isKeyResultInTargetOrStretch(EvaluationView v) {
        if ("ordinal".equalsIgnoreCase(v.getKeyResultType())) {
            return "TARGET".equalsIgnoreCase(v.getZone()) || "STRETCH".equalsIgnoreCase(v.getZone());
        } else if ("metric".equalsIgnoreCase(v.getKeyResultType())) {
            Double progress = calculateProgress(v);
            return progress != null && progress >= 0.7;
        }
        return false;
    }

    private boolean isKeyResultInFail(EvaluationView v) {
        if ("ordinal".equalsIgnoreCase(v.getKeyResultType())) {
            return "FAIL".equalsIgnoreCase(v.getZone());
        } else if ("metric".equalsIgnoreCase(v.getKeyResultType())) {
            Double progress = calculateProgress(v);
            return progress != null && progress < 0.3;
        }
        return false;
    }

    private boolean isKeyResultInCommit(EvaluationView v) {
        if ("ordinal".equalsIgnoreCase(v.getKeyResultType())) {
            return "COMMIT".equalsIgnoreCase(v.getZone());
        } else if ("metric".equalsIgnoreCase(v.getKeyResultType())) {
            Double progress = calculateProgress(v);
            return progress != null && progress >= 0.3 && progress < 0.7;
        }
        return false;
    }

    private boolean isKeyResultInTarget(EvaluationView v) {
        if ("ordinal".equalsIgnoreCase(v.getKeyResultType())) {
            return "TARGET".equalsIgnoreCase(v.getZone());
        } else if ("metric".equalsIgnoreCase(v.getKeyResultType())) {
            Double progress = calculateProgress(v);
            return progress != null && progress >= 0.7 && progress < 1.0;
        }
        return false;
    }

    private boolean isKeyResultInStretch(EvaluationView v) {
        if ("ordinal".equalsIgnoreCase(v.getKeyResultType())) {
            return "STRETCH".equalsIgnoreCase(v.getZone());
        } else if ("metric".equalsIgnoreCase(v.getKeyResultType())) {
            Double progress = calculateProgress(v);
            return progress != null && progress >= 1.0;
        }
        return false;
    }

    private Double calculateProgress(EvaluationView v) {
        if (v.getBaseline() == null || v.getStretchGoal() == null || v.getValueMetric() == null) {
            return null;
        }
        double denominator = v.getStretchGoal() - v.getBaseline();
        if (denominator == 0) {
            return null;
        }
        return (v.getValueMetric() - v.getBaseline()) / denominator;
    }
}
