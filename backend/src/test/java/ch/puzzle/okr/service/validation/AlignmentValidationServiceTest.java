package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlignmentValidationServiceTest {
    private final Alignment keyResultAlignment = new KeyResultAlignment();
    @MockitoBean
    AlignmentPersistenceService alignmentPersistenceService = Mockito.mock(AlignmentPersistenceService.class);
    @Spy
    @InjectMocks
    private AlignmentValidationService validator;

    @DisplayName("Should throw exception on validateOnCreate()")
    @Test
    void shouldThrowExceptionOnValidateOnCreate() {
        assertThrows(UnsupportedOperationException.class, () -> validator.validateOnCreate(keyResultAlignment));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNull() {
        when(alignmentPersistenceService.getModelName()).thenReturn("Alignment");

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(null, keyResultAlignment));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(List.of(new ErrorDto(ErrorKey.ATTRIBUTE_NULL.name(), List.of("ID", "Alignment"))),
                exception.getErrors());
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id of alignment is null")
    @Test
    void shouldThrowExceptionOnValidateOnValidateOnUpdateWhenIdOfAlignmentIsNull() {
        when(alignmentPersistenceService.getModelName()).thenReturn("Alignment");
        Alignment alignment = KeyResultAlignment.Builder.builder().withId(null).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, alignment));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(List.of(new ErrorDto(ErrorKey.ATTRIBUTE_NULL.name(), List.of("ID", "Alignment"))),
                exception.getErrors());
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

    }

    @DisplayName("Should throw exception on validateOnUpdate() when id has changed")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdHasChanged() {
        Alignment alignment = KeyResultAlignment.Builder.builder().withId(3L).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, alignment));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(List.of(new ErrorDto(ErrorKey.ATTRIBUTE_CHANGED.name(), List.of("ID", "1", "3"))),
                exception.getErrors());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(1L, 3L);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when model is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenModelIsInvalid() {
        when(alignmentPersistenceService.getModelName()).thenReturn("Alignment");
        Alignment alignment = KeyResultAlignment.Builder.builder().withId(2L).withAlignedObjective(null).withVersion(1)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, alignment));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(
                List.of(new ErrorDto("Aligned objective must not be null", List.of("alignedObjective", "Alignment"))),
                exception.getErrors());
        verify(validator, times(1)).validate(alignment);
    }
}