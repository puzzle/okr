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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
                .withConfidence(5).build();
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
        CheckIn checkIn = createCheckIn(null);
        createdCheckIn = checkInPersistenceService.save(checkIn);
        createdCheckIn.setChangeInfo("Updated CheckIn");

        CheckIn updateCheckIn = checkInPersistenceService.save(createdCheckIn);

        assertEquals(createdCheckIn.getId(), updateCheckIn.getId());
        assertEquals("Updated CheckIn", updateCheckIn.getChangeInfo());
    }

    @Test
    void getAllCheckIn_ShouldReturnListOfAllCheckIns() {
        List<CheckIn> checkIns = checkInPersistenceService.findAll();

        assertEquals(19, checkIns.size());
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
    void shouldGetCheckInsByKeyResultIdAndOrderThemByDateDesc() {
        List<CheckIn> checkIns = checkInPersistenceService.getCheckInsByKeyResultIdOrderByCheckInDateDesc(7L);
        assertTrue(checkIns.get(0).getCreatedOn().isAfter(checkIns.get(checkIns.size() - 1).getCreatedOn()));
    }

    @Test
    void shouldGetLastCheckInOfKeyResult() {
        CheckIn checkIn = checkInPersistenceService.getLastCheckInOfKeyResult(7L);
        List<CheckIn> checkInList = checkInPersistenceService.getCheckInsByKeyResultIdOrderByCheckInDateDesc(7L);
        for (CheckIn checkInLoop : checkInList) {
            if (!Objects.equals(checkInLoop.getId(), checkIn.getId())) {
                assertTrue(checkIn.getCreatedOn().isAfter(checkInLoop.getCreatedOn()));
            }
        }
    }
}
