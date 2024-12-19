package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

import static ch.puzzle.okr.Constants.CHECK_IN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringIntegrationTest
class CheckInPersistenceServiceIT {

    private static final long KEY_RESULT_ID = 7L;

    @Autowired
    private CheckInPersistenceService checkInPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @AfterEach
    void tearDown() {
        TenantContext.setCurrentTenant(null);
    }

    // uses data from V100_0_0__TestData.sql
    @DisplayName("should get checkIns by keyResultId and order them by date desc on getCheckInsByKeyResultIdOrderByCheckInDate() ")
    @Test
    void getCheckInsByKeyResultIdOrderByCheckInDateShouldGetCheckInsByKeyResultIdAndOrderThemByDateDesc() {
        // act
        List<CheckIn> checkIns = checkInPersistenceService
                .getCheckInsByKeyResultIdOrderByCheckInDateDesc(KEY_RESULT_ID);

        // assert
        assertThat(2, greaterThanOrEqualTo(checkIns.size()));
        CheckIn firstCheckIn = checkIns.get(0);
        CheckIn lastCheckIn = checkIns.get(checkIns.size() - 1);
        assertFirstIsCreatedAfterSecond(firstCheckIn, lastCheckIn);
    }

    private void assertFirstIsCreatedAfterSecond(CheckIn first, CheckIn second) {
        assertTrue(first.getCreatedOn().isAfter(second.getCreatedOn()));
    }

    // uses data from V100_0_0__TestData.sql
    @DisplayName("should get last checkIn of keyResult on getLastCheckInOfKeyResult()")
    @Test
    void getLastCheckInOfKeyResultShouldGetLastCheckInOfKeyResult() {
        // act
        var lastCheckIn = checkInPersistenceService.getLastCheckInOfKeyResult(KEY_RESULT_ID);

        // assert
        var allCheckins = checkInPersistenceService.getCheckInsByKeyResultIdOrderByCheckInDateDesc(KEY_RESULT_ID);
        assertLastIsCreatedAfterAllOtherCheckIns(lastCheckIn, allCheckins);
    }

    private void assertLastIsCreatedAfterAllOtherCheckIns(CheckIn last, List<CheckIn> allCheckIns) {
        for (CheckIn checkIn : allCheckIns) {
            if (!Objects.equals(checkIn.getId(), last.getId())) {
                assertTrue(last.getCreatedOn().isAfter(checkIn.getCreatedOn()));
            }
        }
    }

    @DisplayName("should return checkIn on getModelName()")
    @Test
    void getModelNameShouldReturnCheckIn() {
        assertEquals(CHECK_IN, checkInPersistenceService.getModelName());
    }
}
