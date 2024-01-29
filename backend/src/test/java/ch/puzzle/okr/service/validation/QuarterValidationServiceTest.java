package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class QuarterValidationServiceTest {
    @MockBean
    QuarterPersistenceService quarterPersistenceService = Mockito.mock(QuarterPersistenceService.class);

    @Spy
    @InjectMocks
    private QuarterValidationService validator;

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidId() {
        assertDoesNotThrow(() -> validator.validateOnGet(1L));
        verify(validator, times(1)).validateOnGet(anyLong());
    }

    @Test
    void validateOnGetShouldThrowExceptionWhenIdIsNull() {
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Quarter")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
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

    @Test
    void validateOnGenerationShouldThrowExceptionWhenStartDateIsNull() {
        Quarter quarter = Quarter.Builder.builder().withLabel("GJ 22/23-Q2").withEndDate(LocalDate.of(2022, 11, 1))
                .build();
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGeneration(quarter));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("StartDate", "GJ 22/23-Q2")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnGenerationShouldThrowExceptionWhenEndDateIsNull() {
        Quarter quarter = Quarter.Builder.builder().withLabel("GJ 22/23-Q2").withStartDate(LocalDate.of(2022, 11, 1))
                .build();
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGeneration(quarter));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("EndDate", "GJ 22/23-Q2")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }
}
