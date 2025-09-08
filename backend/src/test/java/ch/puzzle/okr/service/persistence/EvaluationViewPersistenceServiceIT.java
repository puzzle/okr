package ch.puzzle.okr.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import ch.puzzle.okr.util.TeamQuarterFilter;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringIntegrationTest
class EvaluationViewPersistenceServiceIT {

    @Autowired
    private EvaluationViewPersistenceService evaluationViewPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @AfterEach
    void tearDown() {
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Should correctly return evaluation views according to params")
    @Test
    void shouldReturnEvaluationViews() {
        List<Long> teamIds = List.of(5L); // Puzzle ITC
        Long quarterId = 2L;

        TeamQuarterFilter filter = new TeamQuarterFilter(teamIds, quarterId);

        List<EvaluationView> evaluationViews = evaluationViewPersistenceService.findByIds(filter);

        List<Long> expectedObjectiveIds = List.of(3L, 4L);

        List<Long> actualObjectiveIds = evaluationViews
                .stream()
                .map(EvaluationView::getObjectiveId)
                .distinct()
                .toList();

        assertThat(actualObjectiveIds).containsExactlyInAnyOrderElementsOf(expectedObjectiveIds);
    }
}
