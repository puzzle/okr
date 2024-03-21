package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 4, 5, 7, 8, 10, 11 })
    void shouldNotGenerateQuarterIfNotLastMonth(int month) {
        ReflectionTestUtils.setField(quarterBusinessService, "quarterStart", 7);

        Mockito.when(quarterBusinessService.getCurrentYearMonth()).thenReturn(YearMonth.of(2030, month));
        quarterBusinessService.scheduledGenerationQuarters();
        verify(quarterPersistenceService, never()).save(any());
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

    @ParameterizedTest(name = "With start month {0} month {1} should be in quarter {2}")
    @CsvSource(textBlock = """
                5,1,3
                5,2,4
                5,3,4
                5,4,4
                5,5,1
                5,6,1
                5,7,1
                5,8,2
                5,9,2
                5,10,2
                5,11,3
                5,12,3
                10,1,2
                10,2,2
                10,3,2
                10,4,3
                10,5,3
                10,6,3
                10,7,4
                10,8,4
                10,9,4
                10,10,1
                10,11,1
                10,12,1
            """)
    void shouldGetQuartersBasedOnStart(String start, String month, String quarter) {
        ReflectionTestUtils.setField(quarterBusinessService, "quarterStart", Integer.valueOf(start));
        Map<Integer, Integer> quarters = quarterBusinessService.generateQuarters();
        assertEquals(Integer.valueOf(quarter), quarters.get(Integer.valueOf(month)));
    }

    @Test
    void shouldReturnNullWhenNoQuarterGenerationNeeded() {
        Mockito.when(quarterBusinessService.getCurrentYearMonth()).thenReturn(YearMonth.of(2030, 4));
        quarterBusinessService.scheduledGenerationQuarters();
        verify(quarterPersistenceService, times(0)).save(any());
    }
}
