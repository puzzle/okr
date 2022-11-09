package ch.puzzle.okr.service;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeyResultServiceTest {
    @Mock
    KeyResultRepository keyResultRepository;
    @Mock
    MeasureRepository measureRepository;
    List<KeyResult> keyResults;
    @InjectMocks
    private KeyResultService keyResultService;
    private User user;
    private Objective objective;
    private Quarter quarter;
    private KeyResult keyResult;
    private List<Measure> measures;

    @BeforeEach
    void setup() {
        this.user = User.Builder.builder()
                .withId(1L)
                .withEmail("newMail@tese.com")
                .build();

        this.objective = Objective.Builder.builder()
                .withId(5L)
                .withTitle("Objective 1")
                .build();

        this.quarter = Quarter.Builder.builder()
                .withId(5L)
                .withNumber(2)
                .withYear(2022)
                .build();

        this.keyResult = KeyResult.Builder.builder()
                .withId(5L)
                .withTitle("Keyresult 1")
                .withObjective(this.objective)
                .withOwner(this.user)
                .withQuarter(this.quarter)
                .build();

        Measure measure = Measure.Builder.builder().withId(1L).withKeyResult(keyResult).withCreatedBy(user).build();

        this.keyResults = List.of(keyResult, keyResult, keyResult);
        this.measures = List.of(measure, measure, measure);
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
        assertThrows(ResponseStatusException.class, () ->
                keyResultService.getKeyResultById(1));
    }

    @Test
    void shouldEditKeyresult() {
        KeyResult newKeyresult = KeyResult.Builder
                .builder()
                .withId(1L).withTitle("Keyresult 1 update")
                .build();
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
        assertThrows(ResponseStatusException.class, () ->
                keyResultService.getAllMeasuresByKeyResult(1));
    }


}
