package ch.puzzle.okr.service.validation;

import static org.mockito.Mockito.*;

import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import java.util.List;

import ch.puzzle.okr.util.TeamQuarterFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EvaluationViewValidationServiceTest {

    @Mock
    private QuarterValidationService quarterValidationService;
    @Mock
    private TeamValidationService teamValidationService;
    @InjectMocks
    private EvaluationViewValidationService evaluationViewValidationService;

    @DisplayName("Should call proper methods to validate on get")
    @Test
    void shouldCallProperMethodsToValidateOnGet() {
        TeamQuarterFilter ids = new TeamQuarterFilter(List.of(1L, 2L), 1L);
        evaluationViewValidationService.validateOnGet(ids);
        verify(teamValidationService, times(2)).validateOnGet(any());
        verify(quarterValidationService, times(1)).validateOnGet(any());
    }
}
