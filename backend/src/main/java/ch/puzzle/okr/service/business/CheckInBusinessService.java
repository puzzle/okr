package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import ch.puzzle.okr.service.validation.CheckInValidationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckInBusinessService implements BusinessServiceInterface<Long, CheckIn> {
    private final CheckInPersistenceService checkInPersistenceService;
    private final CheckInValidationService validator;

    public CheckInBusinessService(CheckInPersistenceService checkInPersistenceService,
            CheckInValidationService validator) {
        this.checkInPersistenceService = checkInPersistenceService;
        this.validator = validator;
    }

    public CheckIn getEntityById(Long id) {
        validator.validateOnGet(id);
        return checkInPersistenceService.findById(id);
    }

    @Transactional
    public CheckIn createEntity(CheckIn checkIn, AuthorizationUser authorizationUser) {
        checkIn.setCreatedOn(LocalDateTime.now());
        checkIn.setModifiedOn(LocalDateTime.now());
        checkIn.setCreatedBy(authorizationUser.user());
        validator.validateOnCreate(checkIn);
        return checkInPersistenceService.save(checkIn);
    }

    @Transactional
    public CheckIn updateEntity(Long id, CheckIn checkIn, AuthorizationUser authorizationUser) {
        CheckIn savedCheckIn = checkInPersistenceService.findById(id);
        checkIn.setCreatedOn(savedCheckIn.getCreatedOn());
        checkIn.setCreatedBy(savedCheckIn.getCreatedBy());
        checkIn.setModifiedOn(LocalDateTime.now());
        validator.validateOnUpdate(id, checkIn);
        return checkInPersistenceService.save(checkIn);
    }

    @Transactional
    public void deleteEntityById(Long id) {
        validator.validateOnDelete(id);
        checkInPersistenceService.deleteById(id);
    }

    public List<CheckIn> getCheckInsByKeyResultId(Long keyResultId) {
        return checkInPersistenceService.getCheckInsByKeyResultIdOrderByCheckInDateDesc(keyResultId);
    }

    public CheckIn getLastCheckInByKeyResultId(Long keyResultId) {
        return checkInPersistenceService.getLastCheckInOfKeyResult(keyResultId);
    }
}
