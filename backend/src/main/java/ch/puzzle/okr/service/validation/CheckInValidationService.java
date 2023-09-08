package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class CheckInValidationService extends ValidationBase<Measure, Long> {
    public CheckInValidationService(CheckInPersistenceService checkInPersistenceService) {
        super(checkInPersistenceService);
    }

    @Override
    public void validateOnCreate(Measure model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long aLong, Measure model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        doesEntityExist(model.getId());

        validate(model);
    }
}
