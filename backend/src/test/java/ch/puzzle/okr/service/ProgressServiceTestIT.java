package ch.puzzle.okr.service;

import ch.puzzle.okr.OkrApplication;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = OkrApplication.class)
class ProgressServiceTestIT {
    @Autowired
    private ProgressService progressService;
    @Autowired
    private KeyResultRepository keyResultRepository;
    @Autowired
    private ObjectiveService objectiveService;
    @Autowired
    private MeasureRepository measureRepository;

    @Test
    void checkUpdateProgressMethod() {
        this.progressService.updateObjectiveProgress(1L);
        Objective updatedObjective = this.objectiveService.getObjective(1L);
        assertEquals(61, updatedObjective.getProgress());
    }

    @Test
    @Disabled("Marc wird diesen Test überarbeiten.")
    void checkProgressMethodWithNoMeasures() {
        Objective objective = Objective.Builder.builder().withId(1L).build();
        KeyResult keyResult = KeyResult.Builder.builder().withId(1L).withObjective(objective).build();
        User user = User.Builder.builder().withId(1L).build();
        Measure measure = Measure.Builder.builder().withValue(90).withKeyResult(keyResult).withCreatedBy(user)
                .withCreatedOn(LocalDateTime.now()).withChangeInfo("ChangeInfo").withInitiatives("Initiatives").build();

        this.measureRepository.save(measure);
        assertEquals(20, this.objectiveService.getObjective(1L).getProgress());
        this.progressService.updateObjectiveProgress(1L);
        Objective updatedObjective = this.objectiveService.getObjective(1L);
        assertEquals(63, updatedObjective.getProgress());
    }

    @Test
    @Disabled("Marc wird diesen Test überarbeiten.")
    void checkProgressMethodWithNoKeyResults() {
        Objective objective = Objective.Builder.builder().withId(2L).build();
        KeyResult keyResult = KeyResult.Builder.builder().withId(5L).withObjective(objective).build();
        User user = User.Builder.builder().withId(1L).build();
        Measure measure = Measure.Builder.builder().withValue(90).withKeyResult(keyResult).withCreatedBy(user)
                .withCreatedOn(LocalDateTime.now()).withChangeInfo("ChangeInfo").withInitiatives("Initiatives").build();

        this.measureRepository.save(measure);
        assertEquals(60, this.objectiveService.getObjective(2L).getProgress());

        this.progressService.updateObjectiveProgress(2L);
        Objective updatedObjective = this.objectiveService.getObjective(2L);
        assertEquals(90, updatedObjective.getProgress());
    }

    @Test
    @Disabled("Marc wird diesen Test überarbeiten.")
    void checkProgressIfMeasureDecreases() {
        Objective objective = Objective.Builder.builder().withId(1L).build();
        KeyResult keyResult = KeyResult.Builder.builder().withId(1L).withObjective(objective).build();
        User user = User.Builder.builder().withId(1L).build();
        Measure measure = Measure.Builder.builder().withValue(40).withKeyResult(keyResult).withCreatedBy(user)
                .withCreatedOn(LocalDateTime.now()).withChangeInfo("ChangeInfo").withInitiatives("Initiatives").build();

        this.measureRepository.save(measure);
        assertEquals(20, this.objectiveService.getObjective(1L).getProgress());
        this.progressService.updateObjectiveProgress(1L);
        Objective updatedObjective = this.objectiveService.getObjective(1L);
        assertEquals(50, updatedObjective.getProgress());
    }
}
