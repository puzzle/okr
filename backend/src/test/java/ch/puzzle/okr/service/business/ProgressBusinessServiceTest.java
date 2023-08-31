package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.service.persistence.MeasurePersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class ProgressBusinessServiceTest {
    Objective objective;
    KeyResult keyResult;
    Measure measure;

    @MockBean
    MeasurePersistenceService measurePersistenceService = Mockito.mock(MeasurePersistenceService.class);

    @InjectMocks
    @Spy
    private ProgressBusinessService progressBusinessService;

    @BeforeEach
    void setUp() {
        objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
        keyResult = KeyResult.Builder.builder().withId(1000L).withTitle("Keyresult 1").withObjective(objective)
                .withBasisValue(null).withTargetValue(10D).withExpectedEvolution(ExpectedEvolution.MIN).build();
        measure = Measure.Builder.builder()
                .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
                .withCreatedOn(LocalDateTime.MAX).withKeyResult(keyResult).withValue(5D).build();
    }

}