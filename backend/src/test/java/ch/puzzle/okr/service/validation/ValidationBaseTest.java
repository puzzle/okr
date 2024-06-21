package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ValidationBaseTest {
    @MockBean
    QuarterPersistenceService quarterPersistenceService = Mockito.mock(QuarterPersistenceService.class);

    @Spy
    @InjectMocks
    private DummyValidationService validator;

    private void assertOkrResponseStatusException(OkrResponseStatusException exception, List<ErrorDto> expectedErrors) {
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("validateOnGet() should be successful when Id valid")
    @Test
    void validateOnGetShouldBeSuccessfulWhenValidId() {
        assertDoesNotThrow(() -> validator.validateOnGet(1L));
        verify(validator, times(1)).validateOnGet(anyLong());
    }

    @DisplayName("validateOnGet() should throw exception when Id is null")
    @Test
    void validateOnGetShouldThrowExceptionWhenIdIsNull() {
        // arrange
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        // assert
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Quarter")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("validateOnDelete() should be successful when Id is valid")
    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidId() {
        // arrange
        long id = 1L;
        Quarter quarter = Quarter.Builder.builder().withId(id).withLabel("Quarter").build();
        when(quarterPersistenceService.findById(id)).thenReturn(quarter);

        // act
        Quarter validatedQuarter = validator.validateOnDelete(id);

        // assert
        assertEquals(quarter, validatedQuarter);
    }

    @DisplayName("validateOnDelete() should throw exception when Id is null")
    @Test
    void validateOnDeleteShouldThrowExceptionWhenIdIsNull() {
        // arrange
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnDelete(null));

        // assert
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Quarter")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("throwExceptionWhenModelIsNull()  should should be successful when model is valid")
    @Test
    void throwExceptionWhenModelIsNullShouldShouldBeSuccessfulWhenModelIsValid() {
        // act
        Quarter model = Quarter.Builder.builder().withId(1L).withLabel("Quarter").build();

        // act + assert
        assertDoesNotThrow(() -> validator.throwExceptionWhenModelIsNull(model));
        verify(quarterPersistenceService, never()).getModelName();
    }

    @DisplayName("throwExceptionWhenModelIsNull() should throw exception when model is null")
    @Test
    void throwExceptionWhenModelIsNullShouldThrowExceptionWhenModelIsNull() {
        // arrange
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.throwExceptionWhenModelIsNull(null));

        // assert
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("Quarter")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("throwExceptionWhenIdIsNotNull()  should be successful when model is null")
    @Test
    void throwExceptionWhenIdIsNotNullShouldBeSuccessfulWhenModelIsNull() {
        assertDoesNotThrow(() -> validator.throwExceptionWhenIdIsNotNull(null));
        verify(quarterPersistenceService, never()).getModelName();
    }

    @DisplayName("throwExceptionWhenIdIsNotNull() should throw exception when model is not null")
    @Test
    void throwExceptionWhenModelIsNullShouldThrowExceptionWhenModelIsNotNull() {
        // arrange
        long id = 1L;
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.throwExceptionWhenIdIsNotNull(id));

        // assert
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "Quarter")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("throwExceptionWhenIdHasChanged() should be successful when Ids are equal")
    @Test
    void throwExceptionWhenIdHasChangedShouldBeSuccessfulWhenIdsAreEqual() {
        Long id = 1L;
        Long modelId = 1L;
        assertDoesNotThrow(() -> validator.throwExceptionWhenIdHasChanged(id, modelId));
        verify(quarterPersistenceService, never()).getModelName();
    }

    @DisplayName("throwExceptionWhenIdHasChanged() should throw exception when Ids are not equal")
    @Test
    void throwExceptionWhenIdHasChangedShouldThrowExceptionWhenIdsAreNotEqual() {
        // arrange
        Long id = 1L;
        Long modelId = 2L;
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.throwExceptionWhenIdHasChanged(id, modelId));

        // assert
        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", id.toString(), modelId.toString())));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("validate() should throw exception when when constraint in model class is violated")
    @Test
    void validateShouldThrowExceptionWhenWhenConstraintInModelClassIsViolated() {
        // arrange
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // Quarter which violates the not null constraint in Quarter.java model class
        Quarter quarterWithNullLabel = Quarter.Builder.builder().withLabel(null).build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validate(quarterWithNullLabel));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("label", "Quarter")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("validate() should be successful when constraint in model class is not violated")
    @Test
    void validateShouldBeSuccessfulWhenConstraintInModelClassIsNotViolated() {
        // arrange
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");
        Quarter quarterWithValidLabel = Quarter.Builder.builder().withLabel("Quarter").build();

        // act + assert
        assertDoesNotThrow(() -> validator.validate(quarterWithValidLabel));
        verify(quarterPersistenceService, never()).getModelName();
    }

    static class DummyValidationService
            extends ValidationBase<Quarter, Long, QuarterRepository, QuarterPersistenceService> {

        public DummyValidationService(QuarterPersistenceService quarterPersistenceService) {
            super(quarterPersistenceService);
        }

        @Override
        public void validateOnCreate(Quarter model) {
        }

        @Override
        public void validateOnUpdate(Long aLong, Quarter model) {
        }
    }
}
