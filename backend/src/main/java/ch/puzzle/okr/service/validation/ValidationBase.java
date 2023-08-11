package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import org.springframework.data.repository.CrudRepository;
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
    protected final CrudRepository<Model, Id> repository;

    ValidationBase(CrudRepository<Model, Id> repository) {
        this.repository = repository;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public abstract void validateOnGet(Id id);

    public abstract void validateOnCreate(Model model);

    public abstract void validateOnUpdate(Long id, Team model);

    public abstract void validateOnDelete(Id id);

    protected abstract String modelName();

    protected void doesEntityExist(Id id) {
        repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("%s with id %s not found", modelName(), id)));
    }

    protected void isModelNull(Model model) {
        if (model == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Given model %s is null", modelName()));
        }
    }

    protected void isIdNull(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is null");
        }
    }

    protected void validate(Model model) {
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
