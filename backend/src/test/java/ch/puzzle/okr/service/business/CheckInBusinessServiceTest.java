package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInMetric;
import ch.puzzle.okr.models.keyresult.KeyResult;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CheckInBusinessServiceTest {
    @MockBean
    CheckInPersistenceService checkInPersistenceService = Mockito.mock(CheckInPersistenceService.class);
    @MockBean
    CheckInValidationService validator = Mockito.mock(CheckInValidationService.class);
    @MockBean
    UserBusinessService userBusinessService = Mockito.mock(UserBusinessService.class);
    KeyResult ordinalKeyResult;
    @InjectMocks
    private CheckInBusinessService checkInBusinessService;
    private CheckIn checkIn;
    private CheckIn checkInWithId;
    private List<CheckIn> checkIns = new ArrayList<>();
    private CheckIn falseCheckIn;
    private User user;

    @BeforeEach
    void setUp() {
        this.ordinalKeyResult = KeyResultOrdinal.Builder.builder().withCommitZone("Baum").withStretchZone("Wald")
                .withId(7L).withTitle("Keyresult Ordinal").build();

        this.checkIn = CheckInMetric.Builder.builder().withValue(30D).withCheckInType("Metric").withConfidence(5)
                .withChangeInfo("ChangeInfo").withInitiatives("Initiatives").withCreatedBy(user)
                .withKeyResult(ordinalKeyResult).build();
        this.checkInWithId = this.checkIn = CheckInMetric.Builder.builder().withValue(30D).withId(1L)
                .withCheckInType("Metric").withConfidence(5).withChangeInfo("ChangeInfo").withInitiatives("Initiatives")
                .withCreatedBy(user).withKeyResult(ordinalKeyResult).build();
        this.user = User.Builder.builder().withEmail("Email").withFirstname("Firstname").withLastname("Lastname")
                .build();
        this.checkIns.add(checkInWithId);
    }

    @Test
    void shouldReturnCheckIn() {
        Mockito.when(checkInPersistenceService.save(any())).thenReturn(checkIn);
        Mockito.when(userBusinessService.getUserByAuthorisationToken(any())).thenReturn(user);

        CheckIn returnCheckIn = this.checkInBusinessService.saveCheckIn(checkIn, any());
        assertEquals(((CheckInMetric) returnCheckIn).getValue(), 30D);
        assertEquals(returnCheckIn.getChangeInfo(), "ChangeInfo");
        assertEquals(returnCheckIn.getInitiatives(), "Initiatives");
        assertEquals(returnCheckIn.getKeyResult().getId(), 7);
        assertEquals(returnCheckIn.getCreatedBy().getFirstname(), "Frank");
    }

    @Test
    void shouldNotReturnException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInBusinessService.saveCheckIn(falseCheckIn, Jwt.withTokenValue(user.toString()).build()));
        assertEquals(400, exception.getRawStatusCode());
        assertEquals("CheckIn has already an Id", exception.getReason());
    }

    @Test
    void shouldReturnCorrectEntity() {
        Mockito.when(checkInPersistenceService.save(any())).thenReturn(checkIn);
        Mockito.when(userBusinessService.getUserByAuthorisationToken(any())).thenReturn(user);

        CheckIn returnedCheckIn = this.checkInBusinessService.updateCheckIn(1L, checkIn, any());

        assertEquals(checkIn.getId(), returnedCheckIn.getId());
        assertEquals(((CheckInMetric) checkIn).getValue(), ((CheckInMetric) returnedCheckIn).getValue());
        assertEquals(checkIn.getChangeInfo(), returnedCheckIn.getChangeInfo());
        assertEquals(checkIn.getModifiedOn(), returnedCheckIn.getModifiedOn());
    }

    @Test
    void shouldThrowResponseStatusExceptionBadRequest() {
        Mockito.when(checkInPersistenceService.save(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> this.checkInBusinessService.updateCheckIn(1L, checkIn, any()));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
