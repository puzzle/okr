package ch.puzzle.okr.service.business;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import ch.puzzle.okr.service.validation.CheckInValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInBusinessServiceTest {
    TestHelper testHelper = new TestHelper();
    @MockBean
    CheckInPersistenceService checkInPersistenceService = Mockito.mock(CheckInPersistenceService.class);
    @MockBean
    CheckInValidationService validator = Mockito.mock(CheckInValidationService.class);
    @MockBean
    UserBusinessService userBusinessService = Mockito.mock(UserBusinessService.class);
    @InjectMocks
    private CheckInBusinessService checkInBusinessService;

    /* Test data */
    KeyResult ordinalKeyResult;
    KeyResult metricKeyResult;
    private CheckIn checkInMetric;
    private CheckIn checkInOrdinal;
    private User user;
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        /* KeyResult definition */
        this.ordinalKeyResult = KeyResultOrdinal.Builder.builder().withCommitZone("Baum").withStretchZone("Wald")
                .withId(7L).withTitle("Keyresult Ordinal").build();
        this.metricKeyResult = KeyResultMetric.Builder.builder().withBaseline(10D).withStretchGoal(50D)
                .withUnit(Unit.CHF.toString()).withId(8L).withTitle("Keyresult Metric").build();

        /* CheckIn definition */
        this.checkInMetric = CheckInMetric.Builder.builder().withValue(30D).withId(1L).withConfidence(5)
                .withChangeInfo("ChangeInfo1").withInitiatives("Initiatives1").withCreatedBy(user)
                .withKeyResult(metricKeyResult).build();
        this.checkInOrdinal = CheckInOrdinal.Builder.builder().withZone(Zone.COMMIT).withConfidence(5)
                .withChangeInfo("ChangeInfo2").withInitiatives("Initiatives2").withCreatedBy(user)
                .withKeyResult(ordinalKeyResult).build();

        this.user = User.Builder.builder().withEmail("Email").withFirstname("Firstname").withLastname("Lastname")
                .build();
        this.jwt = testHelper.mockJwtToken("johnny", "Johnny", "Appleseed", "test@test.ch");
    }

    @Test
    void shouldGetMetricCheckIn() {
        when(checkInPersistenceService.findById(this.checkInMetric.getId())).thenReturn(this.checkInMetric);
        CheckIn checkIn = checkInBusinessService.getCheckInById(this.checkInMetric.getId());

        assertEquals(this.checkInMetric.getCheckInType(), checkIn.getCheckInType());
        assertEquals(this.checkInMetric.getChangeInfo(), checkIn.getChangeInfo());
        assertEquals(this.checkInMetric.getInitiatives(), checkIn.getInitiatives());
        assertEquals(metricKeyResult.getId(), checkIn.getKeyResult().getId());
    }

    @Test
    void shouldGetOrdinalCheckIn() {
        when(checkInPersistenceService.findById(this.checkInOrdinal.getId())).thenReturn(this.checkInOrdinal);
        CheckIn checkIn = checkInBusinessService.getCheckInById(this.checkInOrdinal.getId());

        assertEquals(this.checkInOrdinal.getCheckInType(), checkIn.getCheckInType());
        assertEquals(this.checkInOrdinal.getChangeInfo(), checkIn.getChangeInfo());
        assertEquals(this.checkInOrdinal.getInitiatives(), checkIn.getInitiatives());
        assertEquals(ordinalKeyResult.getId(), checkIn.getKeyResult().getId());
    }

    @Test
    void shouldUpdateCheckInMetricWhenNoTypeChange() {
        when(checkInPersistenceService.findById(this.checkInMetric.getId())).thenReturn(this.checkInMetric);
        when(checkInPersistenceService.save(any())).thenReturn(this.checkInMetric);

        CheckIn newCheckIn = checkInBusinessService.updateCheckIn(this.checkInMetric.getId(), this.checkInMetric);

        verify(checkInPersistenceService, times(1)).save(this.checkInMetric);
        assertEquals(this.checkInMetric.getId(), newCheckIn.getId());
        assertEquals(this.checkInMetric.getChangeInfo(), newCheckIn.getChangeInfo());
        assertEquals(this.checkInMetric.getCheckInType(), newCheckIn.getCheckInType());
    }

    @Test
    void shouldUpdateCheckInOrdinalWhenNoTypeChange() {
        when(checkInPersistenceService.findById(this.checkInOrdinal.getId())).thenReturn(this.checkInOrdinal);
        when(checkInPersistenceService.save(any())).thenReturn(this.checkInOrdinal);

        CheckIn newCheckIn = checkInBusinessService.updateCheckIn(this.checkInOrdinal.getId(), this.checkInOrdinal);

        verify(checkInPersistenceService, times(1)).save(this.checkInOrdinal);
        assertEquals(this.checkInOrdinal.getId(), newCheckIn.getId());
        assertEquals(this.checkInOrdinal.getChangeInfo(), newCheckIn.getChangeInfo());
        assertEquals(this.checkInOrdinal.getCheckInType(), newCheckIn.getCheckInType());
    }

    @Test
    void shouldSaveMetricCheckIn() {
        when(checkInPersistenceService.save(any())).thenReturn(this.checkInMetric);
        CheckIn createdCheckIn = checkInBusinessService.createCheckIn(this.checkInMetric, this.jwt);

        verify(checkInPersistenceService, times(1)).save(this.checkInMetric);
        assertEquals(this.checkInMetric.getId(), createdCheckIn.getId());
        assertEquals(this.checkInMetric.getChangeInfo(), createdCheckIn.getChangeInfo());
        assertEquals(this.checkInMetric.getCheckInType(), createdCheckIn.getCheckInType());
        assertEquals(((CheckInMetric) this.checkInMetric).getValue(), ((CheckInMetric) createdCheckIn).getValue());
    }

    @Test
    void shouldSaveOrdinalCheckIn() {
        when(checkInPersistenceService.save(any())).thenReturn(this.checkInOrdinal);
        CheckIn createdCheckIn = checkInBusinessService.createCheckIn(this.checkInOrdinal, this.jwt);

        verify(checkInPersistenceService, times(1)).save(this.checkInOrdinal);
        assertEquals(this.checkInOrdinal.getId(), createdCheckIn.getId());
        assertEquals(this.checkInOrdinal.getChangeInfo(), createdCheckIn.getChangeInfo());
        assertEquals(this.checkInOrdinal.getCheckInType(), createdCheckIn.getCheckInType());
        assertEquals(((CheckInOrdinal) this.checkInOrdinal).getZone(), ((CheckInOrdinal) createdCheckIn).getZone());
    }

    @Test
    void shouldDeleteCheckIn() {
        checkInBusinessService.deleteCheckIn(this.checkInOrdinal.getId());
        verify(checkInPersistenceService, times(1)).deleteById(this.checkInOrdinal.getId());
    }

    @Test
    void shouldGetCheckInsByKeyResultId() {
        checkInBusinessService.getCheckInsByKeyResultId(this.metricKeyResult.getId());
        verify(checkInPersistenceService, times(1))
                .getCheckInsByKeyResultIdOrderByCheckInDateDesc(this.metricKeyResult.getId());
    }

    @Test
    void shouldGetLastCheckInByKeyResultId() {
        checkInBusinessService.getLastCheckInByKeyResultId(this.ordinalKeyResult.getId());
        verify(checkInPersistenceService, times(1)).getLastCheckInOfKeyResult(this.ordinalKeyResult.getId());
    }
}
