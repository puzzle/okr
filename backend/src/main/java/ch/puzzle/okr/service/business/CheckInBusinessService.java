package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Measure;
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

    public Measure getMeasureById(Long id) {
        validator.validateOnGet(id);
        return checkInPersistenceService.findById(id);
    }

    public Measure saveMeasure(Measure measure, Jwt token) {
        measure.setCreatedBy(userBusinessService.getUserByAuthorisationToken(token));
        validator.validateOnCreate(measure);
        return checkInPersistenceService.save(measure);
    }

    public Measure updateMeasure(Long id, Measure measure, Jwt token) {
        measure.setCreatedBy(userBusinessService.getUserByAuthorisationToken(token));
        validator.validateOnUpdate(id, measure);
        return checkInPersistenceService.save(measure);
    }
}
