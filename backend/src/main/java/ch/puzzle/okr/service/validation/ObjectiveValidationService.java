package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ObjectiveValidationService extends ValidationBase<Objective, Long, ObjectiveRepository> {

    public ObjectiveValidationService(ObjectivePersistenceService objectivePersistenceService) {
        super(objectivePersistenceService);
    }

    @Override
    public void validateOnCreate(Objective model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        if (model.getModifiedBy() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Not allowed to set ModifiedBy %s on create", model.getModifiedBy()));
        }
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Objective model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenIdHasChanged(id, model.getId());
        if (model.getModifiedBy() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Something went wrong. ModifiedBy %s is not set.", model.getModifiedBy()));
        }
        doesEntityExist(id);
        validate(model);
    }
}
