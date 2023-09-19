package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import ch.puzzle.okr.service.validation.CheckInValidationService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckInBusinessService {
    private final CheckInPersistenceService checkInPersistenceService;
    private final CheckInValidationService validator;
    private final UserBusinessService userBusinessService;

    public CheckInBusinessService(CheckInPersistenceService checkInPersistenceService,
            CheckInValidationService validator, UserBusinessService userBusinessService) {
        this.checkInPersistenceService = checkInPersistenceService;
        this.validator = validator;
        this.userBusinessService = userBusinessService;
    }

    public CheckIn getCheckInById(Long id) {
        validator.validateOnGet(id);
        return checkInPersistenceService.findById(id);
    }

    @Transactional
    public CheckIn createCheckIn(CheckIn checkIn, Jwt token) {
        checkIn.setCreatedOn(LocalDateTime.now());
        checkIn.setCreatedBy(userBusinessService.getUserByAuthorisationToken(token));
        validator.validateOnCreate(checkIn);
        return checkInPersistenceService.save(checkIn);
    }

    @Transactional
    public CheckIn updateCheckIn(Long id, CheckIn checkIn) {
        CheckIn savedCheckIn = checkInPersistenceService.findById(id);
        checkIn.setCreatedOn(savedCheckIn.getCreatedOn());
        checkIn.setCreatedBy(savedCheckIn.getCreatedBy());
        checkIn.setModifiedOn(LocalDateTime.now());
        validator.validateOnUpdate(id, checkIn);
        return checkInPersistenceService.save(checkIn);
    }

    @Transactional
    public void deleteCheckIn(Long id) {
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
