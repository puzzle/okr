package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuarterServiceTest {
    @MockBean
    QuarterRepository quarterRepository = Mockito.mock(QuarterRepository.class);

    @InjectMocks
    private QuarterService quarterService;

    @Mock
    Calendar myCal;

    @Mock
    HashMap<Integer, Integer> quarterMap;

    @Test
    void shouldReturnProperQuarter() {
        Quarter quarter = Quarter.Builder.builder().withId(3L).withLabel("GJ 22/23-Q2").build();
        when(this.quarterRepository.findById(anyLong())).thenReturn(Optional.of(quarter));
        Quarter objectQuarter = this.quarterService.getQuarterById(3L);
        assertEquals("GJ 22/23-Q2", objectQuarter.getLabel());
        assertEquals(3, objectQuarter.getId());
    }

    @Test
    void shouldThrowResponseException() {
        assertThrows(ResponseStatusException.class, () -> this.quarterService.getQuarterById(null));
    }

    @Test
    void shouldThrowExceptionBecauseOfNotFound() {
        when(this.quarterRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            this.quarterService.getQuarterById(5L);
        });
    }

    @Test
    void shouldFillHasMap() {
        HashMap<Integer, Integer> hashMap = this.quarterService.fillQuarterMap();
        assertEquals(hashMap.get(1), 3);
        assertEquals(hashMap.get(2), 4);
        assertEquals(hashMap.get(3), 1);
        assertEquals(hashMap.get(4), 2);
    }

    @Test
    void shouldSetCurrentBusinessYearQuater() {
        when(this.myCal.get(Calendar.MONTH)).thenReturn(6);
        when(this.quarterMap.get(2)).thenReturn(4);

        assertEquals(this.quarterService.getBusinessYearQuarter(), 1);
    }

    @Test
    void shouldGenerateCurrentQuarter() {
        when(this.quarterService.getBusinessYearQuarter()).thenReturn(3);
        when(this.quarterService.getCurrentYear()).thenReturn(22);

        assertEquals(this.quarterService.generateCurrentQuarter(), "GJ 22/23-Q3");
    }

    @Test
    void shouldGenerateFutureQuater() {
        assertEquals(this.quarterService.generateCurrentQuarter(), "GJ 22/23-Q3");
    }
}
