package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuarterBusinessServiceTest {
    @Mock
    QuarterPersistenceService quarterPersistenceService = Mockito.mock(QuarterPersistenceService.class);

    @Mock
    QuarterValidationService quarterValidationService = Mockito.mock(QuarterValidationService.class);

    @InjectMocks
    @Spy
    private QuarterBusinessService quarterBusinessService;

    private static Stream<Arguments> shouldGetFutureQuarters() {
        return Stream.of(Arguments.of(2023, 7, List.of("GJ 23/24-Q2")), Arguments.of(2022, 10, List.of("GJ 22/23-Q3")),
                Arguments.of(2022, 1, List.of("GJ 21/22-Q4")), Arguments.of(2023, 4, List.of("GJ 23/24-Q1")));
    }

    private static Stream<Arguments> shouldGetPastQuarters() {
        return Stream.of(Arguments.of(2023, 7, List.of("GJ 22/23-Q4", "GJ 22/23-Q3", "GJ 22/23-Q2", "GJ 22/23-Q1")),
                Arguments.of(2022, 10, List.of("GJ 22/23-Q1", "GJ 21/22-Q4", "GJ 21/22-Q3", "GJ 21/22-Q2")),
                Arguments.of(2022, 1, List.of("GJ 21/22-Q2", "GJ 21/22-Q1", "GJ 20/21-Q4", "GJ 20/21-Q3")),
                Arguments.of(2023, 4, List.of("GJ 22/23-Q3", "GJ 22/23-Q2", "GJ 22/23-Q1", "GJ 21/22-Q4")));
    }

    private static Stream<Arguments> shouldGenerateQuarterLabel() {
        return Stream.of(Arguments.of(2023, 1, "GJ 23/24-Q1"), Arguments.of(22, 2, "GJ 22/23-Q2"),
                Arguments.of(2021, 3, "GJ 21/22-Q3"), Arguments.of(22, 4, "GJ 22/23-Q4"));
    }

    private static Stream<Arguments> shouldGetFirstMonthFromQuarter() {
        return Stream.of(Arguments.of(1, 1), Arguments.of(2, 4), Arguments.of(3, 7), Arguments.of(4, 10));
    }

    private static Stream<Arguments> shouldGetOrCreateQuarters() {
        return Stream.of(Arguments.of(2023, 2022, 1, 3, "GJ 22/23-Q3", List.of("GJ 22/23-Q4"),
                List.of("GJ 22/23-Q2", "GJ 22/23-Q1", "GJ 21/22-Q4", "GJ 21/22-Q3"))

        );
    }

    private static Stream<Arguments> shouldShortenYear() {
        return Stream.of(Arguments.of(2000, "00"), Arguments.of(2005, "05"), Arguments.of(2014, "14"),
                Arguments.of(2020, "20"), Arguments.of(2023, "23"));
    }

    @BeforeEach
    void beforeEach() {
        quarterBusinessService.now = YearMonth.of(2022, 1);
    }

    @Test
    void shouldReturnProperQuarter() {
        Quarter quarter = Quarter.Builder.builder().withId(3L).withLabel("GJ 22/23-Q2").build();
        when(this.quarterPersistenceService.findById(anyLong())).thenReturn(quarter);
        Quarter objectQuarter = this.quarterBusinessService.getQuarterById(3L);
        assertEquals("GJ 22/23-Q2", objectQuarter.getLabel());
        assertEquals(3, objectQuarter.getId());
    }

    @Test
    void shouldCallMethodsOnGetQuarter() {
        this.quarterBusinessService.getQuarterById(1L);
        Mockito.verify(this.quarterValidationService).validateOnGet(any());
        Mockito.verify(this.quarterPersistenceService).findById(any());
    }

    @Test
    void shouldCallMethodsOnGetActiveQuarter() {
        this.quarterBusinessService.getActiveQuarter(any());
        Mockito.verify(this.quarterValidationService).validateActiveQuarterOnGet(any());
        Mockito.verify(this.quarterPersistenceService).getCurrentQuarter(any());
    }

    @Test
    void shouldFillHashMap() {
        Map<Integer, Integer> hashMap = QuarterBusinessService.yearToBusinessQuarterMap;
        assertEquals(3, hashMap.get(1));
        assertEquals(4, hashMap.get(2));
        assertEquals(1, hashMap.get(3));
        assertEquals(2, hashMap.get(4));
    }

    // Can be removed?
//    @ParameterizedTest
//    @MethodSource
//    void shouldGetOrCreateQuarters(int currentYear, int firstLabelYear, int month, int businessYearQuarter,
//            String currentQuarterLabel, List<String> futureQuarters, List<String> pastQuarters) {
//
//        Quarter quarter = Quarter.Builder.builder().withLabel(currentQuarterLabel).withId(1L).build();
//        YearMonth yearMonth = YearMonth.of(currentYear, month);
//        quarterBusinessService.now = yearMonth;
//
//         doReturn(currentQuarterLabel).when(this.quarterBusinessService).generateQuarterLabel(firstLabelYear,
//         businessYearQuarter);
//         doReturn(futureQuarters).when(this.quarterBusinessService).getFutureQuarters(yearMonth, 1);
//         doReturn(pastQuarters).when(this.quarterBusinessService).getPastQuarters(yearMonth, 4);
//        doReturn(Optional.of(quarter)).when(this.quarterRepository).findByLabel(anyString());
//
//        assertEquals(List.of(quarter, quarter, quarter, quarter, quarter, quarter),
//                quarterBusinessService.getQuarters());
//    }

    @Test
    void shouldGetQuarters() {

    }

    private static Stream<Arguments> shouldGenerateCurrentQuarterLabel() {
        return Stream.of(Arguments.of(2023, 1, "GJ 22/23-Q3"), Arguments.of(2023, 2, "GJ 22/23-Q3"),
                Arguments.of(2023, 3, "GJ 22/23-Q3"), Arguments.of(2023, 4, "GJ 22/23-Q4"),
                Arguments.of(2023, 5, "GJ 22/23-Q4"), Arguments.of(2023, 6, "GJ 22/23-Q4"),
                Arguments.of(2023, 7, "GJ 23/24-Q1"), Arguments.of(2023, 8, "GJ 23/24-Q1"),
                Arguments.of(2023, 9, "GJ 23/24-Q1"), Arguments.of(2023, 10, "GJ 23/24-Q2"),
                Arguments.of(2023, 11, "GJ 23/24-Q2"), Arguments.of(2023, 12, "GJ 23/24-Q2"));
    }

    @ParameterizedTest
    @MethodSource
    void shouldGetFirstMonthFromQuarter(int quarter, int month) {
        assertEquals(month, monthFromQuarter(quarter));
    }

    private int monthFromQuarter(int quarter) {
        return quarter * 3 - 2;
    }
}
