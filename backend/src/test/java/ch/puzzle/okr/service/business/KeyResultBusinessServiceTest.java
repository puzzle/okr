package ch.puzzle.okr.service.business;

import ch.puzzle.okr.dto.KeyResultMeasureDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.KeyResultMeasureMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.keyResult.KeyResult;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.MeasurePersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeyResultBusinessServiceTest {
    @MockBean
    KeyResultPersistenceService keyResultPersistenceService = Mockito.mock(KeyResultPersistenceService.class);
    @MockBean
    ObjectivePersistenceService objectivePersistenceService = Mockito.mock(ObjectivePersistenceService.class);
    @MockBean
    MeasurePersistenceService measurePersistenceService = Mockito.mock(MeasurePersistenceService.class);
    @MockBean
    KeyResultMeasureMapper keyResultMeasureMapper = Mockito.mock(KeyResultMeasureMapper.class);
    List<KeyResult> keyResults;
    User user;
    Objective objective;
    KeyResult keyResult;
    Measure measure1;
    Measure measure2;
    Measure measure3;
    List<Measure> measures;
    @InjectMocks
    private KeyResultBusinessService keyResultBusinessService;

    @BeforeEach
    void setup() {
        this.user = User.Builder.builder().withId(1L).withEmail("newMail@tese.com").build();

        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();

        this.keyResult = KeyResult.Builder.builder().withId(5L).withUnit(Unit.PERCENT).withTitle("Keyresult 1")
                .withObjective(this.objective).withOwner(this.user).build();

        measure1 = Measure.Builder.builder().withId(1L).withKeyResult(keyResult).withCreatedBy(user).build();
        measure2 = Measure.Builder.builder().withId(2L).withKeyResult(keyResult).withCreatedBy(user).build();
        measure3 = Measure.Builder.builder().withId(3L).withKeyResult(keyResult).withCreatedBy(user).build();
        this.keyResults = List.of(keyResult, keyResult, keyResult);
        this.measures = List.of(measure1, measure2, measure3);
    }

    @Test
    void shouldGetKeyResultById() {
        when(keyResultPersistenceService.getKeyResultById(1L)).thenReturn(keyResult);
        KeyResult keyResult = keyResultBusinessService.getKeyResultById(1L);

        assertEquals("Keyresult 1", keyResult.getTitle());
        assertEquals(5, keyResult.getId());
    }

    @Test
    void shouldEditKeyresult() {
        KeyResult newKeyresult = KeyResult.Builder.builder().withId(1L).withTitle("Keyresult 1 update").build();
        Mockito.when(keyResultPersistenceService.updateKeyResult(any())).thenReturn(newKeyresult);
        Mockito.when(keyResultPersistenceService.getKeyResultById(1L)).thenReturn(keyResult);

        keyResultBusinessService.updateKeyResult(newKeyresult);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult 1 update", newKeyresult.getTitle());
    }

    @Test
    void saveKeyResult() {
        Mockito.when(keyResultPersistenceService.saveKeyResult(any())).thenReturn(keyResult);
        KeyResult savedKeyResult = keyResultBusinessService.saveKeyResult(keyResult);
        assertEquals("Keyresult 1", savedKeyResult.getTitle());
    }

    @Test
    void shouldBePossibleToSaveKeyResultWithoutDescription() {
        Mockito.when(this.keyResultPersistenceService.saveKeyResult(any())).thenReturn(this.keyResult);
        this.keyResult.setDescription("");
        KeyResult keyResult = this.keyResultBusinessService.saveKeyResult(this.keyResult);
        assertEquals("Keyresult 1", keyResult.getTitle());
        assertEquals("", keyResult.getDescription());
    }

    @Test
    void shouldGetAllKeyresultsByObjective() {
        when(objectivePersistenceService.findById(1L)).thenReturn(objective);
        when(keyResultPersistenceService.getKeyResultsByObjective(any())).thenReturn(keyResults);

        List<KeyResult> keyResultList = keyResultBusinessService.getAllKeyResultsByObjective(1L);

        assertEquals(3, keyResultList.size());
        assertEquals("Keyresult 1", keyResultList.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNoKeyResultInObjective() {
        when(objectivePersistenceService.findById(1L)).thenReturn(objective);
        when(keyResultPersistenceService.getKeyResultsByObjective(any())).thenReturn(Collections.emptyList());

        List<KeyResult> keyResultList = keyResultBusinessService.getAllKeyResultsByObjective(1L);

        assertEquals(0, keyResultList.size());
    }

    @Test
    void shouldThrowExceptionWhenObjectiveDoesntExist() {
        when(objectivePersistenceService.findById(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective with id 1 not found"));
        assertThrows(ResponseStatusException.class, () -> keyResultBusinessService.getAllKeyResultsByObjective(1L));
    }

    @Test
    void shouldGetAllMeasuresByKeyResult() {
        when(keyResultPersistenceService.getKeyResultById(1L)).thenReturn(keyResult);
        when(measurePersistenceService.getMeasuresByKeyResultIdOrderByMeasureDateDesc(any())).thenReturn(measures);

        List<Measure> measureList = keyResultBusinessService.getAllMeasuresByKeyResult(1);

        assertEquals(3, measureList.size());
        assertEquals(1, measureList.get(0).getId());
        assertEquals("Keyresult 1", measureList.get(0).getKeyResult().getTitle());
        assertEquals("Objective 1", measureList.get(0).getKeyResult().getObjective().getTitle());
        assertEquals("newMail@tese.com", measureList.get(0).getCreatedBy().getEmail());
    }

    @Test
    void shouldReturnEmptyListWhenNoMeasuresInKeyResult() {
        when(keyResultPersistenceService.getKeyResultById(1L)).thenReturn(keyResult);
        when(measurePersistenceService.getMeasuresByKeyResultIdOrderByMeasureDateDesc(any()))
                .thenReturn(Collections.emptyList());

        List<Measure> measureList = keyResultBusinessService.getAllMeasuresByKeyResult(1);

        assertEquals(0, measureList.size());
    }

    @Test
    void shouldThrowExceptionWhenGetMeasuresFromNonExistingKeyResult() {
        when(keyResultPersistenceService.getKeyResultById(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 1 not found"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultBusinessService.getAllMeasuresByKeyResult(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("KeyResult with id 1 not found", exception.getReason());
    }

    @Test
    void shouldGetAllKeyResultsFromObjectiveWithMeasure() {
        when(objectivePersistenceService.findById(any())).thenReturn(objective);
        when(measurePersistenceService.getLastMeasuresOfKeyresults(any())).thenReturn(measures);
        when(keyResultPersistenceService.getKeyResultsByObjective(any())).thenReturn(keyResults);
        when(keyResultMeasureMapper.toDto(keyResult, measure1)).thenReturn(new KeyResultMeasureDto(5L, 1L,
                "Keyresult 1", "Description", 1L, "Paco", "Egiman", ExpectedEvolution.CONSTANT, Unit.PERCENT, 20D, 100D,
                new MeasureDto(1L, 1L, 10D, "", "", 1L, null, null), 0L));

        List<KeyResultMeasureDto> keyResultList = keyResultBusinessService.getAllKeyResultsByObjectiveWithMeasure(1L);

        assertEquals(3, keyResultList.size());
        assertEquals("Keyresult 1", keyResultList.get(0).title());
        assertEquals(1, keyResultList.get(0).measure().id());
        assertEquals(1, keyResultList.get(0).objectiveId());
    }

    @Test
    void shouldReturnNullObjectWhenMeasureIsNull() {
        when(objectivePersistenceService.findById(any())).thenReturn(objective);
        when(measurePersistenceService.getLastMeasuresOfKeyresults(any())).thenReturn(measures);
        when(keyResultPersistenceService.getKeyResultsByObjective(any())).thenReturn(keyResults);
        when(keyResultMeasureMapper.toDto(any(), any())).thenReturn(new KeyResultMeasureDto(5L, 1L, "Keyresult 1",
                "Description", 1L, "Paco", "Egiman", ExpectedEvolution.CONSTANT, Unit.PERCENT, 20D, 100D, null, 0L));

        List<KeyResultMeasureDto> keyResultList = keyResultBusinessService.getAllKeyResultsByObjectiveWithMeasure(1L);

        assertEquals(3, keyResultList.size());
        assertNull(keyResultList.get(0).measure());
    }

}
