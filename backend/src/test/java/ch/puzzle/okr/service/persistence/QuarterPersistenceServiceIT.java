package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.test.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.util.QuarterRangeChecker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringIntegrationTest
class QuarterPersistenceServiceIT {

    @Autowired
    private QuarterPersistenceService quarterPersistenceService;

    @Test
    void shouldReturnSingleQuarterWhenFindingByValidId() {
        Quarter returnedQuarter = quarterPersistenceService.findById(1L);

        assertEquals(1L, returnedQuarter.getId());
        assertEquals("GJ 22/23-Q4", returnedQuarter.getLabel());
        assertEquals(LocalDate.of(2023, 4, 1), returnedQuarter.getStartDate());
        assertEquals(LocalDate.of(2023, 6, 30), returnedQuarter.getEndDate());
    }

    @Test
    void shouldThrowExceptionWhenFindingQuarterNotFound() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> quarterPersistenceService.findById(321L));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of("Quarter", "321")));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void shouldThrowExceptionWhenFindingQuarterWithIdNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> quarterPersistenceService.findById(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Quarter")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
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

        assertTrue(QuarterRangeChecker.nowIsInQuarter(LocalDate.now(), quarter));
        assertNotNull(quarter.getId());
        assertNotNull(quarter.getLabel());
    }

    @DisplayName("findByLabel() should return single Quarter when label is valid")
    @Test
    void findByLabelShouldReturnSingleQuarterWhenLabelIsValid() {
        // arrange + act
        Quarter returnedQuarter = quarterPersistenceService.findByLabel("GJ 22/23-Q4");

        // assert
        assertEquals(1L, returnedQuarter.getId());
        assertEquals("GJ 22/23-Q4", returnedQuarter.getLabel());
        assertEquals(LocalDate.of(2023, 4, 1), returnedQuarter.getStartDate());
        assertEquals(LocalDate.of(2023, 6, 30), returnedQuarter.getEndDate());
    }

    @DisplayName("findByLabel() should return null when label is not valid")
    @Test
    void findByLabelShouldReturnNullWhenLabelIsNotValid() {
        // arrange + act
        Quarter returnedQuarter = quarterPersistenceService.findByLabel("a_not_valid_label");

        // assert
        assertNull(returnedQuarter);
    }

    @DisplayName("findByLabel() should return null when label is null")
    @Test
    void findByLabelShouldReturnNullWhenLabelIsNull() {
        // arrange + act
        Quarter returnedQuarter = quarterPersistenceService.findByLabel(null);

        // assert
        assertNull(returnedQuarter);
    }
}
