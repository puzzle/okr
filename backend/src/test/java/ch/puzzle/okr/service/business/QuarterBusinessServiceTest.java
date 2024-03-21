package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuarterBusinessServiceTest {
    @Mock
    QuarterPersistenceService quarterPersistenceService;

    @Mock
    QuarterValidationService quarterValidationService;

    @InjectMocks
    @Spy
    private QuarterBusinessService quarterBusinessService;

    private static Stream<Arguments> shouldGetFirstMonthFromQuarter() {
        return Stream.of(Arguments.of(1, 1), Arguments.of(2, 4), Arguments.of(3, 7), Arguments.of(4, 10));
    }

    @Test
    void shouldReturnProperQuarter() {
        quarterBusinessService.getQuarterById(3L);
        verify(quarterValidationService, times(1)).validateOnGet(3L);
        verify(quarterPersistenceService, times(1)).findById(3L);

    }

    @Test
    void shouldReturnExceptionWhenIdIsNullOnGetQuarter() {
        quarterBusinessService.getQuarterById(null);
        verify(quarterValidationService, times(1)).validateOnGet(null);
    }

    @Test
    void shouldCallGetCurrentQuarterOnGetCurrentQuarter() {
        quarterBusinessService.getCurrentQuarter();
        verify(quarterPersistenceService, times(1)).getCurrentQuarter();
    }

    @Test
    void shouldCallGetQuarters() {
        quarterBusinessService.getQuarters();
        verify(quarterPersistenceService).getMostCurrentQuarters();
    }

    @Test
    void shouldGetBacklogQuarter() {
        Quarter realQuarter1 = Quarter.Builder.builder().withId(1L).withLabel("GJ-22/23-Q3")
                .withStartDate(LocalDate.of(2022, 4, 1)).withEndDate(LocalDate.of(2022, 7, 31)).build();
        Quarter realQuarter2 = Quarter.Builder.builder().withId(2L).withLabel("GJ-22/23-Q4")
                .withStartDate(LocalDate.of(2022, 8, 1)).withEndDate(LocalDate.of(2022, 11, 30)).build();
        List<Quarter> quarterList = new ArrayList<>(Arrays.asList(realQuarter1, realQuarter2));

        Quarter backlogQuarter = Quarter.Builder.builder().withId(199L).withLabel("Backlog").build();
        when(quarterPersistenceService.getMostCurrentQuarters()).thenReturn(quarterList);
        when(quarterPersistenceService.findByLabel("Backlog")).thenReturn(backlogQuarter);

        quarterList = quarterBusinessService.getQuarters();
        assertEquals(3, quarterList.size());
        assertEquals("Backlog", quarterList.get(0).getLabel());
        assertNull(quarterList.get(0).getStartDate());
        assertNull(quarterList.get(0).getEndDate());
    }

    @Test
    void shouldGenerateCorrectQuarter() {
        ReflectionTestUtils.setField(quarterBusinessService, "quarterStart", 7);
        ReflectionTestUtils.setField(quarterBusinessService, "quarterFormat", "GJ xx/yy-Qzz");

        Quarter quarterStandard = Quarter.Builder.builder().withId(null).withLabel("GJ 30/31-Q1")
                .withStartDate(LocalDate.of(2030, 7, 1)).withEndDate(LocalDate.of(2030, 9, 30)).build();

        Quarter quarterAfterMidYear = Quarter.Builder.builder().withId(null).withLabel("GJ 30/31-Q3")
                .withStartDate(LocalDate.of(2031, 1, 1)).withEndDate(LocalDate.of(2031, 3, 31)).build();

        Mockito.when(quarterBusinessService.getCurrentYearMonth()).thenReturn(YearMonth.of(2030, 3));

        quarterBusinessService.scheduledGenerationQuarters();
        verify(quarterPersistenceService).save(quarterStandard);

        Mockito.when(quarterBusinessService.getCurrentYearMonth()).thenReturn(YearMonth.of(2030, 9));
        quarterBusinessService.scheduledGenerationQuarters();
        verify(quarterPersistenceService).save(quarterAfterMidYear);
    }

    @Test
    void shouldGetQuartersBasedOnStart() {
        ReflectionTestUtils.setField(quarterBusinessService, "quarterStart", 5);
        Map<Integer, Integer> startMay = quarterBusinessService.generateQuarters();
        assertEquals(startMay,
                Map.ofEntries(Map.entry(1, 3), Map.entry(2, 4), Map.entry(3, 4), Map.entry(4, 4), Map.entry(5, 1),
                        Map.entry(6, 1), Map.entry(7, 1), Map.entry(8, 2), Map.entry(9, 2), Map.entry(10, 2),
                        Map.entry(11, 3), Map.entry(12, 3)));

        ReflectionTestUtils.setField(quarterBusinessService, "quarterStart", 10);
        Map<Integer, Integer> startOctober = quarterBusinessService.generateQuarters();
        assertEquals(startOctober,
                Map.ofEntries(Map.entry(1, 2), Map.entry(2, 2), Map.entry(3, 2), Map.entry(4, 3), Map.entry(5, 3),
                        Map.entry(6, 3), Map.entry(7, 4), Map.entry(8, 4), Map.entry(9, 4), Map.entry(10, 1),
                        Map.entry(11, 1), Map.entry(12, 1)));
    }

    @Test
    void shouldReturnNullWhenNoQuarterGenerationNeeded() {
        Mockito.when(quarterBusinessService.getCurrentYearMonth()).thenReturn(YearMonth.of(2030, 4));
        quarterBusinessService.scheduledGenerationQuarters();
        verify(quarterPersistenceService, times(0)).save(any());
    }
}
