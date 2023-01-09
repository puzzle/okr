package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.junit.jupiter.api.*;
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
        Map<Integer, Integer> hashMap = this.quarterService.yearToBusinessQuarterMap();
        assertEquals(3, hashMap.get(1));
        assertEquals(4, hashMap.get(2));
        assertEquals(1, hashMap.get(3));
        assertEquals(2, hashMap.get(4));
    }

    @Test
    void shouldSetCurrentBusinessYearQuarter() {
        assertEquals(2, this.quarterService.getBusinessYearQuarter());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 07, 01);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;

        assertEquals(1, this.quarterService.getBusinessYearQuarter());
    }

    @Test
    void shouldReturnCurrentYear() {
        assertEquals(22, this.quarterService.getCurrentYear());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2004, 01, 01);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;

        assertEquals(04, this.quarterService.getCurrentYear());
    }

    @Disabled
    @Test
    void shouldGenerateCurrentQuarterInFirst() {
//        assertEquals("GJ 22/23-Q2", this.quarterService.generateCurrentQuarter());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 05, 01);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;

//        assertEquals("GJ 21/22-Q4", this.quarterService.generateCurrentQuarter());
    }

    @Test
    void shouldGenerateFutureQuater() {
        assertEquals("GJ 22/23-Q3", this.quarterService.getFutureQuarterLabels(22, 2,1));
    }

    @Test
    void shouldGenerateFutureQuaterOnBusinessYearChange() {
        assertEquals("GJ 23/24-Q1", this.quarterService.getFutureQuarterLabels(22, 4,1 ));
    }

    @Test
    void shouldReturnPastQuartersOverYearChange() {
        when(this.quarterRepository.findByLabel("GJ 21/22-Q2")).thenReturn(quarter1);
        when(this.quarterRepository.findByLabel("GJ 21/22-Q3")).thenReturn(quarter2);
        when(this.quarterRepository.findByLabel("GJ 21/22-Q4")).thenReturn(quarter3);
        when(this.quarterRepository.findByLabel("GJ 22/23-Q1")).thenReturn(quarter4);

        Collections.reverse(quarterList);

        assertEquals(this.quarterService.getPastQuarters(22, 2, 4), quarterList);
    }

    @Test
    void shouldGeneratePastQuartersInRepository() {
        when(this.quarterRepository.findByLabel(anyString())).thenReturn(null);
        when(this.quarterRepository.save(any())).thenReturn(quarter1).thenReturn(quarter2).thenReturn(quarter3)
                .thenReturn(quarter4);

        assertEquals(this.quarterService.getPastQuarters(22, 2,4 ), quarterList);
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
