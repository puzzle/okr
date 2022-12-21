package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuarterServiceTest {
    @MockBean
    QuarterRepository quarterRepository = Mockito.mock(QuarterRepository.class);

    @InjectMocks
    private QuarterService quarterService;

    @Mock
    Calendar calendarMock;

    Quarter quarter1 = Quarter.Builder.builder().withId(1L).withLabel("GJ 21/22-Q2").build();
    Quarter quarter2 = Quarter.Builder.builder().withId(2L).withLabel("GJ 21/22-Q3").build();
    Quarter quarter3 = Quarter.Builder.builder().withId(3L).withLabel("GJ 21/22-Q4").build();
    Quarter quarter4 = Quarter.Builder.builder().withId(4L).withLabel("GJ 22/23-Q1").build();
    Quarter quarter5 = Quarter.Builder.builder().withId(4L).withLabel("GJ 22/23-Q2").build();
    Quarter quarter6 = Quarter.Builder.builder().withId(4L).withLabel("GJ 22/23-Q3").build();
    List<Quarter> quarterList = new ArrayList<>(Arrays.asList(quarter1, quarter2, quarter3, quarter4));

    @BeforeEach
    void beforeEach() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 10, 01);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;
    }

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
        HashMap<Integer, Integer> hashMap = this.quarterService.yearToBusinessQuarterMap();
        assertEquals(hashMap.get(1), 3);
        assertEquals(hashMap.get(2), 4);
        assertEquals(hashMap.get(3), 1);
        assertEquals(hashMap.get(4), 2);
    }

    @Test
    void shouldSetCurrentBusinessYearQuarter() {
        assertEquals(this.quarterService.getBusinessYearQuarter(), 2);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 07, 01);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;

        assertEquals(this.quarterService.getBusinessYearQuarter(), 1);
    }

    @Test
    void shouldReturnCurrentYear() {
        assertEquals(this.quarterService.getCurrentYear(), 22);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2004, 01, 01);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;

        assertEquals(this.quarterService.getCurrentYear(), 04);
    }

    @Test
    void shouldGenerateCurrentQuarterInFirst() {
        assertEquals(this.quarterService.generateCurrentQuarter(), "GJ 22/23-Q2");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 05, 01);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;

        assertEquals(this.quarterService.generateCurrentQuarter(), "GJ 21/22-Q4");
    }

    @Test
    void shouldGenerateFutureQuater() {
        assertEquals(this.quarterService.generateFutureQuarterLabel(22, 2), "GJ 22/23-Q3");
    }

    @Test
    void shouldGenerateFutureQuaterOnBusinessYearChange() {
        assertEquals(this.quarterService.generateFutureQuarterLabel(22, 4), "GJ 23/24-Q1");
    }

    @Test
    void shouldReturnPastQuartersOverYearChange() {
        when(this.quarterRepository.findByLabel("GJ 21/22-Q2")).thenReturn(quarter1);
        when(this.quarterRepository.findByLabel("GJ 21/22-Q3")).thenReturn(quarter2);
        when(this.quarterRepository.findByLabel("GJ 21/22-Q4")).thenReturn(quarter3);
        when(this.quarterRepository.findByLabel("GJ 22/23-Q1")).thenReturn(quarter4);

        Collections.reverse(quarterList);

        assertEquals(this.quarterService.generatePastQuarters(22, 2), quarterList);
    }

    @Test
    void shouldGeneratePastQuartersInRepository() {
        when(this.quarterRepository.findByLabel(anyString())).thenReturn(null);
        when(this.quarterRepository.save(any())).thenReturn(quarter1).thenReturn(quarter2).thenReturn(quarter3)
                .thenReturn(quarter4);

        assertEquals(this.quarterService.generatePastQuarters(22, 2), quarterList);
    }

    @Test
    void shouldGenerateFullQuarterListWhenHavingAllQuartersInRepository() {
        when(this.quarterRepository.findByLabel("GJ 21/22-Q2")).thenReturn(quarter1);
        when(this.quarterRepository.findByLabel("GJ 21/22-Q3")).thenReturn(quarter2);
        when(this.quarterRepository.findByLabel("GJ 21/22-Q4")).thenReturn(quarter3);
        when(this.quarterRepository.findByLabel("GJ 22/23-Q1")).thenReturn(quarter4);
        when(this.quarterRepository.findByLabel("GJ 22/23-Q2")).thenReturn(quarter5);
        when(this.quarterRepository.findByLabel("GJ 22/23-Q3")).thenReturn(quarter6);

        List<Quarter> finalQuarterList = new ArrayList<>(
                Arrays.asList(quarter4, quarter3, quarter2, quarter1, quarter5, quarter6));

        assertEquals(this.quarterService.getOrCreateQuarters(), finalQuarterList);
    }

    @Test
    void shouldSaveFutureAndCurrentQuarterInRepository() {
        when(this.quarterRepository.save(any())).thenReturn(quarter5).thenReturn(quarter6);
        when(this.quarterRepository.findByLabel(anyString())).thenReturn(null);

        when(this.quarterRepository.findByLabel("GJ 21/22-Q2")).thenReturn(quarter1);
        when(this.quarterRepository.findByLabel("GJ 21/22-Q3")).thenReturn(quarter2);
        when(this.quarterRepository.findByLabel("GJ 21/22-Q4")).thenReturn(quarter3);
        when(this.quarterRepository.findByLabel("GJ 22/23-Q1")).thenReturn(quarter4);

        List<Quarter> finalQuarterList = new ArrayList<>(
                Arrays.asList(quarter4, quarter3, quarter2, quarter1, quarter5, quarter6));

        assertEquals(this.quarterService.getOrCreateQuarters(), finalQuarterList);
    }

    @Test
    void shouldSaveFutureQuarterInRepository() {
        when(this.quarterRepository.save(any())).thenReturn(quarter6);
        when(this.quarterRepository.findByLabel("GJ 22/23-Q2")).thenReturn(quarter5);

        when(this.quarterRepository.findByLabel("GJ 21/22-Q2")).thenReturn(quarter1);
        when(this.quarterRepository.findByLabel("GJ 21/22-Q3")).thenReturn(quarter2);
        when(this.quarterRepository.findByLabel("GJ 21/22-Q4")).thenReturn(quarter3);
        when(this.quarterRepository.findByLabel("GJ 22/23-Q1")).thenReturn(quarter4);

        List<Quarter> finalQuarterList = new ArrayList<>(
                Arrays.asList(quarter4, quarter3, quarter2, quarter1, quarter5, quarter6));

        assertEquals(this.quarterService.getOrCreateQuarters(), finalQuarterList);
    }
}
