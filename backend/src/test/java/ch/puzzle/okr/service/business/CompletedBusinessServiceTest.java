package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.persistence.CompletedPersistenceService;
import ch.puzzle.okr.service.validation.CompletedValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompletedBusinessServiceTest {
    @MockBean
    CompletedPersistenceService completedPersistenceService = Mockito.mock(CompletedPersistenceService.class);
    @InjectMocks
    CompletedValidationService validator = Mockito.mock(CompletedValidationService.class);;

    Completed successfulCompleted = Completed.Builder.builder().withId(1L)
            .withObjective(Objective.Builder.builder().withId(3L).withTitle("Gute Lernende").build())
            .withComment("Wir haben es gut geschafft").build();

    @InjectMocks
    @Spy
    private CompletedBusinessService completedBusinessService;

    @Test
    void saveSuccessFulCompleted() {
        Mockito.when(completedPersistenceService.save(any())).thenReturn(successfulCompleted);

        Completed completed = Completed.Builder.builder()
                .withObjective(Objective.Builder.builder().withId(4L)
                        .withTitle("Build a company culture that kills the competition.").build())
                .withComment("Das ist gut").build();

        Completed savedCompleted = completedBusinessService.createCompleted(completed);
        verify(completedPersistenceService, times(1)).save(completed);
        assertEquals(successfulCompleted, savedCompleted);
    }

    @Test
    void shouldBePossibleToSaveCompletedWithoutComment() {
        Completed completed = Completed.Builder.builder().withObjective(Objective.Builder.builder().withId(4L)
                .withTitle("Build a company culture that kills the competition.").build()).build();

        Mockito.when(completedPersistenceService.save(any())).thenReturn(successfulCompleted);

        Completed savedCompleted = completedBusinessService.createCompleted(completed);
        verify(completedPersistenceService, times(1)).save(completed);
        assertEquals(savedCompleted, successfulCompleted);
    }

    @Test
    void shouldDeleteKeyResultAndAssociatedCheckIns() {
        when(completedPersistenceService.getCompletedByObjectiveId(anyLong())).thenReturn(successfulCompleted);

        this.completedBusinessService.deleteCompletedByObjectiveId(6L);

        verify(this.completedPersistenceService, times(1)).deleteById(1L);
    }
}
