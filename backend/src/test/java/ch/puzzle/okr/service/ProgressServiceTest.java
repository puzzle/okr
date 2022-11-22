package ch.puzzle.okr.service;

import ch.puzzle.okr.OkrApplication;
import ch.puzzle.okr.models.Objective;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = OkrApplication.class)
class ProgressServiceTest {
    @Autowired
    private ProgressService progressService;

    @Test
    void checkUpdateProgressMethod() {
        Objective objective = Objective.Builder.builder().withId(1L).build();
        Double percentValue = this.progressService.getObjectiveProgressInPercent(objective);
        assertEquals(55D, percentValue);
    }

    @Test
    void checkProgressMethodWithNoMeasures() {
        Objective objective = Objective.Builder.builder().withId(2L).build();
        Double percentValue = this.progressService.getObjectiveProgressInPercent(objective);
        assertEquals(0D, percentValue);
    }

    @Test
    void checkProgressMethodWithNoKeyResults() {
        Objective objective = Objective.Builder.builder().withId(3L).build();
        Double percentValue = this.progressService.getObjectiveProgressInPercent(objective);
        assertEquals(0D, percentValue);
    }
}
