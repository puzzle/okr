package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class KeyResultBusinessService {

    private final KeyResultPersistenceService keyResultPersistenceService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final MeasureBusinessService measureBusinessService;
    private final KeyResultValidationService validator;

    public KeyResultBusinessService(KeyResultPersistenceService keyResultPersistenceService,
            ObjectivePersistenceService objectivePersistenceService, MeasureBusinessService measureBusinessService,
            KeyResultValidationService validator) {
        this.keyResultPersistenceService = keyResultPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.measureBusinessService = measureBusinessService;
        this.validator = validator;
    }

    @Transactional
    public KeyResult createKeyResult(KeyResult keyResult) {
        keyResult.setCreatedOn(LocalDateTime.now());
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
        return keyResultPersistenceService.save(keyResult);
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
}
