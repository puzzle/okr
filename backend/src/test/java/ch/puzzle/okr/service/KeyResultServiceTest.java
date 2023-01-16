package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.KeyResultMeasureDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.KeyResultMeasureMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyResultServiceTest {
    @MockBean
    KeyResultRepository keyResultRepository = Mockito.mock(KeyResultRepository.class);
    @MockBean
    ObjectiveRepository objectiveRepository = Mockito.mock(ObjectiveRepository.class);
    @MockBean
    MeasureRepository measureRepository = Mockito.mock(MeasureRepository.class);
    @MockBean
    KeyResultMeasureMapper keyResultMeasureMapper = Mockito.mock(KeyResultMeasureMapper.class);
    @MockBean
    ProgressService progressService = Mockito.mock(ProgressService.class);
    List<KeyResult> keyResults;
    User user;
    Objective objective;
    Quarter quarter;
    KeyResult keyResult;
    Measure measure1;
    Measure measure2;
    Measure measure3;
    List<Measure> measures;
    @InjectMocks
    private KeyResultService keyResultService;

    @BeforeEach
    void setup() {
        this.user = User.Builder.builder().withId(1L).withEmail("newMail@tese.com").build();

        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();

        this.quarter = Quarter.Builder.builder().withId(5L).withLabel("GJ 22/23-Q2").build();

        this.keyResult = KeyResult.Builder.builder().withId(5L).withTitle("Keyresult 1").withObjective(this.objective)
                .withOwner(this.user).build();

        measure1 = Measure.Builder.builder().withId(1L).withKeyResult(keyResult).withCreatedBy(user).build();
        measure2 = Measure.Builder.builder().withId(2L).withKeyResult(keyResult).withCreatedBy(user).build();
        measure3 = Measure.Builder.builder().withId(3L).withKeyResult(keyResult).withCreatedBy(user).build();
        this.keyResults = List.of(keyResult, keyResult, keyResult);
        this.measures = List.of(measure1, measure2, measure3);
    }

    @Test
    void shouldGetKeyResultById() {
        when(keyResultRepository.findById(1L)).thenReturn(Optional.of(keyResult));
        KeyResult keyResult = keyResultService.getKeyResultById(1);

        assertEquals("Keyresult 1", keyResult.getTitle());
        assertEquals(5, keyResult.getId());
    }

    @Test
    void shouldThrowExceptionWhenKeyResultDoesntExist() {
        assertThrows(ResponseStatusException.class, () -> keyResultService.getKeyResultById(1));
    }

    @Test
    void shouldEditKeyresult() {
        KeyResult newKeyresult = KeyResult.Builder.builder().withId(1L).withTitle("Keyresult 1 update").build();
        Mockito.when(keyResultRepository.save(any())).thenReturn(newKeyresult);
        Mockito.when(keyResultRepository.findById(1L)).thenReturn(Optional.of(keyResult));

        keyResultService.updateKeyResult(newKeyresult);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult 1 update", newKeyresult.getTitle());
    }

    @Test
    void shouldThrowErrorWhenKeyResultDoesntExistDuringPut() {
        Mockito.when(keyResultRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> keyResultService.updateKeyResult(keyResult));
    }

    @Test
    void createKeyResult() {
        Mockito.when(this.keyResultRepository.save(any())).thenReturn(this.keyResult);
        KeyResult keyResult = this.keyResultService.createKeyResult(this.keyResult);
        assertEquals("Keyresult 1", keyResult.getTitle());
    }

    @Test
    void shouldGetAllKeyresultsByObjective() {
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objective));
        when(keyResultRepository.findByObjective(any())).thenReturn(keyResults);

        List<KeyResult> keyResultList = keyResultService.getAllKeyResultsByObjective(1);

        assertEquals(3, keyResultList.size());
        assertEquals("Keyresult 1", keyResultList.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNoKeyResultInObjective() {
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objective));
        when(keyResultRepository.findByObjective(any())).thenReturn(Collections.emptyList());

        List<KeyResult> keyResultList = keyResultService.getAllKeyResultsByObjective(1);

        assertEquals(0, keyResultList.size());
    }

    @Test
    void shouldThrowExceptionWhenObjectiveDoesntExist() {
        assertThrows(ResponseStatusException.class, () -> keyResultService.getAllKeyResultsByObjective(1));
    }

    @Test
    void shouldGetAllMeasuresByKeyResult() {
        when(keyResultRepository.findById(1L)).thenReturn(Optional.of(keyResult));
        when(measureRepository.findByKeyResult(any())).thenReturn(measures);

        List<Measure> measureList = keyResultService.getAllMeasuresByKeyResult(1);

        assertEquals(3, measureList.size());
        assertEquals(1, measureList.get(0).getId());
        assertEquals("Keyresult 1", measureList.get(0).getKeyResult().getTitle());
        assertEquals("Objective 1", measureList.get(0).getKeyResult().getObjective().getTitle());
        assertEquals("newMail@tese.com", measureList.get(0).getCreatedBy().getEmail());
    }

    @Test
    void shouldReturnEmptyListWhenNoMeasuresInKeyResult() {
        when(keyResultRepository.findById(1L)).thenReturn(Optional.of(keyResult));
        when(measureRepository.findByKeyResult(any())).thenReturn(Collections.emptyList());

        List<Measure> measureList = keyResultService.getAllMeasuresByKeyResult(1);

        assertEquals(0, measureList.size());
    }

    @Test
    void shouldThrowExceptionWhenGetMeasuresFromNonExistingKeyResult() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultService.getAllMeasuresByKeyResult(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("KeyResult with id 1 not found", exception.getReason());
    }

    @Test
    void shouldGetAllKeyResultsFromObjectiveWithMeasure() {
        when(objectiveRepository.findById(any())).thenReturn(Optional.of(objective));
        when(measureRepository.findLastMeasuresOfKeyresults(any())).thenReturn(measures);
        when(keyResultRepository.findByObjective(any())).thenReturn(keyResults);
        when(keyResultMeasureMapper.toDto(keyResult, measure1)).thenReturn(new KeyResultMeasureDto(5L, 1L,
                "Keyresult 1", "Description", 1L, "Paco", "Egiman", ExpectedEvolution.CONSTANT, Unit.PERCENT, 20L, 100L,
                new MeasureDto(1L, 1L, 10, "", "", 1L, null, null), 0L));

        List<KeyResultMeasureDto> keyResultList = keyResultService.getAllKeyResultsByObjectiveWithMeasure(1L);

        assertEquals(3, keyResultList.size());
        assertEquals("Keyresult 1", keyResultList.get(0).getTitle());
        assertEquals(1, keyResultList.get(0).getMeasure().getId());
        assertEquals(1, keyResultList.get(0).getObjectiveId());
    }

    @Test
    void shouldReturnNullObjectWhenMeasureIsNull() {
        when(objectiveRepository.findById(any())).thenReturn(Optional.of(objective));
        when(measureRepository.findLastMeasuresOfKeyresults(any())).thenReturn(measures);
        when(keyResultRepository.findByObjective(any())).thenReturn(keyResults);
        when(keyResultMeasureMapper.toDto(any(), any())).thenReturn(new KeyResultMeasureDto(5L, 1L, "Keyresult 1",
                "Description", 1L, "Paco", "Egiman", ExpectedEvolution.CONSTANT, Unit.PERCENT, 20L, 100L, null, 0L));

        List<KeyResultMeasureDto> keyResultList = keyResultService.getAllKeyResultsByObjectiveWithMeasure(1L);

        assertEquals(3, keyResultList.size());
        assertNull(keyResultList.get(0).getMeasure());
    }

    @Test
    void shouldDeleteKeyResultAndAssociatedMeasures() {
        when(measureRepository.findByKeyResult(any())).thenReturn(measures);
        when(keyResultRepository.findById(1L)).thenReturn(Optional.of(keyResult));

        keyResultService.deleteKeyResultById(1L);

        verify(keyResultRepository, times(1)).deleteById(1L);
        verify(measureRepository, times(1)).findByKeyResult(keyResult);
        verify(measureRepository, times(1)).deleteById(1L);
        verify(measureRepository, times(1)).deleteById(2L);
        verify(measureRepository, times(1)).deleteById(3L);
        verify(progressService, times(1)).updateObjectiveProgress(any());
    }
}
