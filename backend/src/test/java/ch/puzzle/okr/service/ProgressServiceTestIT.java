package ch.puzzle.okr.service;

import ch.puzzle.okr.OkrApplication;
import ch.puzzle.okr.helper.KeyResultMeasureValue;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = OkrApplication.class)
class ProgressServiceTestIT {
    private static ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
    private static KeyResultMeasureValue keyResultMeasureValue = factory.createProjection(KeyResultMeasureValue.class);
    @InjectMocks
    @Spy
    private ProgressService progressService;
    @Mock
    private ObjectiveService objectiveService;
    @Mock
    private ObjectiveRepository objectiveRepository;
    @Mock
    private KeyResultRepository keyResultRepository;

    private static Stream<Arguments> shouldReturnCorrectKeyResultProgress() {
        return Stream.of(Arguments.of(null, 11.5D, null), Arguments.of(keyResultMeasureValue, 50.25D, 50L),
                Arguments.of(keyResultMeasureValue, 15.789D, 15L), Arguments.of(keyResultMeasureValue, 25D, 25L));
    }

    private static Stream<Arguments> shouldCalculateObjectiveProgress() {
        return Stream.of(Arguments.of(List.of(keyResultMeasureValue, keyResultMeasureValue), 11.5D, 11L),
                Arguments.of(List.of(keyResultMeasureValue, keyResultMeasureValue, keyResultMeasureValue), 56.7D, 56L),
                Arguments.of(List.of(keyResultMeasureValue), 100D, 100L), Arguments.of(List.of(), 0D, null));
    }

    private static Stream<Arguments> shouldReturnProgressRestrictedFromOneToHundred() {
        return Stream.of(Arguments.of(120D, 100D), Arguments.of(50D, 50D), Arguments.of(-50D, 0D),
                Arguments.of(25D, 25D));
    }

    private static Stream<Arguments> shouldReturnCorrectProgress() {
        return Stream.of(Arguments.of(120, 100, 120, 100D), Arguments.of(50, 85, 65, 57.142857142857146D),
                Arguments.of(100, 0, 80, 80D));
    }

    @BeforeEach
    void setUp() {
        keyResultMeasureValue.setTargetValue(0);
        keyResultMeasureValue.setBasisValue(0);
        keyResultMeasureValue.setValue(0);
    }

    @Test
    void shouldMakeCallsToUpdateObjectiveProgress() {
        Objective objective = new Objective();
        when(this.objectiveService.getObjective(anyLong())).thenReturn(objective);
        when(this.objectiveRepository.getCalculationValuesForProgress(anyLong()))
                .thenReturn(List.of(keyResultMeasureValue, keyResultMeasureValue));
        when(this.progressService.calculateObjectiveProgress(anyList())).thenReturn(null);
        when(this.keyResultRepository.findByObjectiveOrderByTitle(objective)).thenReturn(List.of(new KeyResult()));

        this.progressService.updateObjectiveProgress(1L);

        verify(this.objectiveService, times(1)).getObjective(anyLong());
        verify(this.objectiveRepository, times(1)).save(objective);
    }

    @ParameterizedTest
    @MethodSource
    void shouldReturnCorrectKeyResultProgress(KeyResultMeasureValue keyResultMeasureValue, Double checkedProgress,
            Long expectedProgress) {
        when(this.keyResultRepository.getProgressValuesKeyResult(1L)).thenReturn(keyResultMeasureValue);
        if (keyResultMeasureValue != null) {
            when(this.progressService.returnCheckedProgress(keyResultMeasureValue)).thenReturn(checkedProgress);
        }
        Long calculatedProgress = this.progressService.updateKeyResultProgress(1L);
        assertEquals(expectedProgress, calculatedProgress);
    }

    @ParameterizedTest
    @MethodSource
    void shouldCalculateObjectiveProgress(List<KeyResultMeasureValue> keyResultMeasureValues, Double checkedProgress,
            Long expectedProgress) {
        when(this.progressService.returnCheckedProgress(keyResultMeasureValue)).thenReturn(checkedProgress);
        Long calculatedProgress = this.progressService.calculateObjectiveProgress(keyResultMeasureValues);
        assertEquals(expectedProgress, calculatedProgress);
    }

    @ParameterizedTest
    @MethodSource
    void shouldReturnProgressRestrictedFromOneToHundred(Double progress, Double optimizedProgress) {
        when(progressService.calculateKeyResultProgress(keyResultMeasureValue)).thenReturn(progress);
        Double calculatedProgress = this.progressService.returnCheckedProgress(keyResultMeasureValue);
        assertEquals(optimizedProgress, calculatedProgress);
    }

    @ParameterizedTest
    @MethodSource
    void shouldReturnCorrectProgress(int targetValue, int basisValue, int value, Double expectedProgress) {
        keyResultMeasureValue.setTargetValue(targetValue);
        keyResultMeasureValue.setBasisValue(basisValue);
        keyResultMeasureValue.setValue(value);
        Double progress = progressService.calculateKeyResultProgress(keyResultMeasureValue);
        assertEquals(expectedProgress, progress);
    }
}
