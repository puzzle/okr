package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Quarter;

import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class QuarterValidationService extends ValidationBase<Quarter, Long> {

    public QuarterValidationService(QuarterPersistenceService quarterPersistenceService) {
        super(quarterPersistenceService);
    }

    @Override
    public void validateOnCreate(Quarter model) {
    }

    @Override
    public void validateOnUpdate(Long id, Quarter model) {
    }

    public void validateOnSave(Quarter model) {
        throwExceptionIfModelIsNull(model);
        validate(model);
    }
}
