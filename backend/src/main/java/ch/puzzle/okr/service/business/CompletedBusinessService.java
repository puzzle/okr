package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.service.persistence.CompletedPersistenceService;
import ch.puzzle.okr.service.validation.CompletedValidationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
