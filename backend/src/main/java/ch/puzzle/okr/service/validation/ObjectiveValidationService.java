package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.persistance.ObjectivePersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ObjectiveValidationService extends ValidationBase<Objective, Long> {

    public ObjectiveValidationService(ObjectivePersistenceService objectivePersistenceService) {
        super(objectivePersistenceService);
    }

    @Override
    public void validateOnCreate(Objective model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        // Progress can't be set when creating new objective
        if (model.getProgress() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give a progress");
        }
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
