package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.QuarterRepository;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
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

import static ch.puzzle.okr.test.AssertionHelper.assertOkrResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationBaseTest {
    @MockBean
    QuarterPersistenceService quarterPersistenceService = Mockito.mock(QuarterPersistenceService.class);

    @MockBean
    ObjectivePersistenceService objectivePersistenceService = Mockito.mock(ObjectivePersistenceService.class);

    @Spy
    @InjectMocks
    private DummyValidationService validator;

    @InjectMocks
    private DummyValidationServiceWithSeveralConstraints validatorWithSeveralConstraints;

    @DisplayName("getPersistenceService() should return not null")
    @Test
    void getPersistenceServiceShouldReturnNotNull() {
        QuarterPersistenceService persistenceService = validator.getPersistenceService();
        assertNotNull(persistenceService);
    }

    @DisplayName("should be successful when Id is valid")
    @Test
    void validateOnGetShouldBeSuccessfulWhenIdIsValid() {
        Long id = 1L;
        assertDoesNotThrow(() -> validator.validateOnGet(id));
        verify(validator, times(1)).validateOnGet(anyLong());
    }

    @DisplayName("should throw exception when Id is null")
    @Test
    void validateOnGetShouldThrowExceptionWhenIdIsNull() {
        // arrange
        Long id = null;
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(id));

        // assert
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Quarter")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("should be successful when Id is valid")
    @Test
    void validateOnDeleteShouldBeSuccessfulWhenIdIsValid() {
        // arrange
        Long id = 1L;
        Quarter quarter = Quarter.Builder.builder().withId(id).withLabel("Quarter").build();
        when(quarterPersistenceService.findById(id)).thenReturn(quarter);

        // act
        Quarter validatedQuarter = validator.validateOnDelete(id);

        // assert
        assertEquals(quarter, validatedQuarter);
    }

    @DisplayName("should throw exception when Id is null")
    @Test
    void validateOnDeleteShouldThrowExceptionWhenIdIsNull() {
        // arrange
        Long id = null;
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnDelete(id));

        // assert
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Quarter")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("should not throw exception when model is valid")
    @Test
    void throwExceptionWhenModelIsNullShouldBeSuccessfulWhenModelIsValid() {
        // act
        Long id = 1L;
        Quarter model = Quarter.Builder.builder().withId(id).withLabel("Quarter").build();

        // act + assert
        assertDoesNotThrow(() -> validator.throwExceptionWhenModelIsNull(model));
        verify(quarterPersistenceService, never()).getModelName();
    }

    @DisplayName("should throw exception when model is null")
    @Test
    void throwExceptionWhenModelIsNullShouldThrowExceptionWhenModelIsNull() {
        // arrange
        Quarter model = null;
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.throwExceptionWhenModelIsNull(model));

        // assert
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("Quarter")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("should be successful exception when Id is null")
    @Test
    void throwExceptionWhenIdIsNotNullShouldBeSuccessfulWhenIdIsNull() {
        Long id = null;
        assertDoesNotThrow(() -> validator.throwExceptionWhenIdIsNotNull(id));
        verify(quarterPersistenceService, never()).getModelName();
    }

    @DisplayName("should throw exception when Id is not null")
    @Test
    void throwExceptionWhenIdIsNotNullShouldThrowExceptionWhenIdIsNotNull() {
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

    @DisplayName("should be successful when Ids are equal")
    @Test
    void throwExceptionWhenIdHasChangedShouldBeSuccessfulWhenIdsAreEqual() {
        Long id = 1L;
        Long modelId = 1L;
        assertDoesNotThrow(() -> validator.throwExceptionWhenIdHasChanged(id, modelId));
        verify(quarterPersistenceService, never()).getModelName();
    }

    @DisplayName("should throw exception when Ids are not equal")
    @Test
    void throwExceptionWhenIdHasChangedShouldThrowExceptionWhenIdsAreNotEqual() {
        // arrange
        Long id = 1L;
        Long modelId = 2L;
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.throwExceptionWhenIdHasChanged(id, modelId));

        // assert
        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", id.toString(), modelId.toString())));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("should be successful when constraint in model class is not violated")
    @Test
    void validateShouldBeSuccessfulWhenConstraintInModelClassIsNotViolated() {
        // arrange
        Quarter quarterWithValidLabel = Quarter.Builder.builder().withLabel("Quarter").build();
        Mockito.when(objectivePersistenceService.getModelName()).thenReturn("Quarter");

        // act + assert
        assertDoesNotThrow(() -> validator.validate(quarterWithValidLabel));
        verify(quarterPersistenceService, never()).getModelName();
    }

    @DisplayName("should throw exception when when constraint in model class is violated")
    @Test
    void validateShouldThrowExceptionWhenWhenConstraintInModelClassIsViolated() {
        // arrange
        // Quarter which violates the NotNull constraint in Quarter model class
        Quarter quarterWithNullLabel = Quarter.Builder.builder().withLabel(null).build();
        Mockito.when(quarterPersistenceService.getModelName()).thenReturn("Quarter");

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validate(quarterWithNullLabel));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("label", "Quarter")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("should throw exception when one of several constraints in model class is violated")
    @Test
    void validateShouldThrowExceptionWhenOneOfSeveralConstraintsInModelClassIsViolated() {
        // arrange
        // Objective which violates several constraints in Objective model class
        Objective objective = Objective.Builder.builder().withTitle("X").build();
        Mockito.when(objectivePersistenceService.getModelName()).thenReturn("Objective");

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validatorWithSeveralConstraints.validate(objective));

        List<ErrorDto> expectedErrors = List.of( //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("team", "Objective")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "Objective")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "Objective")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("state", "Objective")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("quarter", "Objective")), //
                new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "Objective", "2", "250")) //
        );
        assertOkrResponseStatusException(exception, expectedErrors);
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

    static class DummyValidationServiceWithSeveralConstraints
            extends ValidationBase<Objective, Long, ObjectiveRepository, ObjectivePersistenceService> {

        public DummyValidationServiceWithSeveralConstraints(ObjectivePersistenceService objectivePersistenceService) {
            super(objectivePersistenceService);
        }

        @Override
        public void validateOnCreate(Objective model) {
        }

        @Override
        public void validateOnUpdate(Long aLong, Objective model) {
        }
    }

}
