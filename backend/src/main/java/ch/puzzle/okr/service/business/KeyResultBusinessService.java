package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class KeyResultBusinessService {

    private final KeyResultPersistenceService keyResultPersistenceService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final MeasureBusinessService measureBusinessService;
    private final UserBusinessService userBusinessService;
    private final KeyResultValidationService validator;

    public KeyResultBusinessService(KeyResultPersistenceService keyResultPersistenceService,
            ObjectivePersistenceService objectivePersistenceService, MeasureBusinessService measureBusinessService,
            UserBusinessService userBusinessService, KeyResultValidationService validator) {
        this.keyResultPersistenceService = keyResultPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.measureBusinessService = measureBusinessService;
        this.userBusinessService = userBusinessService;
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
        keyResult.setModifiedOn(LocalDateTime.now());
        validator.validateOnUpdate(id, keyResult);
        return validateUpdatedKeyResult(id, keyResult);
    }

    @Transactional
    public void deleteKeyResultById(Long id) {
        validator.validateOnDelete(id);
        measureBusinessService.getMeasuresByKeyResultId(id)
                .forEach(measure -> measureBusinessService.deleteMeasureById(measure.getId()));
        keyResultPersistenceService.deleteById(id);
    }

    public List<Measure> getAllMeasuresByKeyResult(long keyResultId) {
        KeyResult keyResult = keyResultPersistenceService.findById(keyResultId);
        return measureBusinessService.getMeasuresByKeyResultId(keyResult.getId());
    }

    public List<KeyResult> getAllKeyResultsByObjective(Long objectiveId) {
        Objective objective = objectivePersistenceService.findById(objectiveId);
        return keyResultPersistenceService.getKeyResultsByObjective(objective);
    }

    public KeyResult validateUpdatedKeyResult(Long id, KeyResult keyResult) {
        KeyResult savedKeyResult = getKeyResultById(id);
        if ((savedKeyResult instanceof KeyResultMetric && keyResult instanceof KeyResultOrdinal)
                || (savedKeyResult instanceof KeyResultOrdinal && keyResult instanceof KeyResultMetric)) {
            if (!measureBusinessService.getMeasuresByKeyResultId(id).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "You can not change the type of a KeyResult when there are CheckIns on this KeyResult");

                // TODO Update other values of key Result except type with baseline, unit, stretchGoal and the three
                // zones
            }
            return keyResultPersistenceService.save(keyResult);
        } else {
            return keyResultPersistenceService.save(keyResult);
        }
    }
}
