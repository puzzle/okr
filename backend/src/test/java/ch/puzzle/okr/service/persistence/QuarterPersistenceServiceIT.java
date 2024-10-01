package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
class QuarterPersistenceServiceIT {

    @Autowired
    private QuarterPersistenceService quarterPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @AfterEach
    void tearDown() {
        TenantContext.setCurrentTenant(null);
    }

    @Test
    void shouldReturnSingleQuarterWhenFindingByLabel() {
        String labelQ4 = "GJ 22/23-Q4";
        Quarter returnedQuarter = quarterPersistenceService.findByLabel(labelQ4);

        assertEquals(1L, returnedQuarter.getId());
        assertEquals(labelQ4, returnedQuarter.getLabel());
        assertEquals(LocalDate.of(2023, 4, 1), returnedQuarter.getStartDate());
        assertEquals(LocalDate.of(2023, 6, 30), returnedQuarter.getEndDate());
    }

    @Test
    void shouldReturnCurrentQuarterFutureQuarterAnd4PastQuarters() {
        List<Quarter> quarterListFromFunction = quarterPersistenceService.getMostCurrentQuarters();

        assertEquals(6, quarterListFromFunction.size());
        assertTrue(
                quarterListFromFunction.get(0).getStartDate().isAfter(quarterListFromFunction.get(5).getStartDate()));
    }

    @Test
    void shouldReturnCurrentQuarter() {
        Quarter quarter = quarterPersistenceService.getCurrentQuarter();

        assertTrue(LocalDate.now().isEqual(quarter.getStartDate()) || //
                LocalDate.now().isAfter(quarter.getStartDate()));

        assertTrue(LocalDate.now().isEqual(quarter.getEndDate()) || //
                LocalDate.now().isBefore(quarter.getEndDate()));

        assertNotNull(quarter.getId());
        assertNotNull(quarter.getLabel());
    }

}
