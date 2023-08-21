package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistance.QuarterPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class QuarterValidationService extends ValidationBase<Quarter, Long> {

    public QuarterValidationService(QuarterPersistenceService quarterPersistenceService) {
        super(quarterPersistenceService);
    }
    @Override
    public void validateOnCreate(Quarter model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Quarter model) {

    }
}
