package ch.puzzle.okr.service.validation;

import static ch.puzzle.okr.Constants.BACK_LOG_QUARTER_LABEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.test.TestHelper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class QuarterValidationServiceTest {
    @MockBean
    QuarterPersistenceService quarterPersistenceService = Mockito.mock(QuarterPersistenceService.class);

    @Spy
    @InjectMocks
    private QuarterValidationService validator;

    @DisplayName("throwExceptionWhenStartEndDateQuarterIsNull() should do nothing when Quarter Label is Backlog")
    @Test
    void throwExceptionWhenStartEndDateQuarterIsNullShouldDoNothingWhenQuarterLabelIsBacklog() {
        // arrange
        Quarter quarter = mock(Quarter.class);
        when(quarter.getLabel()).thenReturn(BACK_LOG_QUARTER_LABEL);

        // act
        QuarterValidationService.throwExceptionWhenStartEndDateQuarterIsNull(quarter);

        // assert
        verify(quarter, never()).getStartDate();
        verify(quarter, never()).getEndDate();
    }

    @DisplayName("throwExceptionWhenStartEndDateQuarterIsNull() should throw Exception when StartDate is null")
    @Test
    void throwExceptionWhenStartEndDateQuarterIsNullShouldThrowExceptionWhenStartDateIsNull() {
        // arrange
        Quarter quarter = mock(Quarter.class);
        when(quarter.getLabel()).thenReturn("no Backlog");
        when(quarter.getStartDate()).thenReturn(null);

        // act + assert
        OkrResponseStatusException okrResponseStatusException = assertThrows(OkrResponseStatusException.class,
                                                                             () -> QuarterValidationService
                                                                                     .throwExceptionWhenStartEndDateQuarterIsNull(quarter));
        assertEquals(BAD_REQUEST, okrResponseStatusException.getStatusCode());
    }

    @DisplayName("throwExceptionWhenStartEndDateQuarterIsNull() should throw Exception when EndDate is null")
    @Test
    void throwExceptionWhenStartEndDateQuarterIsNullShouldThrowExceptionWhenEndDateIsNull() {
        // arrange
        Quarter quarter = mock(Quarter.class);
        when(quarter.getLabel()).thenReturn("no Backlog");
        when(quarter.getStartDate()).thenReturn(LocalDate.now());
        when(quarter.getEndDate()).thenReturn(null);

        // act + assert
        OkrResponseStatusException okrResponseStatusException = assertThrows(OkrResponseStatusException.class,
                                                                             () -> QuarterValidationService
                                                                                     .throwExceptionWhenStartEndDateQuarterIsNull(quarter));
        assertEquals(BAD_REQUEST, okrResponseStatusException.getStatusCode());
    }

    @DisplayName("throwExceptionWhenStartEndDateQuarterIsNull should do nothing when both dates are not null")
    @Test
    void throwExceptionWhenStartEndDateQuarterIsNullShouldDoNothingWhenBothDatesAreNotNull() {
        // arrange
        Quarter quarter = mock(Quarter.class);
        when(quarter.getLabel()).thenReturn("no Backlog");
        when(quarter.getStartDate()).thenReturn(LocalDate.now());
        when(quarter.getEndDate()).thenReturn(LocalDate.now());

        // act
        QuarterValidationService.throwExceptionWhenStartEndDateQuarterIsNull(quarter);

        // assert (does nothing ... so nothing to check here)
    }

    @Test
    void validateOnCreateShouldThrowException() {
        Exception exception = assertThrows(IllegalCallerException.class, () -> validator.validateOnCreate(any()));
        assertEquals("This method must not be called", exception.getMessage());
    }

    @Test
    void validateOnUpdateShouldThrowException() {
        Exception exception = assertThrows(IllegalCallerException.class,
                                           () -> validator.validateOnUpdate(anyLong(), any()));
        assertEquals("This method must not be called because there is no update of quarters", exception.getMessage());
    }

    @DisplayName("validateOnGeneration() should throw exception when StartDate is null")
    @Test
    void validateOnGenerationShouldThrowExceptionWhenStartDateIsNull() {
        // arrange
        Quarter quarter = mock(Quarter.class);
        when(quarter.getStartDate()).thenReturn(null);
        when(quarter.getLabel()).thenReturn("Any Label");

        // act + assert
        OkrResponseStatusException okrResponseStatusException = assertThrows(OkrResponseStatusException.class,
                                                                             () -> validator
                                                                                     .validateOnGeneration(quarter));

        assertOkrResponseStatusException( //
                                         okrResponseStatusException, //
                                         List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("StartDate", "Any Label"))));
    }

    @DisplayName("validateOnGeneration() should throw exception when EndDate is null")
    @Test
    void validateOnGenerationShouldThrowExceptionWhenEndDateIsNull() {
        // arrange
        Quarter quarter = mock(Quarter.class);
        when(quarter.getStartDate()).thenReturn(LocalDate.now());
        when(quarter.getEndDate()).thenReturn(null);
        when(quarter.getLabel()).thenReturn("Any Label");

        // act + assert
        OkrResponseStatusException okrResponseStatusException = assertThrows(OkrResponseStatusException.class,
                                                                             () -> validator
                                                                                     .validateOnGeneration(quarter));

        assertOkrResponseStatusException( //
                                         okrResponseStatusException, //
                                         List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("EndDate", "Any Label"))));
    }

    @DisplayName("validateOnGeneration() should do nothing when both dates are not null")
    @Test
    void validateOnGenerationShouldDoNothingWhenBothDatesAreNotNull() {
        // arrange
        Quarter quarter = mock(Quarter.class);
        when(quarter.getStartDate()).thenReturn(LocalDate.now());
        when(quarter.getEndDate()).thenReturn(LocalDate.now());

        // act
        validator.validateOnGeneration(quarter);

        // assert (does nothing ... so nothing to check here)
    }

    private void assertOkrResponseStatusException(OkrResponseStatusException exception, List<ErrorDto> expectedErrors) {
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

}
