package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.models.OkrResponseStatusException;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

        assertEquals(BAD_REQUEST, exception.getStatus());
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
}
