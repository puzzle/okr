package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public void validateOnUpdate(Long id, CheckIn model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNull(id);
        throwExceptionWhenIdHasChanged(id, model.getId());
        doesEntityExist(id);

        List<CheckIn> checkInsOfKeyResult = ((CheckInPersistenceService) this.persistenceService)
                .getCheckInsByKeyResultIdOrderByCheckInDateDesc(model.getKeyResult().getId());
        checkInsOfKeyResult = checkInsOfKeyResult.stream().filter(checkIn -> checkIn.getId().equals(id)).toList();
        if (checkInsOfKeyResult.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not change key result id of check-in");
        }
        validate(model);
    }
}
