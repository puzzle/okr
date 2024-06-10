package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ch.puzzle.okr.Constants.BACKLOG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuarterBusinessServiceTest {
    @Mock
    QuarterPersistenceService quarterPersistenceService;

    @Mock
    QuarterValidationService quarterValidationService;

    @Mock
    ObjectiveBusinessService objectiveBusinessService;

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

        Quarter backlogQuarter = Quarter.Builder.builder().withId(199L).withLabel(BACKLOG).build();
        when(quarterPersistenceService.getMostCurrentQuarters()).thenReturn(quarterList);
        when(quarterPersistenceService.findByLabel(BACKLOG)).thenReturn(backlogQuarter);

        quarterList = quarterBusinessService.getQuarters();
        assertEquals(4, quarterList.size());
        assertEquals(BACKLOG, quarterList.get(0).getLabel());
        assertNull(quarterList.get(0).getStartDate());
        assertNull(quarterList.get(0).getEndDate());
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 4, 5, 7, 8, 10, 11 })
    void shouldNotGenerateQuarterIfNotLastMonth(int month) {
        ReflectionTestUtils.setField(quarterBusinessService, "quarterStart", 7);

        Mockito.when(quarterBusinessService.getCurrentYearMonth()).thenReturn(YearMonth.of(2030, month));
        quarterBusinessService.scheduledGenerationQuarters();
        verify(quarterPersistenceService, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(ints = { 3, 6, 9, 12 })
    void shouldGenerateQuarterIfLastMonth(int month) {
        ReflectionTestUtils.setField(quarterBusinessService, "quarterStart", 7);

        Mockito.when(quarterBusinessService.getCurrentYearMonth()).thenReturn(YearMonth.of(2030, month));
        quarterBusinessService.scheduledGenerationQuarters();
        verify(quarterPersistenceService, times(1)).save(any());
    }

    private static Stream<Arguments> generateQuarterParams() {
        return Stream.of(Arguments.of(7, "GJ xx/yy-Qzz", YearMonth.of(2030, 3), "GJ 30/31-Q1"),
                Arguments.of(7, "GJ xx/yy-Qzz", YearMonth.of(2030, 9), "GJ 30/31-Q3"),
                Arguments.of(5, "GJ xx/yy-Qzz", YearMonth.of(2030, 4), "GJ 30/31-Q2"),
                Arguments.of(1, "GJ xx-Qzz", YearMonth.of(2030, 9), "GJ 31-Q1"),
                Arguments.of(1, "GJ xxxx-Qzz", YearMonth.of(2030, 6), "GJ 2030-Q4"),
                Arguments.of(2, "xx-yy-xxxx-yyyy-Qzz", YearMonth.of(2030, 1), "30-31-2030-2031-Q2"));
    }

    @ParameterizedTest
    @MethodSource("generateQuarterParams")
    void shouldGenerateCorrectQuarter(int quarterStart, String quarterFormat, YearMonth currentYearMonth,
            String expectedLabel) {
        ReflectionTestUtils.setField(quarterBusinessService, "quarterStart", quarterStart);
        ReflectionTestUtils.setField(quarterBusinessService, "quarterFormat", quarterFormat);

        int monthsToNextQuarterStart = 4;
        LocalDate expectedStart = currentYearMonth.plusMonths(monthsToNextQuarterStart).atDay(1);

        int monthsToNextQuarterEnd = 6;
        LocalDate expectedEnd = currentYearMonth.plusMonths(monthsToNextQuarterEnd).atEndOfMonth();

        Quarter expectedQuarter = Quarter.Builder.builder().withId(null).withLabel(expectedLabel)
                .withStartDate(expectedStart).withEndDate(expectedEnd).build();

        Mockito.when(quarterBusinessService.getCurrentYearMonth()).thenReturn(currentYearMonth);

        quarterBusinessService.scheduledGenerationQuarters();

        verify(quarterPersistenceService).save(expectedQuarter);
    }

    private static Stream<Arguments> getQuartersParams() {
        return Stream.of(Arguments.of(5, 1, 3), Arguments.of(5, 2, 4), Arguments.of(5, 3, 4), Arguments.of(5, 4, 4),
                Arguments.of(5, 5, 1), Arguments.of(5, 6, 1), Arguments.of(5, 7, 1), Arguments.of(5, 8, 2),
                Arguments.of(5, 9, 2), Arguments.of(5, 10, 2), Arguments.of(5, 11, 3), Arguments.of(5, 12, 3),
                Arguments.of(10, 1, 2), Arguments.of(10, 2, 2), Arguments.of(10, 3, 2), Arguments.of(10, 4, 3),
                Arguments.of(10, 5, 3), Arguments.of(10, 6, 3), Arguments.of(10, 7, 4), Arguments.of(10, 8, 4),
                Arguments.of(10, 9, 4), Arguments.of(10, 10, 1), Arguments.of(10, 11, 1), Arguments.of(10, 12, 1));
    }

    @ParameterizedTest(name = "Start month={0}, current month={1} => quarter={2}")
    @MethodSource("getQuartersParams")
    void shouldGetQuartersBasedOnStart(int start, int month, int quarter) {
        ReflectionTestUtils.setField(quarterBusinessService, "quarterStart", start);
        Map<Integer, Integer> quarters = quarterBusinessService.generateQuarters();
        assertEquals(quarter, quarters.get(month));
    }

    @Test
    void shouldReturnNullWhenNoQuarterGenerationNeeded() {
        Mockito.when(quarterBusinessService.getCurrentYearMonth()).thenReturn(YearMonth.of(2030, 4));
        quarterBusinessService.scheduledGenerationQuarters();
        verify(quarterPersistenceService, times(0)).save(any());
    }
}
