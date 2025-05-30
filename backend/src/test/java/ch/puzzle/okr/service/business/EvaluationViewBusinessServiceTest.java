package ch.puzzle.okr.service.business;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import ch.puzzle.okr.service.persistence.EvaluationViewPersistenceService;
import ch.puzzle.okr.service.validation.EvaluationViewValidationService;
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

    @DisplayName("Should validate method calls on get by ids")
    @Test
    void shouldGetAction() {
        List<EvaluationViewId> ids = List.of(new EvaluationViewId(1L, 1L), new EvaluationViewId(2L, 1L));
        evaluationViewBusinessService.findByIds(ids);
        verify(evaluationViewPersistenceService, times(1)).findByIds(anyList());
        verify(evaluationViewValidationService, times(1)).validateOnGet(anyList());
    }
}
