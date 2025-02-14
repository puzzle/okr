package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.Constants.CHECK_IN;
import static ch.puzzle.okr.mapper.keyresult.helper.TestDataConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.repository.CheckInRepository;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringIntegrationTest
class CheckInPersistenceServiceIT {

    private static final long KEY_RESULT_ID = 7L;

    @Autowired
    private CheckInPersistenceService checkInPersistenceService;

    @MockitoSpyBean
    private CheckInRepository checkInRepository;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @AfterEach
    void tearDown() {
        TenantContext.setCurrentTenant(null);
    }

    // uses data from V100_0_0__TestData.sql
    @DisplayName("Should get checkIns by keyResultId and order them by date desc on getCheckInsByKeyResultIdOrderByCheckInDate() ")
    @Test
    void shouldGetCheckInsByKeyResultIdAndOrderThemByDateDesc() {
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
    @DisplayName("Should get last checkIn of keyResult on getLastCheckInOfKeyResult()")
    @Test
    void shouldGetLastCheckInOfKeyResult() {
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

    @DisplayName("Should return checkIn on getModelName()")
    @Test
    void shouldReturnCheckIn() {
        assertEquals(CHECK_IN, checkInPersistenceService.getModelName());
    }

    @DisplayName("Should mark as deleted on deleteById() per default")
    @Test
    void shouldMarkAsDeletedOnMethodCall() {
        // arrange
        CheckInOrdinal checkInOrdinal = (CheckInOrdinal) CheckInOrdinal.Builder
                .builder() //
                .withKeyResult(KeyResultOrdinal.Builder.builder().withId(8L).build()) //
                .withCreatedBy(User.Builder.builder().withId(11L).build()) //
                .withVersion(CHECK_IN_VERSION) //
                .withZone(CHECK_IN_ORDINAL_ZONE) //
                .withConfidence(CHECK_IN_CONFIDENCE) //
                .withCreatedOn(CHECK_IN_CREATED_ON) //
                .withChangeInfo(CHECK_IN_CHANGE_INFO) //
                .withInitiatives(CHECK_IN_INITIATIVES) //
                .build();
        var newEntity = checkInPersistenceService.save(checkInOrdinal);

        long entityId = newEntity.getId();

        // act
        checkInPersistenceService.deleteById(entityId);

        // assert
        assertTrue(checkInPersistenceService.findById(entityId).isDeleted());
        Mockito.verify(checkInRepository, Mockito.times(1)).markAsDeleted(entityId);
    }
}
