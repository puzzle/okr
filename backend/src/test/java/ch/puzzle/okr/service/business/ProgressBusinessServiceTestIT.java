package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.service.persistence.MeasurePersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.stream.Stream;

@SpringIntegrationTest
class ProgressBusinessServiceTestIT {
    @Mock
    private MeasurePersistenceService measurePersistenceService;
    @InjectMocks
    @Spy
    private ProgressBusinessService progressBusinessService;

    private static Stream<Arguments> shouldCalculateKeyResultProgressNoMinMax() {
        return Stream.of(Arguments.of(ExpectedEvolution.INCREASE, 100D, 120D, 120D, 100D),
                Arguments.of(ExpectedEvolution.INCREASE, 85D, 50D, 65D, 57.142857142857146D),
                Arguments.of(ExpectedEvolution.INCREASE, 0D, 100D, 80D, 80D));
    }
}
