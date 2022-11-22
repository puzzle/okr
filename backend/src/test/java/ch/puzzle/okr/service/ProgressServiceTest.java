package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @MockBean
    KeyResultRepository keyResultRepository = Mockito.mock(KeyResultRepository.class);

    @MockBean
    MeasureRepository measureRepository = Mockito.mock(MeasureRepository.class);

    @MockBean
    ObjectiveRepository objectiveRepository = Mockito.mock(ObjectiveRepository.class);

    @MockBean
    ObjectiveService objectiveService = Mockito.mock(ObjectiveService.class);

    @InjectMocks
    ProgressService progressService;

    // Test if progress in percent is calculated correctly
    @Test
    @Disabled
    void checkUpdateProgressMethod() {
        Objective objective = Objective.Builder.builder().withId(1L).build();
        KeyResult keyResult = KeyResult.Builder.builder().withId(1L).withDescription("Hello").withTargetValue(100L)
                .withObjective(objective).build();
        KeyResult keyResult2 = KeyResult.Builder.builder().withId(2L).withDescription("Hello").withTargetValue(200L)
                .withObjective(objective).build();
        List<KeyResult> keyResultList = Arrays.asList(keyResult, keyResult2);

        Measure measure = Measure.Builder.builder().withKeyResult(keyResult).withValue(50).build();
        Measure measure2 = Measure.Builder.builder().withKeyResult(keyResult2).withValue(60).build();
        List<Measure> measureList = Arrays.asList(measure, measure2);

        when(keyResultRepository.findAll()).thenReturn(keyResultList);
        when(measureRepository.findAll()).thenReturn(measureList);

//        Double percentValue = this.progressService.getObjectiveProgressInPercent(1L);
//        assertEquals(40D, percentValue);
    }

    @Test
    @Disabled
    void checkUpdateProgressMethodWithThreeKeyResults() {
        Objective objective = Objective.Builder.builder().withId(1L).build();
        KeyResult keyResult = KeyResult.Builder.builder().withId(1L).withDescription("Hello").withTargetValue(100L)
                .withObjective(objective).build();
        KeyResult keyResult2 = KeyResult.Builder.builder().withId(2L).withDescription("Hello").withTargetValue(200L)
                .withObjective(objective).build();
        KeyResult keyResult3 = KeyResult.Builder.builder().withId(3L).withDescription("Hello").withTargetValue(300L)
                .withObjective(objective).build();
        List<KeyResult> keyResultList = Arrays.asList(keyResult, keyResult2, keyResult3);

        Measure measure = Measure.Builder.builder().withKeyResult(keyResult).withValue(50).build();
        Measure measure2 = Measure.Builder.builder().withKeyResult(keyResult2).withValue(60).build();
        List<Measure> measureList = Arrays.asList(measure, measure2);

        when(keyResultRepository.findAll()).thenReturn(keyResultList);
        when(measureRepository.findAll()).thenReturn(measureList);

//        Double percentValue = this.progressService.getObjectiveProgressInPercent(1L);
//        assertEquals(26.666666666666668, percentValue);
    }

    @Test
    @Disabled
    void checkUpdateProgressWithOneKeyResult() {
        Objective objective = Objective.Builder.builder().withId(1L).build();
        KeyResult keyResult = KeyResult.Builder.builder().withId(1L).withDescription("Hello").withTargetValue(100L)
                .withObjective(objective).build();
        List<KeyResult> keyResultList = List.of(keyResult);

        Measure measure = Measure.Builder.builder().withKeyResult(keyResult).withValue(30).build();
        Measure measure2 = Measure.Builder.builder().withKeyResult(keyResult).withValue(50).build();
        List<Measure> measureList = new ArrayList<>();
        measureList.add(measure);
        measureList.add(measure2);

        when(keyResultRepository.findAll()).thenReturn(keyResultList);
        when(measureRepository.findAll()).thenReturn(measureList);

//        Double percentValue = this.progressService.getObjectiveProgressInPercent(1L);
//        assertEquals(50D, percentValue);
    }
}
