package ch.puzzle.okr.service;

import ch.puzzle.okr.OkrApplication;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = OkrApplication.class)
class ProgressServiceTestIT {
    @InjectMocks
    private ProgressService progressService;
    @Mock
    private ObjectiveService objectiveService;
    @Mock
    private ObjectiveRepository objectiveRepository;

    @Test
    @Disabled
    void checkUpdateProgressMethod() {
        Objective objective = new Objective.Builder().withId(1L).withProgress(30L).build();
        when(this.objectiveService.getObjective(1L)).thenReturn(objective);
//        when(this.objectiveRepository.getProgressOfObjective(1L)).thenReturn(30D);
//
//        this.progressService.updateObjectiveProgress(1L);
//        verify(this.objectiveRepository, times(1)).getProgressOfObjective(1L);
        verify(this.objectiveRepository, times(1)).save(objective);
    }
}
