package ch.puzzle.okr.service.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

public abstract class ValidationBase<Model> {
    private final Validator validator;

    ValidationBase() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public abstract void validateOnUpdate(Model model);

    public abstract void validateOnSave(Model model);

    public void isModelNull(Model model) {
        if (model == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not save undefined team");
        }
    }

    public void isIdNull(Long id) {
        if (id != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give an id");
        }
    }

    protected void validate(Model team) {
        Set<ConstraintViolation<Model>> violations = validator.validate(team);
        processViolations(violations);
    }

    private void processViolations(Set<ConstraintViolation<Model>> violations) {
        if (!violations.isEmpty()) {
            List<String> reasons = violations.stream().map(ConstraintViolation::getMessage).toList();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(". ", reasons) + ".");
        }
    }
}
