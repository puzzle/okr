package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
public class QuarterPersistenceServiceIT {
    Quarter createdQuarter;
    @Autowired
    private QuarterPersistenceService quarterPersistenceService;

    @AfterEach
    void tearDown() {
        if (createdQuarter != null) {
            quarterPersistenceService.deleteQuarterById(createdQuarter.getId());
            createdQuarter = null;
        }
    }

    @Test
    void shouldReturnSingleQuarterWhenFindingByValidId() {
        Quarter returnedQuarter = quarterPersistenceService.findById(1L);

        assertEquals(1L, returnedQuarter.getId());
        assertEquals("GJ 22/23-Q4", returnedQuarter.getLabel());
        assertEquals(LocalDate.of(2023, 04, 01), returnedQuarter.getStartDate());
        assertEquals(LocalDate.of(2023, 06, 30), returnedQuarter.getEndDate());
    }

    @Test
    void shouldThrowExceptionWhenFindingQuarterNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> quarterPersistenceService.findById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Quarter with id 321 not found", exception.getReason());
    }

    @Test
    void shouldThrowExceptionWhenFindingQuarterWithIdNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> quarterPersistenceService.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing identifier for Quarter", exception.getReason());
    }

    @Test
    void shouldReturnCurrentQuarterFutureQuarterAnd4PastQuarters() {
        List<Quarter> quarterListFromFunction = quarterPersistenceService.getMostCurrentQuarters();

        assertEquals(6, quarterListFromFunction.size());
        assertTrue(
                quarterListFromFunction.get(0).getStartDate().isAfter(quarterListFromFunction.get(5).getStartDate()));
    }
}
