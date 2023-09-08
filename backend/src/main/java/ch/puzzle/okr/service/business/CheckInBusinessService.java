package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import ch.puzzle.okr.service.validation.CheckInValidationService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

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

    public CheckIn saveCheckIn(CheckIn checkIn, Jwt token) {
        checkIn.setCreatedBy(userBusinessService.getUserByAuthorisationToken(token));
        validator.validateOnCreate(checkIn);
        return checkInPersistenceService.save(checkIn);
    }

    public CheckIn updateCheckIn(Long id, CheckIn checkIn, Jwt token) {
        checkIn.setCreatedBy(userBusinessService.getUserByAuthorisationToken(token));
        validator.validateOnUpdate(id, checkIn);
        return checkInPersistenceService.save(checkIn);
    }
}
