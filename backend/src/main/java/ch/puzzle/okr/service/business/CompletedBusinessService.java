package ch.puzzle.okr.service.business;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.persistence.CompletedPersistenceService;
import ch.puzzle.okr.service.validation.CompletedValidationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CompletedBusinessService {
    private final CompletedPersistenceService completedPersistenceService;
    private final CompletedValidationService validator;

    public CompletedBusinessService(CompletedPersistenceService completedPersistenceService,
                                    CompletedValidationService validator) {
        this.completedPersistenceService = completedPersistenceService;
        this.validator = validator;
    }

    @Transactional
    public Completed createCompleted(Completed completed) {
        validator.validateOnCreate(completed);
        return completedPersistenceService.save(completed);
    }

    @Transactional
    public void deleteCompletedByObjectiveId(Long objectiveId) {
        Completed completed = completedPersistenceService.getCompletedByObjectiveId(objectiveId);
        if (completed != null) {
            validator.validateOnDelete(completed.getId());
            completedPersistenceService.deleteById(completed.getId());
        }
    }

    public Completed getCompletedByObjectiveId(Long objectiveId) {
        validator.validateOnGet(objectiveId);
        return completedPersistenceService.getCompletedByObjectiveId(objectiveId);
    }
}
