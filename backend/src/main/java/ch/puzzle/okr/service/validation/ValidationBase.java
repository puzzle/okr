package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.service.persistance.PersistenceBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

public abstract class ValidationBase<Model, Id> {
    private final Validator validator;
    protected final PersistenceBase<Model, Id> persistenceService;

    ValidationBase(PersistenceBase<Model, Id> persistenceService) {
        this.persistenceService = persistenceService;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public void validateOnGet(Id id) {
        throwExceptionWhenIdIsNull(id);
    }

    public abstract void validateOnCreate(Model model);

    public abstract void validateOnUpdate(Id id, Model model);

    public void validateOnDelete(Id id) {
        throwExceptionWhenIdIsNull(id);
        doesEntityExist(id);
    }

    public void doesEntityExist(Id id) {
        persistenceService.findById(id);
    }

    protected void isModelNull(Model model) {
        if (model == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Given model %s is null", persistenceService.getModelName()));
        }
    }

    protected void throwExceptionWhenIdIsNull(Id id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is null");
        }
    }

    protected void throwExceptionWhenIdIsNotNull(Id id) {
        if (id != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(
                    "Model %s cannot have id while create. Found id %s", persistenceService.getModelName(), id));
        }
    }

    public void validate(Model model) {
        Set<ConstraintViolation<Model>> violations = validator.validate(model);
        processViolations(violations);
    }

    private void processViolations(Set<ConstraintViolation<Model>> violations) {
        if (!violations.isEmpty()) {
            List<String> reasons = violations.stream().map(ConstraintViolation::getMessage).toList();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(". ", reasons) + ".");
        }
    }
}
