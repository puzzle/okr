package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class CheckInValidationService extends ValidationBase<CheckIn, Long> {
    public CheckInValidationService(CheckInPersistenceService checkInPersistenceService) {
        super(checkInPersistenceService);
    }

    @Override
    public void validateOnCreate(CheckIn model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long aLong, CheckIn model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        doesEntityExist(model.getId());

        validate(model);
    }
}
