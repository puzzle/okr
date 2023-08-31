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
                .withCreatedBy(User.Builder.builder().withId(1l).withFirstname("Frank").build())
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

    @Test
    void saveMeasure_ShouldSaveNewMeasure() {
        Measure measure = createMeasure(null);

        createdMeasure = measurePersistenceService.saveMeasure(measure);

        assertNotNull(createdMeasure.getId());
        assertEquals(measure.getMeasureDate(), createdMeasure.getMeasureDate());
        assertEquals(measure.getValue(), createdMeasure.getValue());
        assertEquals(measure.getCreatedBy(), createdMeasure.getCreatedBy());
        assertEquals(measure.getCreatedOn(), createdMeasure.getCreatedOn());
        assertEquals(measure.getInitiatives(), createdMeasure.getInitiatives());
        assertEquals(measure.getChangeInfo(), createdMeasure.getChangeInfo());
    }

    @Test
    void updateKeyResult_ShouldUpdateKeyResult() {
        Measure measure = createMeasure(null);
        createdMeasure = measurePersistenceService.saveMeasure(measure);
        createdMeasure.setChangeInfo("Updated Measure");

        Measure updateMeasure = measurePersistenceService.updateMeasure(createdMeasure.getId(), createdMeasure);

        assertEquals(createdMeasure.getId(), updateMeasure.getId());
        assertEquals("Updated Measure", updateMeasure.getChangeInfo());
    }

    @Test
    void updateKeyResult_ShouldThrowExceptionWhenKeyResultNotFound() {
        Measure measure = createMeasure(321L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> measurePersistenceService.updateMeasure(measure.getId(), measure));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Measure with id 321 not found", exception.getReason());
    }

    @Test
    void getAllMeasure_ShouldReturnListOfAllMeasures() {
        List<Measure> measures = measurePersistenceService.getAllMeasures();

        assertEquals(20, measures.size());
    }

    @Test
    void getMeasureById_ShouldReturnMeasureProperly() {
        Measure measure = measurePersistenceService.getMeasureById(20L);

        assertEquals(20L, measure.getId());
        assertEquals(0.5, measure.getValue(), 0.01);
        assertEquals(
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ",
                measure.getChangeInfo());
    }

    @Test
    void getMeasureById_ShouldThrowExceptionWhenMeasureNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> measurePersistenceService.getMeasureById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Measure with id 321 not found", exception.getReason());
    }

    @Test
    void getMeasuretById_ShouldThrowExceptionWhenMeasureIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> measurePersistenceService.getMeasureById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing attribute measure id", exception.getReason());
    }

    @Test
    void getMeasuresByKeyResultIdAndMeasureDate_ShouldReturnMeasuresProperly() {
        List<Measure> measures = measurePersistenceService.getMeasuresByKeyResultIdAndMeasureDate(8L,
                Instant.parse("2023-07-24T20:00:00.00Z"));
        assertEquals(1, measures.size());
    }

    @Test
    void deleteMeasureById_ShouldDeleteMeasure() {
        Measure measure = createMeasure(null);
        createdMeasure = measurePersistenceService.saveMeasure(measure);
        measurePersistenceService.deleteMeasureById(createdMeasure.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> measurePersistenceService.getMeasureById(createdMeasure.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Measure with id %d not found", createdMeasure.getId()), exception.getReason());
    }
}
