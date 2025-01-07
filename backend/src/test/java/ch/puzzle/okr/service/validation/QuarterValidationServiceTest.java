package ch.puzzle.okr.service.validation;

import static ch.puzzle.okr.Constants.BACK_LOG_QUARTER_LABEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.test.TestHelper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuarterValidationServiceTest {

    @Spy
    @InjectMocks
    private QuarterValidationService validator;

    @DisplayName("Should do nothing on throwExceptionWhenStartEndDateQuarterIsNull() when quarter label is backlog")
    @Test
    void shouldThrowExceptionWhenStartEndDateQuarterIsNullShouldDoNothingWhenQuarterLabelIsBacklog() {
        // arrange
        Quarter quarter = mock(Quarter.class);
        when(quarter.getLabel()).thenReturn(BACK_LOG_QUARTER_LABEL);

        // act
        QuarterValidationService.throwExceptionWhenStartEndDateQuarterIsNull(quarter);

        // assert
        verify(quarter, never()).getStartDate();
        verify(quarter, never()).getEndDate();
    }

    @DisplayName("Should throw exception on throwExceptionWhenStartEndDateQuarterIsNull() when start date is null")
    @Test
    void shouldThrowExceptionWhenStartEndDateQuarterIsNullShouldThrowExceptionWhenStartDateIsNull() {
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

    @DisplayName("Should throw exception on throwExceptionWhenStartEndDateQuarterIsNull() when end date is null")
    @Test
    void shouldThrowExceptionWhenStartEndDateQuarterIsNullShouldThrowExceptionWhenEndDateIsNull() {
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

    @DisplayName("Should do nothing on throwExceptionWhenStartEndDateQuarterIsNull() when both dates are not null")
    @Test
    void shouldThrowExceptionWhenStartEndDateQuarterIsNullShouldDoNothingWhenBothDatesAreNotNull() {
        // arrange
        Quarter quarter = mock(Quarter.class);
        when(quarter.getLabel()).thenReturn("no Backlog");
        when(quarter.getStartDate()).thenReturn(LocalDate.now());
        when(quarter.getEndDate()).thenReturn(LocalDate.now());

        // act
        assertDoesNotThrow(() -> QuarterValidationService.throwExceptionWhenStartEndDateQuarterIsNull(quarter));

        // assert (does nothing ... so nothing to check here)
    }

    @DisplayName("Should throw exception when validateOnCreate() is called")
    @Test
    void shouldThrowExceptionOnValidateOnCreate() {
        Quarter any = any();
        Exception exception = assertThrows(IllegalCallerException.class, () -> validator.validateOnCreate(any));
        assertEquals("This method must not be called", exception.getMessage());
    }

    @DisplayName("Should throw exception when validateOnUpdate() is called")
    @Test
    void shouldThrowExceptionOnValidateOnUpdate() {
        Quarter any = any();
        Long anyLong = anyLong();

        Exception exception = assertThrows(IllegalCallerException.class,
                                           () -> validator.validateOnUpdate(anyLong, any));
        assertEquals("This method must not be called because there is no update of quarters", exception.getMessage());
    }

    @DisplayName("Should throw exception on validateOnGeneration() when start date is null")
    @Test
    void shouldThrowExceptionOnValidateOnGenerationWhenStartDateIsNull() {
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

    @DisplayName("Should throw exception on validateOnGeneration() when end date is null")
    @Test
    void shouldThrowExceptionOnValidateOnGenerationWhenBothDatesAreNull() {
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

    @DisplayName("Should do nothing on validateOnGeneration() when both dates are not null")
    @Test
    void shouldDoNothingOnValidateOnGenerationWhenBothDatesAreNotNull() {
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
