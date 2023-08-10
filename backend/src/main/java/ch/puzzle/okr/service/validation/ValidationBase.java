package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

public abstract class ValidationBase<Id, Model> {
    private final Validator validator;

    ValidationBase() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public abstract void validateOnGet(Id id);

    public abstract void validateOnCreate(Model model);

    public abstract void validateOnUpdate(Long id, Team model);

    public abstract void validateOnDelete(Id id);

    protected abstract String modelName();

    public abstract void doesEntityExist(Id id, String modelName);

    public void isModelNull(Model model, String modelName) {
        if (model == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Can not save undefined %s", modelName));
        }
    }

    public void isIdNull(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is null");
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
