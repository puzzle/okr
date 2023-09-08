package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuarterBusinessServiceTest {
    @Mock
    QuarterPersistenceService quarterPersistenceService = Mockito.mock(QuarterPersistenceService.class);

    @Mock
    QuarterValidationService quarterValidationService = Mockito.mock(QuarterValidationService.class);

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
    void shouldGetQuarters() {
        // initial List
        List<Quarter> quarterList = new ArrayList<>();
        quarterList.add(Quarter.Builder.builder().withId(1L).withLabel("Initial first item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());
        quarterList.add(Quarter.Builder.builder().withId(2L).withLabel("Initial second item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());
        quarterList.add(Quarter.Builder.builder().withId(3L).withLabel("Initial third item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());
        quarterList.add(Quarter.Builder.builder().withId(4L).withLabel("Initial fourth item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());
        quarterList.add(Quarter.Builder.builder().withId(5L).withLabel("Initial fifth item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());
        quarterList.add(Quarter.Builder.builder().withId(6L).withLabel("Initial sixth item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());

        Mockito.when(quarterPersistenceService.getMostCurrentQuarters()).thenReturn(quarterList);

        // Final List
        List<Quarter> quarterListFormatted = new ArrayList<>();
        quarterListFormatted.add(Quarter.Builder.builder().withId(2L).withLabel("Initial second item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());
        quarterListFormatted.add(Quarter.Builder.builder().withId(1L).withLabel("Initial first item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());
        quarterListFormatted.add(Quarter.Builder.builder().withId(3L).withLabel("Initial third item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());
        quarterListFormatted.add(Quarter.Builder.builder().withId(4L).withLabel("Initial fourth item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());
        quarterListFormatted.add(Quarter.Builder.builder().withId(5L).withLabel("Initial fifth item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());
        quarterListFormatted.add(Quarter.Builder.builder().withId(6L).withLabel("Initial sixth item")
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now()).build());

        assertEquals(quarterListFormatted, quarterBusinessService.getQuarters());
        verify(quarterPersistenceService).getMostCurrentQuarters();
    }

    @Test
    void shouldGenerateCorrectQuarter() {
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
    void shouldReturnNullWhenNoQuarterGenerationNeeded() {
        Mockito.when(quarterBusinessService.getCurrentYearMonth()).thenReturn(YearMonth.of(2030, 4));
        quarterBusinessService.scheduledGenerationQuarters();
        verify(quarterPersistenceService, times(0)).save(any());
    }
}
