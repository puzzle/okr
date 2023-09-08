package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.User;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
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

    @InjectMocks
    private CheckInBusinessService checkInBusinessService;

    private Measure measure;
    private Measure measureWithId;
    private List<Measure> measures = new ArrayList<>();
    private Measure falseMeasure;
    KeyResult ordinalKeyResult;
    private User user;

    @BeforeEach
    void setUp() {
        this.ordinalKeyResult = KeyResultOrdinal.Builder.builder().withCommitZone("Baum").withStretchZone("Wald")
                .withId(7L).withTitle("Keyresult Ordinal").build();

        this.measure = Measure.Builder.builder()
                .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
                .withCreatedOn(LocalDateTime.MAX).withKeyResult(this.ordinalKeyResult).withValue(30D)
                .withChangeInfo("ChangeInfo").withInitiatives("Initiatives")
                .withMeasureDate(Instant.parse("2021-11-03T00:00:00.00Z")).build();
        this.measureWithId = Measure.Builder.builder().withId(1L)
                .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
                .withCreatedOn(LocalDateTime.MAX).withKeyResult(this.ordinalKeyResult).withValue(30D)
                .withChangeInfo("ChangeInfo").withInitiatives("Initiatives")
                .withMeasureDate(Instant.parse("2021-11-03T00:00:00.00Z")).build();
        this.user = User.Builder.builder().withEmail("Email").withFirstname("Firstname").withLastname("Lastname")
                .build();
        this.measures.add(measureWithId);
    }

    @Test
    void shouldReturnMeasure() {
        Mockito.when(checkInPersistenceService.save(any())).thenReturn(measure);
        Mockito.when(userBusinessService.getUserByAuthorisationToken(any())).thenReturn(user);

        Measure returnMeasure = this.checkInBusinessService.saveMeasure(measure, any());
        assertEquals(returnMeasure.getValue(), 30);
        assertEquals(returnMeasure.getChangeInfo(), "ChangeInfo");
        assertEquals(returnMeasure.getInitiatives(), "Initiatives");
        assertEquals(returnMeasure.getKeyResult().getId(), 7);
        assertEquals(returnMeasure.getCreatedBy().getFirstname(), "Frank");
    }

    @Test
    void shouldThrowErrorWhenInDbIsSameMeasureDateWithSameKeyResultId() {
        Mockito.when(checkInPersistenceService.getMeasuresByKeyResultIdAndMeasureDate(any(), any()))
                .thenReturn(measures);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> this.checkInBusinessService.saveMeasure(measure));
        assertEquals(400, exception.getRawStatusCode());
        assertEquals("Only one Messung is allowed per day and Key Result!", exception.getReason());
    }

    @Test
    void shouldNotReturnException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInBusinessService.saveMeasure(falseMeasure));
        assertEquals(400, exception.getRawStatusCode());
        assertEquals("Measure has already an Id", exception.getReason());
    }

    @Test
    void shouldReturnCorrectEntity() {
        Mockito.when(checkInPersistenceService.save(any())).thenReturn(measure);
        Mockito.when(userBusinessService.getUserByAuthorisationToken(any())).thenReturn(user);

        Measure returnedMeasure = this.checkInBusinessService.updateMeasure(1L, measure, any());

        assertEquals(measure.getId(), returnedMeasure.getId());
        assertEquals(measure.getValue(), returnedMeasure.getValue());
        assertEquals(measure.getChangeInfo(), returnedMeasure.getChangeInfo());
        assertEquals(measure.getMeasureDate(), returnedMeasure.getMeasureDate());
    }

    @Test
    void shouldThrowResponseStatusExceptionBadRequest() {
        Mockito.when(checkInPersistenceService.save(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> this.checkInBusinessService.updateMeasure(1L, measure, any()));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
