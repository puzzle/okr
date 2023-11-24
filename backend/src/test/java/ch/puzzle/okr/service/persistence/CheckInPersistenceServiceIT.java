package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@SpringIntegrationTest
class CheckInPersistenceServiceIT {
    CheckIn createdCheckIn;

    @Autowired
    private CheckInPersistenceService checkInPersistenceService;

    private static CheckIn createCheckIn(Long id) {
        return createCheckIn(id, 1);
    }

    private static final String UPDATED_CHECKIN = "Updated CheckIn";

    private static CheckIn createCheckIn(Long id, int version) {
        return CheckInMetric.Builder.builder().withValue(30D).withId(id).withVersion(version)
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
    void saveCheckInShouldSaveNewCheckIn() {
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
    void updateKeyResultShouldUpdateKeyResult() {
        createdCheckIn = checkInPersistenceService.save(createCheckIn(null));
        CheckIn updateCheckIn = createCheckIn(createdCheckIn.getId(), createdCheckIn.getVersion());
        updateCheckIn.setChangeInfo(UPDATED_CHECKIN);

        CheckIn updatedCheckIn = checkInPersistenceService.save(updateCheckIn);

        assertEquals(createdCheckIn.getId(), updatedCheckIn.getId());
        assertEquals(createdCheckIn.getVersion() + 1, updatedCheckIn.getVersion());
        assertEquals(UPDATED_CHECKIN, updatedCheckIn.getChangeInfo());
    }

    @Test
    void updateKeyResultShouldThrowExceptionWhenAlreadyUpdated() {
        createdCheckIn = checkInPersistenceService.save(createCheckIn(null));
        CheckIn updateCheckIn = createCheckIn(createdCheckIn.getId(), 0);
        updateCheckIn.setChangeInfo(UPDATED_CHECKIN);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> checkInPersistenceService.save(updateCheckIn));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("DATA_HAS_BEEN_UPDATED", List.of("CheckIn")));

        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));

    }

    @Test
    void getAllCheckInShouldReturnListOfAllCheckIns() {
        List<CheckIn> checkIns = checkInPersistenceService.findAll();

        assertEquals(19, checkIns.size());
    }

    @Test
    void getCheckInByIdShouldReturnCheckInProperly() {
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
