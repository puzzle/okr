package ch.puzzle.okr.service;

import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.ProgressBusinessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {
    Objective objective;
    KeyResult keyResult;
    @MockBean
    ObjectiveBusinessService objectiveBusinessService = Mockito.mock(ObjectiveBusinessService.class);
    @MockBean
    KeyResultBusinessService keyResultBusinessService = Mockito.mock(KeyResultBusinessService.class);
    @MockBean
    ProgressBusinessService progressBusinessService = Mockito.mock(ProgressBusinessService.class);

    @InjectMocks
    @Spy
    private ProgressService progressService;

    @BeforeEach
    void setUp() {
        objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
        keyResult = KeyResult.Builder.builder().withId(1000L).withTitle("Keyresult 1").withObjective(objective)
                .withBasisValue(null).withTargetValue(10D).withExpectedEvolution(ExpectedEvolution.MIN).build();
    }

    @Test
    void shouldCallUpdateObjectiveProgress() {
        when(keyResultBusinessService.getKeyResultById(1L)).thenReturn(keyResult);

        progressService.deleteKeyResultAndUpdateProgress(1L);

        verify(progressService, times(1)).updateObjectiveProgress(any());
    }
}