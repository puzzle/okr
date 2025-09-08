package ch.puzzle.okr.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ch.puzzle.okr.models.State;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.service.persistence.EvaluationViewPersistenceService;
import ch.puzzle.okr.service.validation.EvaluationViewValidationService;
import ch.puzzle.okr.util.TeamQuarterFilter;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EvaluationViewBusinessServiceTest {
    @Mock
    private EvaluationViewValidationService evaluationViewValidationService;
    @Mock
    private EvaluationViewPersistenceService evaluationViewPersistenceService;
    @InjectMocks
    private EvaluationViewBusinessService evaluationViewBusinessService;

    @DisplayName("Should validate method call on findByIds")
    @Test
    void shouldGetAction() {

        List<Long> teamIds = List.of(1L, 2L);
        Long quarterId = 1L;

        TeamQuarterFilter ids = new TeamQuarterFilter(teamIds, quarterId);

        evaluationViewBusinessService.findByIds(ids);
        verify(evaluationViewPersistenceService, times(1)).findByIds(ids);
        verify(evaluationViewValidationService, times(1)).validateOnGet(ids);
    }

    @DisplayName("Should calculate the correct sum of unique objectives")
    @Test
    void testCalculateObjectiveSum() {
        assertEquals(3, evaluationViewBusinessService.calculateObjectiveSum(evaluationViewList));
    }

    @DisplayName("Should calculate the correct sum of completed objectives")
    @Test
    void testCalculateCompletedObjectivesSum() {
        assertEquals(2, evaluationViewBusinessService.calculateCompletedObjectivesSum(evaluationViewList));
    }

    @DisplayName("Should calculate the correct sum of successfully completed objectives")
    @Test
    void testCalculateSuccessfullyCompletedObjectivesSum() {
        assertEquals(1, evaluationViewBusinessService.calculateSuccessfullyCompletedObjectivesSum(evaluationViewList));
    }

    @DisplayName("Should calculate the correct sum of key results")
    @Test
    void testCalculateKeyResultSum() {
        assertEquals(9, evaluationViewBusinessService.calculateKeyResultSum(evaluationViewList));
    }

    @DisplayName("Should calculate the correct sum of ordinal key results")
    @Test
    void testCalculateKeyResultsOrdinalSum() {
        assertEquals(4, evaluationViewBusinessService.calculateKeyResultsOrdinalSum(evaluationViewList));
    }

    @DisplayName("Should calculate the correct sum of metric key results")
    @Test
    void testCalculateKeyResultsMetricSum() {
        assertEquals(4, evaluationViewBusinessService.calculateKeyResultsMetricSum(evaluationViewList));
    }

    @DisplayName("Should calculate the correct sum of key results in Target or Stretch zone")
    @Test
    void testCalculateKeyResultsInTargetOrStretchSum() {
        assertEquals(4, evaluationViewBusinessService.calculateKeyResultsInTargetOrStretchSum(evaluationViewList));
    }

    @DisplayName("Should calculate the correct sum of key results in Fail zone")
    @Test
    void testCalculateKeyResultsInFailSum() {
        assertEquals(2, evaluationViewBusinessService.calculateKeyResultsInFailSum(evaluationViewList));
    }

    @DisplayName("Should calculate the correct sum of key results in Commit zone")
    @Test
    void testCalculateKeyResultsInCommitSum() {
        assertEquals(2, evaluationViewBusinessService.calculateKeyResultsInCommitSum(evaluationViewList));
    }

    @DisplayName("Should calculate the correct sum of key results in Target zone")
    @Test
    void testCalculateKeyResultsInTargetSum() {
        assertEquals(2, evaluationViewBusinessService.calculateKeyResultsInTargetSum(evaluationViewList));
    }

    @DisplayName("Should calculate the correct sum of key results in Stretch zone")
    @Test
    void testCalculateKeyResultsInStretchSum() {
        assertEquals(2, evaluationViewBusinessService.calculateKeyResultsInStretchSum(evaluationViewList));
    }

    private static EvaluationView createEvaluationView(Long rowId, Long keyResultId, Long objectiveId,
                                                       State objectiveSate, String keyResultType, Double baseline,
                                                       Double commitValue, Double targetValue, Double stretchGoal,
                                                       Double valueMetric, Zone zone) {
        return EvaluationView.Builder
                .builder()
                .withRowId(rowId)
                .withKeyResultId(keyResultId)
                .withObjectiveId(objectiveId)
                .withTeamId(1L) // Not needed because filtering of the data given is done at this point
                .withQuarterId(1L) // Not needed because filtering of the data given is done at this point
                .withObjectiveState(objectiveSate)
                .withKeyResultType(keyResultType)
                .withBaseline(baseline)
                .withCommitValue(commitValue)
                .withTargetValue(targetValue)
                .withStretchGoal(stretchGoal)
                .withValueMetric(valueMetric)
                .withZone(zone)
                .withLatestCheckInDate(Instant.now())
                .build();
    }

    private static final List<EvaluationView> evaluationViewList = List
            .of(createEvaluationView(1L, 1L, 1L, State.ONGOING, "metric", 0D, 3D, 7D, 10D, 1D, null),
                createEvaluationView(2L, 2L, 1L, State.ONGOING, "ordinal", null, null, null, null, null, Zone.TARGET),
                createEvaluationView(3L, 3L, 1L, State.NOTSUCCESSFUL, "metric", 0D, 3D, 7D, 10D, 3D, null),
                createEvaluationView(4L, 4L, 2L, State.SUCCESSFUL, "metric", 0D, 3D, 7D, 10D, 7D, null),
                createEvaluationView(5L, 5L, 2L, State.SUCCESSFUL, "metric", 0D, 3D, 7D, 10D, 10D, null),
                createEvaluationView(6L, 6L, 3L, State.ONGOING, "ordinal", null, null, null, null, null, Zone.FAIL),
                createEvaluationView(7L, 7L, 3L, State.ONGOING, "ordinal", null, null, null, null, null, Zone.COMMIT),
                createEvaluationView(8L, 8L, 3L, State.ONGOING, "ordinal", null, null, null, null, null, Zone.STRETCH),
                createEvaluationView(9L, 9L, 3L, State.ONGOING, null, null, null, null, null, null, null),
                createEvaluationView(10L, 10L, 4L, State.ONGOING, "metric", null, null, null, null, 10D, null));
}
