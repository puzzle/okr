package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Team;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class ValidationService {

    private final Validator validator;

    public ValidationService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public void validateOnSave(Team team) {
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not save undefined team");
        }
        if (team.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give an id");
        }
        validate(team);
    }

    public void validateOnUpdate(Team team) {
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not update undefined team");
        }
        if (team.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute team id");
        }
        validate(team);
    }

    private void validate(Team team) {
        Set<ConstraintViolation<Team>> violations = validator.validate(team);
        processViolations(violations);
    }

    private void processViolations(Set<ConstraintViolation<Team>> violations) {
        if (!violations.isEmpty()) {
            List<String> reasons = violations.stream().map(ConstraintViolation::getMessage).toList();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(". ", reasons) + ".");
        }
    }
}
