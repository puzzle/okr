package ch.puzzle.okr.service;

import ch.puzzle.okr.OkrApplication;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
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
        Objective objective = Objective.Builder.builder().withId(1L).build();
        Quarter quarter = Quarter.Builder.builder().withId(1L).build();
        User user = User.Builder.builder().withId(1L).build();
        KeyResult keyResult = KeyResult.Builder.builder().withObjective(objective)
                .withTargetValue(100L).withTitle("HelloWorld")
                .withDescription("Very Important").withBasisValue(50L)
                .withQuarter(quarter).withUnit(Unit.PERCENT)
                .withExpectedEvolution(ExpectedEvolution.DECREASE)
                .withOwner(user).withCreatedBy(user)
                .withCreatedOn(LocalDateTime.now()).build();

        this.keyResultRepository.save(keyResult);
        this.progressService.updateObjectiveProgress(1L);

        Objective updatedObjective = this.objectiveService.getObjective(1L);
        assertEquals(44D, updatedObjective.getProgress());
    }

    @Test
    void checkProgressMethodWithNoMeasures() {
        Objective objective = Objective.Builder.builder().withId(1L).build();
        KeyResult keyResult = KeyResult.Builder.builder().withId(1L).withObjective(objective).build();
        User user = User.Builder.builder().withId(1L).build();
        Measure measure = Measure.Builder.builder().withValue(90)
                .withKeyResult(keyResult).withCreatedBy(user).withCreatedOn(LocalDateTime.now())
                .withChangeInfo("ChangeInfo").withInitiatives("Initiatives").build();

        this.measureRepository.save(measure);
        this.progressService.updateObjectiveProgress(1L);
        Objective updatedObjective = this.objectiveService.getObjective(1L);
        assertEquals(62.5D, updatedObjective.getProgress());
    }

    @Test
    void checkProgressMethodWithNoKeyResults() {
        Objective objective = Objective.Builder.builder().withId(2L).build();
        KeyResult keyResult = KeyResult.Builder.builder().withId(5L).withObjective(objective).build();
        User user = User.Builder.builder().withId(1L).build();
        Measure measure = Measure.Builder.builder().withValue(90)
                .withKeyResult(keyResult).withCreatedBy(user).withCreatedOn(LocalDateTime.now())
                .withChangeInfo("ChangeInfo").withInitiatives("Initiatives").build();

        this.measureRepository.save(measure);
        this.progressService.updateObjectiveProgress(2L);
        Objective updatedObjective = this.objectiveService.getObjective(2L);
        assertEquals(45D, updatedObjective.getProgress());
    }
}
