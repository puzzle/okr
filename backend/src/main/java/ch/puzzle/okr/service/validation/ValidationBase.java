package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.service.persistence.PersistenceBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <ID>
 *            the Identifier or primary key of the entity
 * @param <R>
 *            the Repository of the entity
 * @param <PS>
 *            the Persistence Service of this repository and entity
 */
public abstract class ValidationBase<T, ID, R, PS extends PersistenceBase<T, ID, R>> {
    private final Validator validator;
    private final PS persistenceService;

    ValidationBase(PS persistenceService) {
        this.persistenceService = persistenceService;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public PS getPersistenceService() {
        return persistenceService;
    }

    public void validateOnGet(ID id) {
        throwExceptionWhenIdIsNull(id);
    }

    public abstract void validateOnCreate(T model);

    public abstract void validateOnUpdate(ID id, T model);

    public void validateOnDelete(ID id) {
        throwExceptionWhenIdIsNull(id);
        doesEntityExist(id);
    }

    public void doesEntityExist(ID id) {
        persistenceService.findById(id);
    }

    public void throwExceptionIfModelIsNull(T model) {
        if (model == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Given model %s is null", persistenceService.getModelName()));
        }
    }

    public void throwExceptionWhenIdIsNull(ID id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is null");
        }
    }

    protected void throwExceptionWhenIdIsNotNull(ID id) {
        if (id != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(
                    "Model %s cannot have id while create. Found id %s", persistenceService.getModelName(), id));
        }
    }

    protected void throwExceptionWhenIdHasChanged(ID id, ID modelId) {
        if (!Objects.equals(id, modelId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Id %s has changed to %s during update", id, modelId));
        }
    }

    public void validate(T model) {
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        processViolations(violations);
    }

    private void processViolations(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            List<String> reasons = violations.stream().map(ConstraintViolation::getMessage).toList();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(". ", reasons) + ".");
        }
    }
}
