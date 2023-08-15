package ch.puzzle.okr.service;

import ch.puzzle.okr.OkrApplication;
import ch.puzzle.okr.controller.TeamController;
import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.MeasureRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WithMockUser(value = "spring")
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = OkrApplication.class)
class ProgressServiceTestIT {
    @InjectMocks
    @Spy
    private ProgressService progressService;
    @Mock
    private MeasureRepository measureRepository;

    private static Stream<Arguments> shouldCalculateKeyResultProgressNoMinMax() {
        return Stream.of(Arguments.of(ExpectedEvolution.INCREASE, 100D, 120D, 120D, 100D),
                Arguments.of(ExpectedEvolution.INCREASE, 85D, 50D, 65D, 57.142857142857146D),
                Arguments.of(ExpectedEvolution.INCREASE, 0D, 100D, 80D, 80D));
    }

    @ParameterizedTest
    @MethodSource
    void shouldCalculateKeyResultProgressNoMinMax(ExpectedEvolution expectedEvolution, Double basisValue,
            Double targetValue, Double value, Double expectedProgress) {
        KeyResult keyResult = KeyResult.Builder.builder().withId(1L).withExpectedEvolution(expectedEvolution)
                .withBasisValue(basisValue).withTargetValue(targetValue).build();
        Measure measure = Measure.Builder.builder().withValue(value).build();

        when(measureRepository.findFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(1L)).thenReturn(measure);

        Double progress = progressService.calculateKeyResultProgressForNoMinMax(keyResult);
        assertEquals(expectedProgress, progress);
    }

    private static Stream<Arguments> shouldCalculateKeyResultProgressMinMax() {
        return Stream.of(
                // one measure for MIN
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(-1D), 0D),
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(0D), 0D),
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(1D), 0D),
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(99.9D), 0D),
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(100D), 100D),
                Arguments.of(ExpectedEvolution.MIN, null, 100.0D, of(100.0D), 100D),
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(101D), 100D),
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(1_000_00D), 100D),
                // one measure for MAX
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(-1_000_000D), 100D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(-1D), 100D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(0D), 100D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(1D), 100D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(100D), 100D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(100.0D), 100D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(100.1D), 0D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(101D), 0D),
                // more that on measure for MIN
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(0D, 99D, 99.9D), 0D),
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(0D, 101D, 99.9D), 33D),
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(99.9D, 1200D, 100.1D), 66D),
                Arguments.of(ExpectedEvolution.MIN, null, 100D, of(100.1D, 100.0D, 100D), 100D),
                // more that on measure for MAX
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(0D, 0D, 0D), 100D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(0D, 50D, 100D), 100D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(0D, 50D, 100.1D), 66D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(101D, 99.9D, 100.1D), 33D),
                Arguments.of(ExpectedEvolution.MAX, null, 100D, of(0D, 99.9D, 0.9D), 100D));
    }

    @ParameterizedTest
    @MethodSource
    void shouldCalculateKeyResultProgressMinMax(ExpectedEvolution expectedEvolution, Double basisValue,
            Double targetValue, List<Double> values, Double expectedProgress) {
        KeyResult keyResult = KeyResult.Builder.builder().withId(1L).withExpectedEvolution(expectedEvolution)
                .withBasisValue(basisValue).withTargetValue(targetValue).build();

        List<MeasureRepository.MeasureValue> measureValues = new ArrayList<>();
        values.forEach(value -> measureValues.add(() -> value));
        when(measureRepository.findMeasuresByKeyResultId(1L)).thenReturn(measureValues);

        double calculatedProgress = this.progressService.calculateKeyResultProgressForMinMax(keyResult);
        assertEquals(expectedProgress, calculatedProgress);
    }
}
