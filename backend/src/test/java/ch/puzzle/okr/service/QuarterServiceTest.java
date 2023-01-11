package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuarterServiceTest {
    @MockBean
    QuarterRepository quarterRepository = Mockito.mock(QuarterRepository.class);

    @InjectMocks
    @Spy
    private QuarterService quarterService;

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

    private static Stream<Arguments> shouldGenerateCurrentQuarterLabel() {
        return Stream.of(Arguments.of(2023, 1, "GJ 22/23-Q3"), Arguments.of(2022, 4, "GJ 21/22-Q4"),
                Arguments.of(2021, 7, "GJ 21/22-Q1"), Arguments.of(2022, 10, "GJ 22/23-Q2"));
    }

    private static Stream<Arguments> shouldShortenYear() {
        return Stream.of(Arguments.of(2000, "00"), Arguments.of(2005, "05"), Arguments.of(2014, "14"),
                Arguments.of(2020, "20"), Arguments.of(2023, "23"));
    }

    @BeforeEach
    void beforeEach() {

        quarterService.now = YearMonth.of(2022, 1);
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
    void shouldFillHashMap() {
        Map<Integer, Integer> hashMap = QuarterService.yearToBusinessQuarterMap;
        assertEquals(3, hashMap.get(1));
        assertEquals(4, hashMap.get(2));
        assertEquals(1, hashMap.get(3));
        assertEquals(2, hashMap.get(4));
    }

    @ParameterizedTest
    @MethodSource
    void shouldGetFutureQuarters(int year, int month, List<String> futureQuarters) {
        YearMonth yearMonth = YearMonth.of(year, month);
        assertEquals(futureQuarters, this.quarterService.getFutureQuarters(yearMonth, 1));
    }

    @ParameterizedTest
    @MethodSource
    void shouldGetPastQuarters(int year, int month, List<String> pastQuarters) {
        YearMonth yearMonth = YearMonth.of(year, month);
        assertEquals(pastQuarters, this.quarterService.getPastQuarters(yearMonth, 4));
    }

    @ParameterizedTest
    @MethodSource
    void shouldGenerateQuarterLabel(int year, int quarter, String quarterLabel) {
        assertEquals(quarterLabel, this.quarterService.generateQuarterLabel(year, quarter));
    }

    @ParameterizedTest
    @MethodSource
    void shouldGetOrCreateQuarters(int currentYear, int firstLabelYear, int month, int businessYearQuarter,
            String currentQuarterLabel, List<String> futureQuarters, List<String> pastQuarters) {

        Quarter quarter = Quarter.Builder.builder().withLabel(currentQuarterLabel).withId(1L).build();
        YearMonth yearMonth = YearMonth.of(currentYear, month);
        quarterService.now = yearMonth;

        doReturn(currentQuarterLabel).when(this.quarterService).generateQuarterLabel(firstLabelYear,
                businessYearQuarter);
        doReturn(futureQuarters).when(this.quarterService).getFutureQuarters(yearMonth, 1);
        doReturn(pastQuarters).when(this.quarterService).getPastQuarters(yearMonth, 4);
        doReturn(Optional.of(quarter)).when(this.quarterRepository).findByLabel(anyString());

        assertEquals(List.of(quarter, quarter, quarter, quarter, quarter, quarter),
                quarterService.getOrCreateQuarters());
    }

    @ParameterizedTest
    @MethodSource
    void shouldGenerateCurrentQuarterLabel(int year, int month, String quarterLabel) {
        YearMonth yearMonth = YearMonth.of(year, month);
        quarterService.now = yearMonth;
        assertEquals(quarterLabel, quarterService.getQuarter(yearMonth));
    }

    @ParameterizedTest
    @MethodSource
    void shouldShortenYear(int year, String shortedYear) {
        assertEquals(shortedYear, this.quarterService.shortenYear(year));
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
