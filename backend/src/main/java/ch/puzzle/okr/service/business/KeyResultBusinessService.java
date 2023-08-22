package ch.puzzle.okr.service.business;

import ch.puzzle.okr.dto.KeyResultMeasureDto;
import ch.puzzle.okr.mapper.KeyResultMeasureMapper;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.MeasurePersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class KeyResultBusinessService {

    private final KeyResultPersistenceService keyResultPersistenceService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final MeasurePersistenceService measurePersistenceService;
    private final KeyResultMeasureMapper keyResultMeasureMapper;

    public KeyResultBusinessService(KeyResultPersistenceService keyResultPersistenceService,
            ObjectivePersistenceService objectivePersistenceService,
            MeasurePersistenceService measurePersistenceService, KeyResultMeasureMapper keyResultMeasureMapper) {
        this.keyResultPersistenceService = keyResultPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.measurePersistenceService = measurePersistenceService;
        this.keyResultMeasureMapper = keyResultMeasureMapper;
    }

    public KeyResult saveKeyResult(KeyResult keyResult) {
        return keyResultPersistenceService.saveKeyResult(keyResult);
    }

    public KeyResult getKeyResultById(Long id) {
        return keyResultPersistenceService.getKeyResultById(id);
    }

    public KeyResult updateKeyResult(KeyResult keyResult) {
        return keyResultPersistenceService.updateKeyResult(keyResult);
    }

    public List<Measure> getAllMeasuresByKeyResult(long keyResultId) {
        KeyResult keyResult = keyResultPersistenceService.getKeyResultById(keyResultId);
        return measurePersistenceService.getMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResult.getId());
    }

    public List<Measure> getLastMeasures(Long objectiveId) {
        return measurePersistenceService.getLastMeasuresOfKeyresults(objectiveId);
    }

    public List<KeyResult> getAllKeyResultsByObjective(Long objectiveId) {
        Objective objective = objectivePersistenceService.getObjectiveById(objectiveId);
        return keyResultPersistenceService.getKeyResultsByObjective(objective);
    }

    public List<KeyResultMeasureDto> getAllKeyResultsByObjectiveWithMeasure(Long id) {
        List<Measure> measureList = getLastMeasures(id);
        return getAllKeyResultsByObjective(id).stream()
                .map(i -> keyResultMeasureMapper.toDto(i, measureList.stream()
                        .filter(j -> Objects.equals(j.getKeyResult().getId(), i.getId())).findFirst().orElse(null)))
                .toList();
    }

    @Transactional
    public void deleteKeyResultById(Long id) {
        List<Measure> measures = getAllMeasuresByKeyResult(id);
        for (Measure measure : measures) {
            measurePersistenceService.deleteMeasureById(measure.getId());
        }
        keyResultPersistenceService.deleteKeyResultById(id);
    }
}
