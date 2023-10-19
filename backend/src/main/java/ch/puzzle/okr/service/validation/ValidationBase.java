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

public abstract class ValidationBase<T, E, R> {
    private final Validator validator;
    protected final PersistenceBase<T, E, R> persistenceService;

    ValidationBase(PersistenceBase<T, E, R> persistenceService) {
        this.persistenceService = persistenceService;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public void validateOnGet(E id) {
        throwExceptionWhenIdIsNull(id);
    }

    public abstract void validateOnCreate(T model);

    public abstract void validateOnUpdate(E id, T model);

    public void validateOnDelete(E id) {
        throwExceptionWhenIdIsNull(id);
        doesEntityExist(id);
    }

    public void doesEntityExist(E id) {
        persistenceService.findById(id);
    }

    public void throwExceptionIfModelIsNull(T model) {
        if (model == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Given model %s is null", persistenceService.getModelName()));
        }
    }

    public void throwExceptionWhenIdIsNull(E id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is null");
        }
    }

    protected void throwExceptionWhenIdIsNotNull(E id) {
        if (id != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(
                    "Model %s cannot have id while create. Found id %s", persistenceService.getModelName(), id));
        }
    }

    protected void throwExceptionWhenIdHasChanged(E id, E modelId) {
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
