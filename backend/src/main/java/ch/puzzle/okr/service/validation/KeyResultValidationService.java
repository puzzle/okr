package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class KeyResultValidationService extends ValidationBase<KeyResult, Long> {

    public KeyResultValidationService(KeyResultPersistenceService keyResultPersistenceService) {
        super(keyResultPersistenceService);
    }

    @Override
    public void validateOnCreate(KeyResult model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, KeyResult model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        doesEntityExist(id);

        validate(model);
    }
}
