package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
public class MeasurePersistenceServiceIT {
    Measure createdMeasure;
    @Autowired
    private MeasurePersistenceService measurePersistenceService;

    private static Measure createMeasure(Long id) {
        return Measure.Builder.builder().withId(id)
                .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
                .withCreatedOn(LocalDateTime.MAX)
                .withKeyResult(KeyResultMetric.Builder.builder().withBaseline(1.0).withStretchGoal(13.0).withId(8L)
                        .withObjective(Objective.Builder.builder().withId(1L).build()).build())
                .withValue(30D).withChangeInfo("ChangeInfo").withInitiatives("Initiatives")
                .withMeasureDate(Instant.parse("2021-11-03T00:00:00.00Z")).build();
    }

    @AfterEach
    void tearDown() {
        try {
            if (createdMeasure != null) {
                measurePersistenceService.getMeasureById(createdMeasure.getId());
                measurePersistenceService.deleteMeasureById(createdMeasure.getId());
            }
        } catch (ResponseStatusException ex) {
            // created measure already deleted
        } finally {
            createdMeasure = null;
        }
    }
}
