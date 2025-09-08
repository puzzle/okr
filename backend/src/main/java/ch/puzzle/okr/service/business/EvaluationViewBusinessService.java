package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.State;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.service.persistence.EvaluationViewPersistenceService;
import ch.puzzle.okr.service.validation.EvaluationViewValidationService;
import ch.puzzle.okr.util.TeamQuarterFilter;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class EvaluationViewBusinessService {

    private final EvaluationViewPersistenceService evaluationViewPersistenceService;
    private final EvaluationViewValidationService evaluationViewValidationService;

    public EvaluationViewBusinessService(EvaluationViewPersistenceService evaluationViewPersistenceService,
                                         EvaluationViewValidationService evaluationViewValidationService) {
        this.evaluationViewPersistenceService = evaluationViewPersistenceService;
        this.evaluationViewValidationService = evaluationViewValidationService;
    }

    public List<EvaluationView> findByIds(TeamQuarterFilter filter) {
        evaluationViewValidationService.validateOnGet(filter);
        return evaluationViewPersistenceService.findByIds(filter);
    }

    public long calculateObjectiveSum(List<EvaluationView> views) {
        return views.stream().map(EvaluationView::getObjectiveId).distinct().count();
    }

    public long calculateCompletedObjectivesSum(List<EvaluationView> views) {
        return views
                .stream()
                .filter(Predicate.not(v -> State.ONGOING.equals(v.getObjectiveState())))
                .map(EvaluationView::getObjectiveId)
                .distinct()
                .count();
    }

    public long calculateSuccessfullyCompletedObjectivesSum(List<EvaluationView> views) {
        return views
                .stream()
                .filter(v -> State.SUCCESSFUL.equals(v.getObjectiveState()))
                .map(EvaluationView::getObjectiveId)
                .distinct()
                .count();
    }

    public long calculateKeyResultSum(List<EvaluationView> views) {
        return views.stream().map(EvaluationView::getKeyResultId).filter(Objects::nonNull).distinct().count();
    }

    public long calculateKeyResultsOrdinalSum(List<EvaluationView> views) {
        return views.stream().filter(v -> "ordinal".equalsIgnoreCase(v.getKeyResultType())).count();
    }

    public long calculateKeyResultsMetricSum(List<EvaluationView> views) {
        return views.stream().filter(v -> "metric".equalsIgnoreCase(v.getKeyResultType())).count();
    }

    public long calculateKeyResultsInTargetOrStretchSum(List<EvaluationView> views) {
        return views.stream().filter(this::isKeyResultInTargetOrStretch).count();
    }

    public long calculateKeyResultsInFailSum(List<EvaluationView> views) {
        return views.stream().filter(this::isKeyResultInFail).count();
    }

    public long calculateKeyResultsInCommitSum(List<EvaluationView> views) {
        return views.stream().filter(this::isKeyResultInCommit).count();
    }

    public long calculateKeyResultsInTargetSum(List<EvaluationView> views) {
        return views.stream().filter(this::isKeyResultInTarget).count();
    }

    public long calculateKeyResultsInStretchSum(List<EvaluationView> views) {
        return views.stream().filter(this::isKeyResultInStretch).count();
    }

    private boolean isKeyResultInTargetOrStretch(EvaluationView v) {
        if ("ordinal".equalsIgnoreCase(v.getKeyResultType())) {
            Zone zone = v.getZone();
            return zone == Zone.TARGET || zone == Zone.STRETCH;
        } else if ("metric".equalsIgnoreCase(v.getKeyResultType())) {
            double progress = calculateProgressRatio(v);
            return progress >= 0.7;
        }
        return false;
    }

    private boolean isKeyResultInFail(EvaluationView v) {
        if ("ordinal".equalsIgnoreCase(v.getKeyResultType())) {
            Zone zone = v.getZone();
            return zone == Zone.FAIL;
        } else if ("metric".equalsIgnoreCase(v.getKeyResultType())) {
            double progress = calculateProgressRatio(v);
            return progress < 0.3;
        }
        return false;
    }

    private boolean isKeyResultInCommit(EvaluationView v) {
        if ("ordinal".equalsIgnoreCase(v.getKeyResultType())) {
            Zone zone = v.getZone();
            return zone == Zone.COMMIT;
        } else if ("metric".equalsIgnoreCase(v.getKeyResultType())) {
            double progress = calculateProgressRatio(v);
            return progress >= 0.3 && progress < 0.7;
        }
        return false;
    }

    private boolean isKeyResultInTarget(EvaluationView v) {
        if ("ordinal".equalsIgnoreCase(v.getKeyResultType())) {
            Zone zone = v.getZone();
            return zone == Zone.TARGET;
        } else if ("metric".equalsIgnoreCase(v.getKeyResultType())) {
            double progress = calculateProgressRatio(v);
            return progress >= 0.7 && progress < 1.0;
        }
        return false;
    }

    private boolean isKeyResultInStretch(EvaluationView v) {
        if ("ordinal".equalsIgnoreCase(v.getKeyResultType())) {
            Zone zone = v.getZone();
            return zone == Zone.STRETCH;
        } else if ("metric".equalsIgnoreCase(v.getKeyResultType())) {
            double progress = calculateProgressRatio(v);
            return progress >= 1.0;
        }
        return false;
    }

    private Double calculateProgressRatio(EvaluationView v) {
        if (v.getBaseline() == null || v.getStretchGoal() == null || v.getValueMetric() == null) {
            return 0D;
        }
        double denominator = v.getStretchGoal() - v.getBaseline();
        if (denominator == 0) {
            return 0D;
        }
        return (v.getValueMetric() - v.getBaseline()) / denominator;
    }
}
