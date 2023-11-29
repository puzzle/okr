package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.repository.CheckInRepository;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CheckInValidationService
        extends ValidationBase<CheckIn, Long, CheckInRepository, CheckInPersistenceService> {

    public CheckInValidationService(CheckInPersistenceService checkInPersistenceService) {
        super(checkInPersistenceService);
    }

    @Override
    public void validateOnCreate(CheckIn model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, CheckIn model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(id);
        throwExceptionWhenIdHasChanged(id, model.getId());
        CheckIn savedCheckIn = doesEntityExist(id);
        throwExceptionWhenKeyResultHasChanged(model, savedCheckIn);
        validate(model);
    }

    private static void throwExceptionWhenKeyResultHasChanged(CheckIn checkIn, CheckIn savedCheckIn) {
        if (!Objects.equals(checkIn.getKeyResult().getId(), savedCheckIn.getKeyResult().getId())) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_CANNOT_CHANGE,
                    List.of(Constants.KEY_RESULT, Constants.CHECK_IN));
        }
    }
}
