package ch.puzzle.okr.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.persistence.CompletedPersistenceService;
import ch.puzzle.okr.service.validation.CompletedValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompletedBusinessServiceTest {
    @Mock
    CompletedPersistenceService completedPersistenceService;
    @Mock
    CompletedValidationService validator;

    Completed successfulCompleted = Completed.Builder
            .builder()
            .withId(1L)
            .withObjective(Objective.Builder.builder().withId(3L).withTitle("Gute Lernende").build())
            .withComment("Wir haben es gut geschafft")
            .build();

    @InjectMocks
    @Spy
    private CompletedBusinessService completedBusinessService;

    @DisplayName("Should successfully create completed")
    @Test
    void shouldSuccessfullyCreateCompleted() {
        Mockito.when(completedPersistenceService.save(any())).thenReturn(successfulCompleted);

        Completed completed = Completed.Builder
                .builder()
                .withObjective(Objective.Builder
                        .builder()
                        .withId(4L)
                        .withTitle("Build a company culture that kills the competition.")
                        .build())
                .withComment("Das ist gut")
                .build();

        Completed savedCompleted = completedBusinessService.createCompleted(completed);
        verify(completedPersistenceService, times(1)).save(completed);
        assertEquals(successfulCompleted, savedCompleted);
    }

    @DisplayName("Should be possible to save completed without comment")
    @Test
    void shouldBePossibleToSaveCompletedWithoutComment() {
        Completed completed = Completed.Builder
                .builder()
                .withObjective(Objective.Builder
                        .builder()
                        .withId(4L)
                        .withTitle("Build a company culture that kills the competition.")
                        .build())
                .build();

        Mockito.when(completedPersistenceService.save(any())).thenReturn(successfulCompleted);

        Completed savedCompleted = completedBusinessService.createCompleted(completed);
        verify(completedPersistenceService, times(1)).save(completed);
        assertEquals(savedCompleted, successfulCompleted);
    }

    @DisplayName("Should delete all key-results and associated check-ins")
    @Test
    void shouldDeleteKeyResultAndAssociatedCheckIns() {
        when(completedPersistenceService.getCompletedByObjectiveId(anyLong())).thenReturn(successfulCompleted);

        this.completedBusinessService.deleteCompletedByObjectiveId(6L);

        verify(this.completedPersistenceService, times(1)).deleteById(1L);
    }

    @DisplayName("Should do nothing if completed to delete is null")
    @Test
    void shouldDoNothingIfCompletedIsNull() {
        when(completedPersistenceService.getCompletedByObjectiveId(anyLong())).thenReturn(null);

        this.completedBusinessService.deleteCompletedByObjectiveId(6L);

        verify(validator, never()).validateOnDelete(anyLong());
    }

    @DisplayName("Should get completed by objective id")
    @Test
    void shouldGetCompleted() {
        when(completedPersistenceService.getCompletedByObjectiveId(anyLong())).thenReturn(successfulCompleted);

        this.completedBusinessService.getCompletedByObjectiveId(1L);

        verify(this.completedPersistenceService, times(1)).getCompletedByObjectiveId(1L);
    }

    @DisplayName("Should throw exception if completed is null")
    @Test
    void shouldThrowExceptionIfCompletedIsNull() {
        when(completedPersistenceService.getCompletedByObjectiveId(-1L)).thenReturn(null);
        when(completedPersistenceService.getModelName()).thenCallRealMethod();

        assertThrows(OkrResponseStatusException.class, () -> completedBusinessService.getCompletedByObjectiveId(-1L));

        verify(this.completedPersistenceService, times(1)).getCompletedByObjectiveId(-1L);
    }

}
