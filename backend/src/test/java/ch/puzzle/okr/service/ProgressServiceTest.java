package ch.puzzle.okr.service;

import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.MeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {
    KeyResult keyResult;
    Measure measure;

    @MockBean
    MeasureRepository measureRepository = Mockito.mock(MeasureRepository.class);

    @InjectMocks
    @Spy
    private ProgressService progressService;

    @BeforeEach
    void setUp() {
        keyResult = KeyResult.Builder.builder().withId(1000L).withBasisValue(null).withTargetValue(10D)
                .withExpectedEvolution(ExpectedEvolution.MIN).build();

        this.measure = Measure.Builder.builder()
                .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
                .withCreatedOn(LocalDateTime.MAX).withKeyResult(keyResult).withValue(5D).build();
    }

    private static Stream<Arguments> shouldCalculateKeyResultProgressForNoMinMax() {
        return Stream.of(Arguments.of(ExpectedEvolution.INCREASE), Arguments.of(ExpectedEvolution.DECREASE),
                Arguments.of(ExpectedEvolution.NONE), Arguments.of(ExpectedEvolution.CONSTANT));
    }

    @ParameterizedTest
    @MethodSource
    void shouldCalculateKeyResultProgressForNoMinMax(ExpectedEvolution expectedEvolution) {
        keyResult.setExpectedEvolution(expectedEvolution);
        keyResult.setBasisValue(0D);
        keyResult.setTargetValue(100D);
        when(measureRepository.findFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResult.getId()))
                .thenReturn(measure);

        progressService.calculateKeyResultProgress(keyResult);

        verify(progressService, times(1)).calculateKeyResultProgressForNoMinMax(any());
        verify(progressService, never()).calculateKeyResultProgressForMinMax(any());
    }

    private static Stream<Arguments> shouldCallUpdateKeyResultProgressForMinOrMax() {
        return Stream.of(Arguments.of(ExpectedEvolution.MIN), Arguments.of(ExpectedEvolution.MAX));
    }

    @ParameterizedTest
    @MethodSource
    void shouldCallUpdateKeyResultProgressForMinOrMax(ExpectedEvolution expectedEvolution) {
        keyResult.setExpectedEvolution(expectedEvolution);
        when(measureRepository.findFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResult.getId()))
                .thenReturn(measure);

        List<MeasureRepository.MeasureValue> measureValues = new ArrayList<>();
        measureValues.add(() -> 10D);
        when(measureRepository.findMeasuresByKeyResultId(any())).thenReturn(measureValues);

        progressService.calculateKeyResultProgress(keyResult);

        verify(progressService, times(1)).calculateKeyResultProgressForMinMax(any());
        verify(progressService, never()).calculateKeyResultProgressForNoMinMax(any());
    }

    private static Stream<Arguments> shouldThrowExceptionIfNoMinOrMaxCallUpdateKeyResultProgress() {
        return Stream.of(Arguments.of(ExpectedEvolution.NONE), Arguments.of(ExpectedEvolution.INCREASE),
                Arguments.of(ExpectedEvolution.DECREASE), Arguments.of(ExpectedEvolution.CONSTANT));
    }

    @ParameterizedTest
    @MethodSource
    void shouldThrowExceptionIfNoMinOrMaxCallUpdateKeyResultProgress(ExpectedEvolution expectedEvolution) {
        keyResult.setExpectedEvolution(expectedEvolution);

        List<MeasureRepository.MeasureValue> measureValues = new ArrayList<>();
        measureValues.add(() -> 10D);
        when(measureRepository.findMeasuresByKeyResultId(any())).thenReturn(measureValues);

        assertThrows(IllegalArgumentException.class,
                () -> progressService.calculateKeyResultProgressForMinMax(keyResult));
    }
}