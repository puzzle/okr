package ch.puzzle.okr.service.business;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ch.puzzle.okr.service.persistence.EvaluationViewPersistenceService;
import ch.puzzle.okr.service.validation.EvaluationViewValidationService;
import java.util.List;

import ch.puzzle.okr.util.TeamQuarterFilter;
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
}
