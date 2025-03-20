package ch.puzzle.okr.service.persistence;


import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static ch.puzzle.okr.util.quarter.EvaluationViewTestHelper.createEvaluationView;
import static org.assertj.core.api.Assertions.assertThat;

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

    private static Stream<Arguments> evaluationViewData() {
        EvaluationViewId evalViewVId_t5_q2 = new EvaluationViewId(5L, 2L);
        List<Integer> evalViewData_t5_q2 = List.of(2, 0, 0, 6, 2, 4, 2, 0, 4, 2, 0);
        EvaluationView evalView_t5_q2 = createEvaluationView(evalViewVId_t5_q2, evalViewData_t5_q2);

        EvaluationViewId evalViewVId_t6_q2 = new EvaluationViewId(6L, 2L);
        List<Integer> evalViewData_t6_q2 = List.of(3, 0, 0, 5, 0, 5, 1, 1, 3, 0, 1);
        EvaluationView evalView_t6_q2 = createEvaluationView(evalViewVId_t6_q2, evalViewData_t6_q2);

        EvaluationViewId evalViewVId_t4_q2 = new EvaluationViewId(4L, 2L);
        List<Integer> evalViewData_t4_q2 = List.of(2, 0, 0, 5, 0, 5, 3, 1, 1, 1, 2);
        EvaluationView evalView_t4_q2 = createEvaluationView(evalViewVId_t4_q2, evalViewData_t4_q2);

        return Stream
                .of(Arguments.of(List.of(evalViewVId_t5_q2), List.of(evalView_t5_q2)),
                    Arguments.of(List.of(evalViewVId_t6_q2), List.of(evalView_t6_q2)),
                    Arguments.of(List.of(evalViewVId_t4_q2), List.of(evalView_t4_q2)),
                    Arguments.of(List.of(evalViewVId_t4_q2, evalViewVId_t5_q2),
                                 List.of(evalView_t4_q2, evalView_t5_q2)),
                    Arguments.of(List.of(evalViewVId_t4_q2, evalViewVId_t5_q2, evalViewVId_t6_q2),
                                 List.of(evalView_t4_q2, evalView_t5_q2, evalView_t6_q2))
                   );
    }

    @ParameterizedTest(name = "Should return evaluation views")
    @MethodSource("evaluationViewData")
    void shouldReturnEvaluationViews(List<EvaluationViewId> ids, List<EvaluationView> expectedEvaluationViews) {
        List<EvaluationView> result = evaluationViewPersistenceService.findByTeamIdsAndQuarterId(ids);
        assertThat(result).hasSameElementsAs(expectedEvaluationViews);
    }
}
