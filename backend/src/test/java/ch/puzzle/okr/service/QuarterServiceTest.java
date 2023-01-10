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

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuarterServiceTest {
    @MockBean
    QuarterRepository quarterRepository = Mockito.mock(QuarterRepository.class);

    @InjectMocks
    @Spy
    private QuarterService quarterService;

    @Mock
    Calendar calendarMock;
    Calendar calendar;

    private static Stream<Arguments> shouldGetCurrentBusinessYearQuarter() {
        return Stream.of(
                Arguments.of(Calendar.JANUARY, 3),
                Arguments.of(Calendar.MARCH, 3),
                Arguments.of(Calendar.APRIL, 4),
                Arguments.of(Calendar.JUNE, 4),
                Arguments.of(Calendar.JULY, 1),
                Arguments.of(Calendar.SEPTEMBER, 1),
                Arguments.of(Calendar.OCTOBER, 2),
                Arguments.of(Calendar.DECEMBER, 2));
    }

    private static Stream<Arguments> shouldReturnCurrentYear() {
        return Stream.of(
                Arguments.of(2010, 2010),
                Arguments.of(2004, 2004),
                Arguments.of(2022, 2022));
    }

    private static Stream<Arguments> shouldGetFutureQuarters() {
        return Stream.of(
                Arguments.of(23, 1, List.of("GJ 23/24-Q2")),
                Arguments.of(22, 2, List.of("GJ 22/23-Q3")),
                Arguments.of(21, 3, List.of("GJ 21/22-Q4")),
                Arguments.of(22, 4, List.of("GJ 23/24-Q1")));
    }

    private static Stream<Arguments> shouldGetPastQuarters() {
        return Stream.of(
                Arguments.of(23, 1, List.of("GJ 22/23-Q4", "GJ 22/23-Q3", "GJ 22/23-Q2", "GJ 22/23-Q1")),
                Arguments.of(22, 2, List.of("GJ 22/23-Q1", "GJ 21/22-Q4", "GJ 21/22-Q3", "GJ 21/22-Q2")),
                Arguments.of(21, 3, List.of("GJ 21/22-Q2", "GJ 21/22-Q1", "GJ 20/21-Q4", "GJ 20/21-Q3")),
                Arguments.of(22, 4, List.of("GJ 22/23-Q3", "GJ 22/23-Q2", "GJ 22/23-Q1", "GJ 21/22-Q4"))
        );
    }

    private static Stream<Arguments> shouldGenerateQuarterLabel() {
        return Stream.of(
                Arguments.of(23, 1, "GJ 23/24-Q1"),
                Arguments.of(22, 2, "GJ 22/23-Q2"),
                Arguments.of(21, 3, "GJ 21/22-Q3"),
                Arguments.of(22, 4, "GJ 22/23-Q4"));
    }




    private static Stream<Arguments> shouldGetOrCreateQuarters() {
        return Stream.of(
                Arguments.of(2023,
                        2022,
                        1,
                        3,
                        "GJ 22/23-Q3",
                        List.of("GJ 22/23-Q4"),
                        List.of("GJ 22/23-Q2", "GJ 22/23-Q1", "GJ 21/22-Q4", "GJ 21/22-Q3"),
                        List.of(
                            Quarter.Builder.builder().withLabel("GJ 22/23-Q3").build(),
                            Quarter.Builder.builder().withLabel("GJ 22/23-Q4").build(),
                            Quarter.Builder.builder().withLabel("GJ 22/23-Q2").build(),
                            Quarter.Builder.builder().withLabel("GJ 22/23-Q1").build(),
                            Quarter.Builder.builder().withLabel("GJ 21/22-Q4").build(),
                            Quarter.Builder.builder().withLabel("GJ 21/22-Q3").build()
                        ))

        );
    }

    @BeforeEach
    void beforeEach() {
        calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1);
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
    void shouldFillHashMap() {
        Map<Integer, Integer> hashMap = this.quarterService.yearToBusinessQuarterMap();
        assertEquals(3, hashMap.get(1));
        assertEquals(4, hashMap.get(2));
        assertEquals(1, hashMap.get(3));
        assertEquals(2, hashMap.get(4));
    }

    @ParameterizedTest
    @MethodSource
    void shouldGetCurrentBusinessYearQuarter(int month, int businessYearQuarter) {
        calendar.set(2022, month, 1);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;

        assertEquals(businessYearQuarter, this.quarterService.getBusinessYearQuarter());
    }

    @ParameterizedTest
    @MethodSource
    void shouldReturnCurrentYear(int year, int shortedYear) {
        calendar.set(year, Calendar.DECEMBER, 1);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;

        assertEquals(shortedYear, this.quarterService.getCurrentYear());
    }

    @ParameterizedTest
    @MethodSource
    void shouldGetFutureQuarters(int year, int quarter, List<String> futureQuarters) {
        assertEquals(futureQuarters, this.quarterService.getFutureQuarterLabels(year, quarter, 1));
    }

    @ParameterizedTest
    @MethodSource
    void shouldGetPastQuarters(int year, int quarter, List<String> pastQuarters) {
        assertEquals(pastQuarters, this.quarterService.getPastQuarters(year, quarter, 4));
    }

    @ParameterizedTest
    @MethodSource
    void shouldGenerateQuarterLabel(int year, int quarter, String quarterLabel) {
        assertEquals(quarterLabel, this.quarterService.generateQuarterLabel(year, quarter));
    }

    @ParameterizedTest
    @MethodSource
    void shouldGetOrCreateQuarters(int currentYear, int firstLabelYear, int quarter, int businessYearQuarter, String currentQuarterLabel, List<String> futureQuarters, List<String> pastQuarters, List<Quarter> quarters) {
        int month = monthFromQuarter(quarter);
        calendar.set(currentYear, month, 1);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;

        doReturn(currentQuarterLabel)
                .when(this.quarterService).generateQuarterLabel(firstLabelYear, businessYearQuarter);
        doReturn(futureQuarters)
                .when(this.quarterService).getFutureQuarterLabels(firstLabelYear, businessYearQuarter,1);
        doReturn(pastQuarters)
                .when(this.quarterService).getPastQuarters(firstLabelYear, businessYearQuarter,4);
        doReturn(quarters)
                .when(this.quarterRepository).saveAll(any());

        assertEquals(quarters, this.quarterService.getOrCreateQuarters());
    }

    @ParameterizedTest
    @MethodSource
    void shouldGenerateCurrentQuarterLabel(int year, int month, String quarterLabel) {
        calendar.set(year, month, 1);
        calendarMock = calendar;
        quarterService.calendar = calendarMock;
        int quarter = quarterService.getBusinessYearQuarter();
        int businessYear = quarterService.getCurrentYear();
        assertEquals(quarterLabel, this.quarterService.generateQuarterLabel(businessYear, quarter));
    }

    private static Stream<Arguments> shouldGenerateCurrentQuarterLabel() {
        return Stream.of(
                Arguments.of(2023, 1, "GJ 22/23-Q3"),
                Arguments.of(2022, 4, "GJ 21/22-Q4"),
                Arguments.of(2021, 7, "GJ 21/22-Q1"),
                Arguments.of(2022, 10, "GJ 22/23-Q2"));
    }

    private int monthFromQuarter(int quarter) {
        return quarter * 3 - 1;
    }
}
