package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Quarter;

import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class QuarterValidationService extends ValidationBase<Quarter, Long> {

    public QuarterValidationService(QuarterPersistenceService quarterPersistenceService) {
        super(quarterPersistenceService);
    }

    @Override
    public void validateOnCreate(Quarter model) {
    }

    @Override
    public void validateOnUpdate(Long id, Quarter model) {
    }

    public void validateOnSave(Quarter model) {
        throwExceptionIfModelIsNull(model);
        validate(model);
    }

    public void validateActiveQuarterOnGet(LocalDate localDate) {
        if (localDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LocalDate can not be null");
        }
    }
}
