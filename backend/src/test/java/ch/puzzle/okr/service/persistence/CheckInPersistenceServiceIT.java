package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInMetric;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
public class CheckInPersistenceServiceIT {
    CheckIn createdCheckIn;
    @Autowired
    private CheckInPersistenceService checkInPersistenceService;

    private static CheckIn createCheckIn(Long id) {
        return CheckInMetric.Builder.builder().withValue(30D).withId(id)
                .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
                .withCreatedOn(LocalDateTime.MAX)
                .withKeyResult(KeyResultMetric.Builder.builder().withBaseline(1.0).withStretchGoal(13.0).withId(8L)
                        .withObjective(Objective.Builder.builder().withId(1L).build()).build())
                .withChangeInfo("ChangeInfo").withInitiatives("Initiatives").withModifiedOn(LocalDateTime.MAX)
                .withConfidence(5).withCheckInType("metric").build();
    }

    @AfterEach
    void tearDown() {
        try {
            if (createdCheckIn != null) {
                checkInPersistenceService.findById(createdCheckIn.getId());
                checkInPersistenceService.deleteById(createdCheckIn.getId());
            }
        } catch (ResponseStatusException ex) {
            // created CheckIn already deleted
        } finally {
            createdCheckIn = null;
        }
    }

    @Test
    void saveCheckIn_ShouldSaveNewCheckIn() {
        CheckIn checkIn = createCheckIn(null);

        createdCheckIn = checkInPersistenceService.save(checkIn);

        assertNotNull(createdCheckIn.getId());
        assertEquals(checkIn.getModifiedOn(), createdCheckIn.getModifiedOn());
        assertEquals(((CheckInMetric) checkIn).getValue(), ((CheckInMetric) createdCheckIn).getValue());
        assertEquals(checkIn.getCreatedBy(), createdCheckIn.getCreatedBy());
        assertEquals(checkIn.getCreatedOn(), createdCheckIn.getCreatedOn());
        assertEquals(checkIn.getInitiatives(), createdCheckIn.getInitiatives());
        assertEquals(checkIn.getChangeInfo(), createdCheckIn.getChangeInfo());
    }

    @Test
    void updateKeyResult_ShouldUpdateKeyResult() {
        CheckIn CheckIn = createCheckIn(null);
        createdCheckIn = checkInPersistenceService.save(CheckIn);
        createdCheckIn.setChangeInfo("Updated CheckIn");

        CheckIn updateCheckIn = checkInPersistenceService.save(createdCheckIn);

        assertEquals(createdCheckIn.getId(), updateCheckIn.getId());
        assertEquals("Updated CheckIn", updateCheckIn.getChangeInfo());
    }

    @Test
    void updateKeyResult_ShouldThrowExceptionWhenKeyResultNotFound() {
        CheckIn CheckIn = createCheckIn(321L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInPersistenceService.save(CheckIn));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("CheckIn with id 321 not found", exception.getReason());
    }

    @Test
    void getAllCheckIn_ShouldReturnListOfAllCheckIns() {
        List<CheckIn> CheckIns = checkInPersistenceService.findAll();

        assertEquals(20, CheckIns.size());
    }

    @Test
    void getCheckInById_ShouldReturnCheckInProperly() {
        CheckIn checkIn = checkInPersistenceService.findById(20L);

        assertEquals(20L, checkIn.getId());
        assertEquals(0.5, ((CheckInMetric) checkIn).getValue(), 0.01);
        assertEquals(
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ",
                checkIn.getChangeInfo());
    }

    @Test
    void getCheckInById_ShouldThrowExceptionWhenCheckInNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInPersistenceService.findById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("CheckIn with id 321 not found", exception.getReason());
    }

    @Test
    void getCheckIntById_ShouldThrowExceptionWhenCheckInIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInPersistenceService.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing attribute CheckIn id", exception.getReason());
    }

    @Test
    void deleteCheckInById_ShouldDeleteCheckIn() {
        CheckIn CheckIn = createCheckIn(null);
        createdCheckIn = checkInPersistenceService.save(CheckIn);
        checkInPersistenceService.deleteById(createdCheckIn.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInPersistenceService.findById(createdCheckIn.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("CheckIn with id %d not found", createdCheckIn.getId()), exception.getReason());
    }
}
