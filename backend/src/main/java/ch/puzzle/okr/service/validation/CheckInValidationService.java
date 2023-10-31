package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.repository.CheckInRepository;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
            throw new ResponseStatusException(BAD_REQUEST,
                    format("Not allowed change the association to the key result (id = %s)", savedCheckIn.getId()));
        }
    }
}
