package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveValidationService extends ValidationBase<Objective, Long> {

    public ObjectiveValidationService(ObjectivePersistenceService objectivePersistenceService) {
        super(objectivePersistenceService);
    }

    @Override
    public void validateOnCreate(Objective model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Objective model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        doesEntityExist(id);

        validate(model);
    }
}
