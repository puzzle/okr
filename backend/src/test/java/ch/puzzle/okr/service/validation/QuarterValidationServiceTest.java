package ch.puzzle.okr.service.validation;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuarterValidationServiceTest {
    @MockBean
    QuarterPersistenceService quarterPersistenceService = Mockito.mock(QuarterPersistenceService.class);

    @Spy
    @InjectMocks
    private QuarterValidationService validator;

    @Test
    void validateOnGet_ShouldBeSuccessfulWhenValidId() {
        assertDoesNotThrow(() -> validator.validateOnGet(1L));
        verify(validator, times(1)).validateOnGet(anyLong());
    }

    @Test
    void validateOnGet_ShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        assertEquals("Id is null", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void validateOnCreate_ShouldThrowException() {
        Exception exception = assertThrows(IllegalCallerException.class, () -> validator.validateOnCreate(any()));
        assertEquals("This method must not be called", exception.getMessage());
    }

    @Test
    void validateOnUpdate_ShouldThrowException() {
        Exception exception = assertThrows(IllegalCallerException.class,
                () -> validator.validateOnUpdate(anyLong(), any()));
        assertEquals("This method must not be called because there is no update of quarters", exception.getMessage());
    }
}
