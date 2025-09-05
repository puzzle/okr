package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.EvaluationDto;
import ch.puzzle.okr.mapper.EvaluationViewMapper;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.service.business.EvaluationViewBusinessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationViewMapperTest {

    @InjectMocks
    private EvaluationViewMapper mapper;
    private EvaluationViewBusinessService businessService;

    @BeforeEach
    void setUp() {
        businessService = mock(EvaluationViewBusinessService.class);
        mapper = new EvaluationViewMapper(businessService);
    }

    @DisplayName("toDto maps values from EvaluationViewBusinessService correctly into EvaluationDto")
    @Test
    void toDto_shouldMapValuesFromBusinessService() {
        var views = Collections.singletonList(new EvaluationView());

        when(businessService.calculateObjectiveSum(views)).thenReturn(1);
        when(businessService.calculateCompletedObjectivesSum(views)).thenReturn(2);
        when(businessService.calculateSuccessfullyCompletedObjectivesSum(views)).thenReturn(3);
        when(businessService.calculateKeyResultSum(views)).thenReturn(4);
        when(businessService.calculateKeyResultsOrdinalSum(views)).thenReturn(5);
        when(businessService.calculateKeyResultsMetricSum(views)).thenReturn(6);
        when(businessService.calculateKeyResultsInTargetOrStretchSum(views)).thenReturn(7);
        when(businessService.calculateKeyResultsInFailSum(views)).thenReturn(8);
        when(businessService.calculateKeyResultsInCommitSum(views)).thenReturn(9);
        when(businessService.calculateKeyResultsInTargetSum(views)).thenReturn(10);
        when(businessService.calculateKeyResultsInStretchSum(views)).thenReturn(11);

        EvaluationDto dto = mapper.toDto(views);

        assertThat(dto.objectiveAmount()).isEqualTo(1);
        assertThat(dto.completedObjectivesAmount()).isEqualTo(2);
        assertThat(dto.successfullyCompletedObjectivesAmount()).isEqualTo(3);
        assertThat(dto.keyResultAmount()).isEqualTo(4);
        assertThat(dto.keyResultsOrdinalAmount()).isEqualTo(5);
        assertThat(dto.keyResultsMetricAmount()).isEqualTo(6);
        assertThat(dto.keyResultsInTargetOrStretchAmount()).isEqualTo(7);
        assertThat(dto.keyResultsInFailAmount()).isEqualTo(8);
        assertThat(dto.keyResultsInCommitAmount()).isEqualTo(9);
        assertThat(dto.keyResultsInTargetAmount()).isEqualTo(10);
        assertThat(dto.keyResultsInStretchAmount()).isEqualTo(11);

        verify(businessService).calculateObjectiveSum(views);
        verify(businessService).calculateCompletedObjectivesSum(views);
        verify(businessService).calculateSuccessfullyCompletedObjectivesSum(views);
        verify(businessService).calculateKeyResultSum(views);
        verify(businessService).calculateKeyResultsOrdinalSum(views);
        verify(businessService).calculateKeyResultsMetricSum(views);
        verify(businessService).calculateKeyResultsInTargetOrStretchSum(views);
        verify(businessService).calculateKeyResultsInFailSum(views);
        verify(businessService).calculateKeyResultsInCommitSum(views);
        verify(businessService).calculateKeyResultsInTargetSum(views);
        verify(businessService).calculateKeyResultsInStretchSum(views);
    }
}
