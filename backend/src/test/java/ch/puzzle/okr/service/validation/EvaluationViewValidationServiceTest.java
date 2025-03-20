package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationViewValidationServiceTest {

    @Mock private QuarterValidationService quarterValidationService;
    @Mock private TeamValidationService teamValidationService;
    @InjectMocks private EvaluationViewValidationService evaluationViewValidationService;

    @DisplayName("Should call proper methods to validate on get")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNull() {
        List<EvaluationViewId> ids = List.of(new EvaluationViewId(1L, 1L), new EvaluationViewId(2L, 1L));
        evaluationViewValidationService.validateOnGet(ids);
        verify(teamValidationService, times(2)).validateOnGet(any());
        verify(quarterValidationService, times(1)).validateOnGet(any());
    }
}
