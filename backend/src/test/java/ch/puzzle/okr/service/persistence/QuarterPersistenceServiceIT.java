package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.Constants.QUARTER;
import static ch.puzzle.okr.test.TestConstants.GJ_FOR_TESTS_QUARTER_ID;
import static ch.puzzle.okr.test.TestConstants.GJ_FOR_TEST_QUARTER_LABEL;
import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.business.QuarterBusinessService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import ch.puzzle.okr.util.quarter.check.QuarterRangeChecker;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.util.ReflectionTestUtils;

@SpringIntegrationTest
class QuarterPersistenceServiceIT {

    private static final Logger log = LoggerFactory.getLogger(QuarterPersistenceServiceIT.class);
    @MockitoSpyBean
    @Autowired
    private QuarterPersistenceService quarterPersistenceService;

    @MockitoSpyBean private TenantConfigProvider tenantConfigProvider;

    @MockitoSpyBean
    @Autowired
    @InjectMocks
    private QuarterBusinessService quarterBusinessService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @AfterEach
    void tearDown() {
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Should return current, future and GJForTests quarter on getMostCurrentQuarters()")
    @Test
    void getMostCurrentQuartersShouldReturnCurrentQuarterAndFutureQuarterAndGJForTestsQuarter() {
        List<Quarter> quarterListFromFunction = quarterPersistenceService.getMostCurrentQuarters();

        assertEquals(3, quarterListFromFunction.size());
        assertGJForTestsQuarterIsFoundOnce(quarterListFromFunction);
        assertCurrentQuarterIsFoundOnce(quarterListFromFunction);
    }

    private void assertGJForTestsQuarterIsFoundOnce(List<Quarter> quarters) {
        long foundGJForTestsQuartersCount = quarters
                .stream()
                .filter(quarter -> quarter.getLabel().equals(GJ_FOR_TEST_QUARTER_LABEL))
                .count();
        assertEquals(1, foundGJForTestsQuartersCount);
    }

    private void assertCurrentQuarterIsFoundOnce(List<Quarter> quarters) {
        long foundCurrentQuartersCount = quarters
                .stream()
                .filter(quarter -> QuarterRangeChecker.nowIsInQuarter(LocalDate.now(), quarter))
                .count();
        assertEquals(1, foundCurrentQuartersCount);
    }

    @DisplayName("Should return current quarter on getCurrentQuarter()")
    @Test
    void shouldGetCurrentQuarter() {
        Quarter quarter = quarterPersistenceService.getCurrentQuarter();

        assertTrue(LocalDate.now().isEqual(quarter.getStartDate()) || //
                   LocalDate.now().isAfter(quarter.getStartDate()));

        assertTrue(LocalDate.now().isEqual(quarter.getEndDate()) || //
                   LocalDate.now().isBefore(quarter.getEndDate()));

        assertNotNull(quarter.getId());
        assertNotNull(quarter.getLabel());
    }

    @DisplayName("Should return single Quarter on findByLabel() when label is valid")
    @Test
    void findByLabelShouldReturnSingleQuarterWhenLabelIsValid() {
        // arrange + act
        Quarter returnedQuarter = quarterPersistenceService.findByLabel(GJ_FOR_TEST_QUARTER_LABEL);

        // assert
        assertEquals(GJ_FOR_TESTS_QUARTER_ID, returnedQuarter.getId());
        assertEquals(GJ_FOR_TEST_QUARTER_LABEL, returnedQuarter.getLabel());
        assertEquals(LocalDate.of(2000, 7, 1), returnedQuarter.getStartDate());
        assertEquals(LocalDate.of(2000, 9, 30), returnedQuarter.getEndDate());
    }

    @DisplayName("Should return null on findByLabel() when label is not valid")
    @Test
    void findByLabelShouldReturnNullWhenLabelIsNotValid() {
        // arrange + act
        Quarter returnedQuarter = quarterPersistenceService.findByLabel("a_not_valid_label");

        // assert
        assertNull(returnedQuarter);
    }

    @DisplayName("Should return null on findByLabel() when label is null")
    @Test
    void findByLabelShouldReturnNullWhenLabelIsNull() {
        // arrange + act
        Quarter returnedQuarter = quarterPersistenceService.findByLabel(null);

        // assert
        assertNull(returnedQuarter);
    }

    @DisplayName("Should return quarter on getModelName()")
    @Test
    void getModelNameShouldReturnQuarter() {
        assertEquals(QUARTER, quarterPersistenceService.getModelName());
    }

    @ParameterizedTest
    @CsvSource(value = {"1,1,0", "2,1,0", "3,1,1", "4,1,0","5,1,0","6,2,1", "7,1,0", "8,1,0", "9,3,1", "10,3,0", "11,1,0", "12,4,1"})
    void testCronJob(int month, int quarterIndex,  int amountOfInvocations) {
        int startQuarter = 7;
        ReflectionTestUtils.setField(quarterBusinessService, "quarterStart", startQuarter);
        int nextYear = Year.now().atMonth(startQuarter).plusMonths(month+12).getYear();
        int nextYearShort = nextYear % 1000;
        String expectedLabel = "GJ " + nextYearShort + "/" + (nextYearShort + 1) + "-Q"+ quarterIndex;

        Mockito.doReturn(YearMonth.of(nextYear, month)).when(quarterBusinessService).getCurrentYearMonth();
        Mockito.doReturn(List.of(TestHelper.SCHEMA_PITC)).when(tenantConfigProvider).getAllTenantIds();

        quarterBusinessService.scheduledGenerationQuarters();

        Mockito.verify(quarterPersistenceService, Mockito.times(amountOfInvocations)).save(ArgumentMatchers.any());

        List<Quarter> createdQuarters = quarterPersistenceService.findAll().stream().filter(quarter -> quarter.getLabel().equals(expectedLabel)).toList();
        assertEquals(amountOfInvocations, createdQuarters.size());
        assertEquals(4+ amountOfInvocations, quarterBusinessService.getQuarters().size());
        createdQuarters.forEach(quarter -> quarterPersistenceService.deleteById(quarter.getId()));
    }
}
