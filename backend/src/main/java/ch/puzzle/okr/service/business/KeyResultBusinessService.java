package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class KeyResultBusinessService {

    private final KeyResultPersistenceService keyResultPersistenceService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final CheckInBusinessService checkInBusinessService;
    private final UserBusinessService userBusinessService;
    private final KeyResultValidationService validator;

    public KeyResultBusinessService(KeyResultPersistenceService keyResultPersistenceService,
            ObjectivePersistenceService objectivePersistenceService, UserBusinessService userBusinessService,
                                    KeyResultValidationService validator, CheckInBusinessService checkInBusinessService) {
        this.keyResultPersistenceService = keyResultPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.userBusinessService = userBusinessService;
        this.checkInBusinessService = checkInBusinessService;
        this.validator = validator;
    }

    @Transactional
    public KeyResult createKeyResult(KeyResult keyResult, Jwt token) {
        keyResult.setCreatedOn(LocalDateTime.now());
        keyResult.setCreatedBy(userBusinessService.getUserByAuthorisationToken(token));
        validator.validateOnCreate(keyResult);
        return keyResultPersistenceService.save(keyResult);
    }

    public KeyResult getKeyResultById(Long id) {
        validator.validateOnGet(id);
        return keyResultPersistenceService.findById(id);
    }

    @Transactional
    public KeyResult updateKeyResult(Long id, KeyResult keyResult) {
        KeyResult savedKeyResult = keyResultPersistenceService.findById(id);
        keyResult.setCreatedBy(savedKeyResult.getCreatedBy());
        keyResult.setCreatedOn(savedKeyResult.getCreatedOn());
        keyResult.setObjective(savedKeyResult.getObjective());
        keyResult.setModifiedOn(LocalDateTime.now());
        if (isKeyResultTypeChangeable(id)) {
            validator.validateOnUpdate(id, keyResult);
            return keyResultPersistenceService.updateEntity(id, keyResult);
        } else {
            savedKeyResult.setTitle(keyResult.getTitle());
            savedKeyResult.setDescription(keyResult.getDescription());
            savedKeyResult.setOwner(keyResult.getOwner());
            savedKeyResult.setModifiedOn(keyResult.getModifiedOn());
            validator.validateOnUpdate(id, keyResult);
            return keyResultPersistenceService.updateAbstractEntity(savedKeyResult);
        }
    }

    @Transactional
    public void deleteKeyResultById(Long id) {
        validator.validateOnDelete(id);
        checkInBusinessService.getCheckInsByKeyResultId(id)
                .forEach(measure -> checkInBusinessService.deleteCheckIn(measure.getId()));
        keyResultPersistenceService.deleteById(id);
    }

    public List<CheckIn> getAllMeasuresByKeyResult(long keyResultId) {
        KeyResult keyResult = keyResultPersistenceService.findById(keyResultId);
        return checkInBusinessService.getCheckInsByKeyResultId(keyResult.getId());
    }

    public List<KeyResult> getAllKeyResultsByObjective(Long objectiveId) {
        Objective objective = objectivePersistenceService.findById(objectiveId);
        return keyResultPersistenceService.getKeyResultsByObjective(objective);
    }

    private boolean isKeyResultTypeChangeable(Long id) {
        return checkInBusinessService.getCheckInsByKeyResultId(id).isEmpty();
    }

    public boolean isImUsed(Long id, KeyResult keyResult) {
        return !checkInBusinessService.getCheckInsByKeyResultId(id).isEmpty()
                && !keyResultPersistenceService.findById(id).getKeyResultType().equals(keyResult.getKeyResultType());
    }
}
