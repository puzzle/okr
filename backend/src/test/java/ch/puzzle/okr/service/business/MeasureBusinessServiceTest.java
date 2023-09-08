package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.MeasurePersistenceService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MeasureBusinessServiceTest {
    @MockBean
    MeasurePersistenceService measurePersistenceService = Mockito.mock(MeasurePersistenceService.class);
    @MockBean
    ObjectiveBusinessService objectiveBusinessService = Mockito.mock(ObjectiveBusinessService.class);

    @InjectMocks
    private MeasureBusinessService measureBusinessService;

    private Measure measure;
    private Measure measureWithId;
    private List<Measure> measures = new ArrayList<>();
    private Measure falseMeasure;
    KeyResult ordinalKeyResult;

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
        this.falseMeasure = Measure.Builder.builder().withId(3L).build();

        this.measures.add(measureWithId);
    }

    @Test
    void shouldReturnMeasure() {
        Mockito.when(measurePersistenceService.saveMeasure(any())).thenReturn(measure);
        Measure returnMeasure = this.measureBusinessService.saveMeasure(measure);
        assertEquals(returnMeasure.getValue(), 30);
        assertEquals(returnMeasure.getChangeInfo(), "ChangeInfo");
        assertEquals(returnMeasure.getInitiatives(), "Initiatives");
        assertEquals(returnMeasure.getKeyResult().getId(), 7);
        assertEquals(returnMeasure.getCreatedBy().getFirstname(), "Frank");
    }

    @Test
    void shouldThrowErrorWhenInDbIsSameMeasureDateWithSameKeyResultId() {
        Mockito.when(measurePersistenceService.getMeasuresByKeyResultIdAndMeasureDate(any(), any()))
                .thenReturn(measures);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> this.measureBusinessService.saveMeasure(measure));
        assertEquals(400, exception.getRawStatusCode());
        assertEquals("Only one Messung is allowed per day and Key Result!", exception.getReason());
    }

    @Test
    void shouldNotReturnException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> measureBusinessService.saveMeasure(falseMeasure));
        assertEquals(400, exception.getRawStatusCode());
        assertEquals("Measure has already an Id", exception.getReason());
    }

    @Test
    void shouldReturnCorrectEntity() {
        Mockito.when(measurePersistenceService.updateMeasure(anyLong(), any())).thenReturn(measure);

        Measure returnedMeasure = this.measureBusinessService.updateMeasure(1L, measure);

        assertEquals(measure.getId(), returnedMeasure.getId());
        assertEquals(measure.getValue(), returnedMeasure.getValue());
        assertEquals(measure.getChangeInfo(), returnedMeasure.getChangeInfo());
        assertEquals(measure.getMeasureDate(), returnedMeasure.getMeasureDate());
    }

    @Test
    void shouldThrowResponseStatusExceptionBadRequest() {
        Mockito.when(measurePersistenceService.updateMeasure(anyLong(), any()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> this.measureBusinessService.updateMeasure(1L, measure));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void shouldDeleteMeasure() {
        measureBusinessService.deleteMeasureById(1L);

        verify(measurePersistenceService, times(1)).deleteMeasureById(1L);
    }
}
