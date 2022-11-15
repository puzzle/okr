package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
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

    @InjectMocks
    ProgressService progressService;

    @Test
    void checkUpdateProgressMethod() {
        Objective objective = Objective.Builder.builder().withId(1L).build();
        KeyResult keyResult = KeyResult.Builder.builder()
                .withId(1L)
                .withDescription("Hello")
                .withTargetValue(100L)
                .withObjective(objective).build();
        KeyResult keyResult2 = KeyResult.Builder.builder()
                .withId(2L)
                .withDescription("Hello")
                .withTargetValue(200L)
                .withObjective(objective).build();
        List<KeyResult> keyResultList = Arrays.asList(keyResult, keyResult2);

        Measure measure = Measure.Builder.builder().withKeyResult(keyResult).withValue(50).build();
        Measure measure2 = Measure.Builder.builder().withKeyResult(keyResult2).withValue(60).build();
        List<Measure> measureList = Arrays.asList(measure, measure2);

        when(keyResultRepository.findAll()).thenReturn(keyResultList);
        when(measureRepository.findAll()).thenReturn(measureList);

        Long percentValue = this.progressService.updateObjectiveProgressValue(1L);
        assertEquals(40, percentValue);
    }
}
