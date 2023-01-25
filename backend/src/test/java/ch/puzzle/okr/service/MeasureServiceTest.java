package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.MeasureRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MeasureServiceTest {
    @MockBean
    MeasureRepository measureRepository = Mockito.mock(MeasureRepository.class);

    @MockBean
    ProgressService progressService = Mockito.mock(ProgressService.class);

    @InjectMocks
    private MeasureService measureService;

    private Measure measure;
    private Measure measureWithId;
    private List<Measure> measures = new ArrayList<>();
    private Measure falseMeasureDate;
    private Measure falseMeasure;

    @BeforeEach
    void setUp() {
        this.measure = Measure.Builder.builder()
                .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
                .withCreatedOn(LocalDateTime.MAX)
                .withKeyResult(KeyResult.Builder.builder().withId(8L).withBasisValue(12D).withTargetValue(50D)
                        .withObjective(Objective.Builder.builder().withId(1L).build()).build())
                .withValue(30D).withChangeInfo("ChangeInfo").withInitiatives("Initiatives")
                .withMeasureDate(Instant.parse("2021-11-03T23:00:00.00Z")).build();
        this.measureWithId = Measure.Builder.builder().withId(1L)
                .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
                .withCreatedOn(LocalDateTime.MAX)
                .withKeyResult(KeyResult.Builder.builder().withId(8L).withBasisValue(12L).withTargetValue(50L)
                        .withObjective(Objective.Builder.builder().withId(1L).build()).build())
                .withValue(30).withChangeInfo("ChangeInfo").withInitiatives("Initiatives")
                .withMeasureDate(Instant.parse("2021-11-03T23:00:00.00Z")).build();
        this.falseMeasure = Measure.Builder.builder().withId(3L).build();
        this.falseMeasureDate = Measure.Builder.builder()
                .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
                .withCreatedOn(LocalDateTime.MAX)
                .withKeyResult(KeyResult.Builder.builder().withId(8L).withBasisValue(12D).withTargetValue(50D)
                        .withObjective(Objective.Builder.builder().withId(1L).build()).build())
                .withValue(30D).withChangeInfo("ChangeInfo").withInitiatives("Initiatives")
                .withMeasureDate(Instant.parse("2021-11-03T00:00:00.00Z")).build();


        this.measures.add(measureWithId);
    }

    @Test
    void shouldReturnMeasure() {
        Mockito.when(measureRepository.save(any())).thenReturn(measure);
        Measure returnMeasure = this.measureService.saveMeasure(measure);
        assertEquals(returnMeasure.getValue(), 30);
        assertEquals(returnMeasure.getChangeInfo(), "ChangeInfo");
        assertEquals(returnMeasure.getInitiatives(), "Initiatives");
        assertEquals(returnMeasure.getKeyResult().getId(), 8);
        assertEquals(returnMeasure.getCreatedBy().getFirstname(), "Frank");
    }

    @Test
    void shouldThrowErrorWhenInDbIsSameMeasureDateWithSameKeyResultId() {
        Mockito.when(measureRepository.findMeasuresByKeyResultIdAndMeasureDate(any(), any())).thenReturn(measures);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> this.measureService.saveMeasure(measure));
        assertEquals(400, exception.getRawStatusCode());
        assertEquals("Only one Messung is allowed per day and Key Result!", exception.getReason());
    }

    @Test
    void shouldNotReturnException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> measureService.saveMeasure(falseMeasure));
        assertEquals(400, exception.getRawStatusCode());
        assertEquals("Measure has already an Id", exception.getReason());
    }

    @Test
    void shouldReturnCorrectEntity() {
        Mockito.when(measureRepository.save(any())).thenReturn(measure);
        Mockito.when(measureRepository.findById(anyLong())).thenReturn(Optional.ofNullable(measure));
        Measure returnedMeasure = this.measureService.updateMeasure(1L, measure);
        assertEquals(measure.getId(), returnedMeasure.getId());
        assertEquals(measure.getValue(), returnedMeasure.getValue());
        assertEquals(measure.getChangeInfo(), returnedMeasure.getChangeInfo());
        assertEquals(measure.getMeasureDate(), returnedMeasure.getMeasureDate());
    }

    @Test
    void shouldThrowResponseStatusExceptionNotFound() {
        Mockito.when(measureRepository.save(any())).thenReturn(measure);
        Mockito.when(measureRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> this.measureService.updateMeasure(1L, measure));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void shouldThrowResponseStatusExceptionBadRequest() {
        Mockito.when(measureRepository.save(any())).thenReturn(measure);
        Mockito.when(measureService.saveMeasure(measure))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Mockito.when(measureRepository.findById(anyLong())).thenReturn(Optional.ofNullable(measure));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> this.measureService.updateMeasure(1L, measure));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void shouldDeleteMeasure() {
        Mockito.when(measureRepository.findById(1L)).thenReturn(Optional.of(measure));

        measureService.deleteMeasureById(1L);

        verify(measureRepository, times(1)).deleteById(1L);
        verify(measureRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowErrorWhenMeasureNotExistsAndNotCallMeasureRepository() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> this.measureService.deleteMeasureById(33L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(measureRepository, times(0)).deleteById(1L);
    }
}
