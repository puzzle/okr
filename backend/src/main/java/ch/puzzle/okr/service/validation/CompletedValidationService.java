package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.service.persistence.CompletedPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class CompletedValidationService extends ValidationBase<Completed, Long> {

    public CompletedValidationService(CompletedPersistenceService completedPersistenceService) {
        super(completedPersistenceService);
    }

    @Override
    public void validateOnCreate(Completed model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Completed model) {
        throw new IllegalCallerException("This method must not be called because there is no update of completed");
    }
}
